package com.certified.covid19response.ui.chat

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.certified.covid19response.R
import com.certified.covid19response.adapter.ChatRecyclerAdapter
import com.certified.covid19response.data.model.Message
import com.certified.covid19response.databinding.FragmentChatBinding
import com.certified.covid19response.ui.MainActivity
import com.certified.covid19response.ui.profile.ProfileFragment
import com.certified.covid19response.util.*
import com.certified.covid19response.util.Extensions.showToast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.AndroidEntryPoint
import timerx.Stopwatch
import timerx.StopwatchBuilder
import timerx.Timer
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private val args: ChatFragmentArgs by navArgs()
    private val viewModel: ChatViewModel by activityViewModels()

    private lateinit var auth: FirebaseAuth
    private val preferences by lazy { PreferenceManager.getDefaultSharedPreferences(requireContext()) }
    private val filePath by lazy { filePath(requireActivity()) }

    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var stopWatch: Stopwatch? = null
//    private var timer: Timer? = null

    private var isRecording = false
    private var isPlayingRecord = false
    private var isPause = false
    private lateinit var file: String
    private lateinit var storage: FirebaseStorage
    private var audioLength by Delegates.notNull<Long>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        storage = Firebase.storage
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.uiState = viewModel.uiState
        viewModel.getChats("${args.conversation?.sender?.id}_${args.conversation?.receiver?.id}")

        binding.apply {
            tvHeading.text = if (preferences.getString(PreferenceKeys.ACCOUNT_TYPE, "") == "user") {
                if (auth.currentUser?.uid == args.conversation?.receiver?.id)
                    "You & Doctor ${args.conversation?.sender?.name?.substringAfter("D_")}"
                else
                    "You & Doctor ${args.conversation?.receiver?.name?.substringAfter("D_")}"
            } else {
                if (auth.currentUser?.uid == args.conversation?.receiver?.id)
                    "You & ${args.conversation?.sender?.name}"
                else
                    "You & ${args.conversation?.receiver?.name}"
            }

            btnBack.setOnClickListener { findNavController().navigate(ChatFragmentDirections.actionChatFragmentToChatListFragment()) }
            btnAttachment.setOnClickListener { showAttachmentDialog() }
            btnDeleteRecording.setOnClickListener {
                isRecording = false
                deleteRecording()
            }
            fabAction.setOnClickListener {
                val message = etMessage.text.toString().trim()
                if (message.isBlank())
                    recordAudio()
                else {
                    val id = "${args.conversation?.sender?.id}_${args.conversation?.receiver?.id}"
                    viewModel?.sendMessage(
                        id = id,
                        text = message,
                        sender = if (args.conversation?.sender?.id == auth.currentUser?.uid) args.conversation?.sender?.copy(
                            profile_image = preferences.getString(
                                PreferenceKeys.USER_PROFILE_IMAGE_KEY,
                                ""
                            ),
                            location = preferences.getString(
                                PreferenceKeys.USER_LOCATION_KEY,
                                ""
                            )!!,
                            nin = preferences.getString(PreferenceKeys.USER_NIN_KEY, "")!!,
                            bio = preferences.getString(PreferenceKeys.USER_BIO_KEY, "")!!,
                            sex = preferences.getString(PreferenceKeys.USER_SEX_KEY, "")!!,
                            position = preferences.getString(PreferenceKeys.USER_POSITION_KEY, "")!!
                        ) else args.conversation?.sender,
                        receiver = if (args.conversation?.receiver?.id == auth.currentUser?.uid) args.conversation?.receiver?.copy(
                            profile_image = preferences.getString(
                                PreferenceKeys.USER_PROFILE_IMAGE_KEY,
                                ""
                            ),
                            location = preferences.getString(
                                PreferenceKeys.USER_LOCATION_KEY,
                                ""
                            )!!,
                            nin = preferences.getString(PreferenceKeys.USER_NIN_KEY, "")!!,
                            bio = preferences.getString(PreferenceKeys.USER_BIO_KEY, "")!!,
                            sex = preferences.getString(PreferenceKeys.USER_SEX_KEY, "")!!,
                            position = preferences.getString(PreferenceKeys.USER_POSITION_KEY, "")!!
                        ) else args.conversation?.receiver, Message()
                    )
                    binding.etMessage.setText("")
                }
            }

            etMessage.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    if (s.toString().isBlank())
                        fabAction.setImageDrawable(
                            ResourcesCompat.getDrawable(
                                resources,
                                R.drawable.ic_wave,
                                requireContext().theme
                            )
                        )
                    else
                        fabAction.setImageDrawable(
                            ResourcesCompat.getDrawable(
                                resources,
                                R.drawable.ic_send_black_24dp,
                                requireContext().theme
                            )
                        )
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    fabAction.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_send_black_24dp,
                            requireContext().theme
                        )
                    )
                }

                override fun afterTextChanged(s: Editable?) {
                    if (s.toString().isBlank())
                        fabAction.setImageDrawable(
                            ResourcesCompat.getDrawable(
                                resources,
                                R.drawable.ic_wave,
                                requireContext().theme
                            )
                        )
                }
            })

            val adapter = ChatRecyclerAdapter(auth.currentUser!!.uid)
            recyclerViewChat.adapter = adapter
            recyclerViewChat.layoutManager = LinearLayoutManager(requireContext())
            adapter.setOnItemClickedListener(object : ChatRecyclerAdapter.OnItemClickedListener {
                override fun onItemClick(message: Message, timer: Timer?) {
                    if (message.record != null) {
                        isPlayingRecord = if (!isPlayingRecord) {
                            if (!isPause)
                                startPlayingRecording(message, timer)
                            else
                                continuePlayingRecording(timer)
                            true
                        } else {
                            if (!isPause)
                                pausePlayingRecording(timer)
                            else
                                stopPlayingRecording(timer)
                            false
                        }
                    }
//                    else if (message.record != null && isPlayingRecord) stopPlayingRecording(timer)
                }
            })

            viewModel?.messages?.observe(viewLifecycleOwner) {
                adapter.notifyDataSetChanged()
                    .run { recyclerViewChat.scrollToPosition(it.size - 1) }
            }
        }
    }

    private fun showAttachmentDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val selection = arrayOf(
            "Take picture",
            "Choose from gallery",
        )
        builder.setTitle("Options")
        builder.setSingleChoiceItems(selection, -1) { dialog, which ->
            when (which) {
                0 -> launchCamera()
                1 -> chooseFromGallery()
            }
            dialog.dismiss()
        }
        builder.show()
    }

    private fun launchCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, ProfileFragment.REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            showToast("An error occurred: ${e.localizedMessage}")
        }
    }

    private fun chooseFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
        try {
            startActivityForResult(
                Intent.createChooser(intent, "Select image"),
                ProfileFragment.PICK_IMAGE_CODE
            )
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "An error occurred: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ProfileFragment.REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            assert(data != null)
            try {
                val bitmap = data?.extras!!["data"] as Bitmap?
                requireContext().openFileOutput("profile_image", Context.MODE_PRIVATE).use {
                    bitmap?.compress(Bitmap.CompressFormat.PNG, 100, it)
                }
                val file = File(requireContext().filesDir, "profile_image")
                viewModel.uiState.set(UIState.LOADING)
                uploadImage(Uri.fromFile(file))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else if (requestCode == ProfileFragment.PICK_IMAGE_CODE && resultCode == Activity.RESULT_OK) {
            assert(data != null)
            try {
                viewModel.uiState.set(UIState.LOADING)
                uploadImage(data?.data)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadImage(uri: Uri?) {
        val id = "${args.conversation?.sender?.id}_${args.conversation?.receiver?.id}"
        val path = "chatImages/${auth.currentUser!!.uid}/${System.currentTimeMillis()}.jpg"
        viewModel.apply {
            uiState.set(UIState.LOADING)
            uploadImage(
                uri = uri,
                path = path,
                storage = storage,
                id = id,
                sender = if (args.conversation?.sender?.id == auth.currentUser?.uid) args.conversation?.sender?.copy(
                    profile_image = preferences.getString(
                        PreferenceKeys.USER_PROFILE_IMAGE_KEY,
                        ""
                    ),
                    location = preferences.getString(
                        PreferenceKeys.USER_LOCATION_KEY,
                        ""
                    )!!,
                    nin = preferences.getString(PreferenceKeys.USER_NIN_KEY, "")!!,
                    bio = preferences.getString(PreferenceKeys.USER_BIO_KEY, "")!!,
                    sex = preferences.getString(PreferenceKeys.USER_SEX_KEY, "")!!,
                    position = preferences.getString(PreferenceKeys.USER_POSITION_KEY, "")!!
                ) else args.conversation?.sender,
                receiver = if (args.conversation?.receiver?.id == auth.currentUser?.uid) args.conversation?.receiver?.copy(
                    profile_image = preferences.getString(
                        PreferenceKeys.USER_PROFILE_IMAGE_KEY,
                        ""
                    ),
                    location = preferences.getString(
                        PreferenceKeys.USER_LOCATION_KEY,
                        ""
                    )!!,
                    nin = preferences.getString(PreferenceKeys.USER_NIN_KEY, "")!!,
                    bio = preferences.getString(PreferenceKeys.USER_BIO_KEY, "")!!,
                    sex = preferences.getString(PreferenceKeys.USER_SEX_KEY, "")!!,
                    position = preferences.getString(PreferenceKeys.USER_POSITION_KEY, "")!!
                ) else args.conversation?.receiver
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if (args.message != null)
            binding.etMessage.apply {
                setText("Hi Doctor ${args.conversation?.receiver?.name?.substringAfter("D_")}, my name is ${auth.currentUser!!.displayName}.\n\nRecently, i've been feeling ${args.message}")
            }
    }

    private fun recordAudio() {
        binding.apply {
            if (!isRecording)
                if (hasPermission(requireContext(), Manifest.permission.RECORD_AUDIO))
                    fabAction.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_send_black_24dp,
                            null
                        )
                    ).run {
                        isRecording = true
                        groupRecording.visibility = View.VISIBLE
                        startRecording()
                    }
                else
                    requestPermission(
                        requireActivity(),
                        requireContext().getString(R.string.permission_required),
                        MainActivity.RECORD_AUDIO_PERMISSION_CODE,
                        Manifest.permission.RECORD_AUDIO
                    )
            else uploadRecording()
        }
    }

    private fun startRecording() {
        val fileName = "${System.currentTimeMillis()}.3gp"
        file = "$filePath/$fileName"

        stopWatch = StopwatchBuilder()
            .startFormat("MM:SS")
            .onTick { time -> binding.tvTimer.text = time }
            .changeFormatWhen(1, TimeUnit.HOURS, "HH:MM:SS")
            .build()

        mediaRecorder = MediaRecorder()
        mediaRecorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(file)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            try {
                prepare()
                start()
                stopWatch!!.start()
            } catch (e: IOException) {
                showToast("An error occurred: ${e.localizedMessage}")
            }
        }
    }

    private fun deleteRecording() {
        binding.apply {
            fabAction.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_wave,
                    null
                )
            )
            mediaRecorder?.apply {
                stop()
                Log.d("TAG", "deleteRecording: Length: ${stopWatch?.getTimeIn(TimeUnit.SECONDS)}")
                release()
            }
            mediaRecorder = null
            stopWatch?.apply {
                stop()
                reset()
            }
            stopWatch = null
            try {
                File(file).apply {
                    Log.d("TAG", "deleteRecording: File: $name")
                    delete()
                }

            } catch (e: IOException) {
                showToast("An error occurred: ${e.localizedMessage}")
                Log.d("TAG", "deleteRecording: ${e.localizedMessage}")
            }
            groupRecording.visibility = View.GONE
        }
    }

    private fun uploadRecording() {
        binding.apply {
            fabAction.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_wave,
                    null
                )
            )
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null
            stopWatch?.apply {
                stop()
                audioLength = stopWatch!!.getTimeIn(TimeUnit.SECONDS)
                reset()
            }
            stopWatch = null
            if (audioLength <= 0)
                return
            val audioRecord = File(file)
            val id = "${args.conversation?.sender?.id}_${args.conversation?.receiver?.id}"
            val path = "chatVoiceRecords/${auth.currentUser!!.uid}/${audioRecord.name}"
            viewModel?.apply {
                uiState.set(UIState.LOADING)
                uploadVoiceRecord(
                    uri = Uri.fromFile(audioRecord),
                    path = path,
                    storage = storage,
                    id = id,
                    sender = if (args.conversation?.sender?.id == auth.currentUser?.uid) args.conversation?.sender?.copy(
                        profile_image = preferences.getString(
                            PreferenceKeys.USER_PROFILE_IMAGE_KEY,
                            ""
                        ),
                        location = preferences.getString(
                            PreferenceKeys.USER_LOCATION_KEY,
                            ""
                        )!!,
                        nin = preferences.getString(PreferenceKeys.USER_NIN_KEY, "")!!,
                        bio = preferences.getString(PreferenceKeys.USER_BIO_KEY, "")!!,
                        sex = preferences.getString(PreferenceKeys.USER_SEX_KEY, "")!!,
                        position = preferences.getString(PreferenceKeys.USER_POSITION_KEY, "")!!
                    ) else args.conversation?.sender,
                    receiver = if (args.conversation?.receiver?.id == auth.currentUser?.uid) args.conversation?.receiver?.copy(
                        profile_image = preferences.getString(
                            PreferenceKeys.USER_PROFILE_IMAGE_KEY,
                            ""
                        ),
                        location = preferences.getString(
                            PreferenceKeys.USER_LOCATION_KEY,
                            ""
                        )!!,
                        nin = preferences.getString(PreferenceKeys.USER_NIN_KEY, "")!!,
                        bio = preferences.getString(PreferenceKeys.USER_BIO_KEY, "")!!,
                        sex = preferences.getString(PreferenceKeys.USER_SEX_KEY, "")!!,
                        position = preferences.getString(PreferenceKeys.USER_POSITION_KEY, "")!!
                    ) else args.conversation?.receiver, audioLength
                )
                deleteRecording()
            }
            groupRecording.visibility = View.GONE
        }
    }

    private fun startPlayingRecording(message: Message, timer: Timer?) {
//        stopPlayingRecording(timer)
        mediaPlayer = MediaPlayer()
        try {
            mediaPlayer?.apply {
                setDataSource(message.record?.record)
                prepare()
                start()
                timer!!.start()
                isPlayingRecord = true
            }
        } catch (e: IOException) {
            Log.d("TAG", "startPlayingRecording: ${e.localizedMessage}")
            showToast("An error occurred: ${e.localizedMessage}")
        }
        when (timer?.getRemainingTimeIn(TimeUnit.SECONDS)) {
            0L -> stopPlayingRecording(timer)
        }
    }

    private fun pausePlayingRecording(timer: Timer?) {
        isPause = true
        mediaPlayer?.pause()
        timer?.stop()
    }

    private fun continuePlayingRecording(timer: Timer?) {
        isPause = false
        mediaPlayer?.start()
        timer?.start()
    }

    fun stopPlayingRecording(timer: Timer?) {
        mediaPlayer?.apply {
            stop()
            release()
        }
        timer?.apply {
            reset()
            stop()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (isPlayingRecord)
            mediaPlayer?.apply {
                stop()
                release()
            }
        mediaRecorder = null
        mediaPlayer = null
        stopWatch?.apply {
            stop()
            reset()
        }
        stopWatch = null
        _binding = null
    }
}