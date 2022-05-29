package com.certified.covid19response.ui.chat

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.certified.covid19response.R
import com.certified.covid19response.adapter.ChatRecyclerAdapter
import com.certified.covid19response.databinding.FragmentChatBinding
import com.certified.covid19response.util.Extensions.showToast
import com.certified.covid19response.util.PreferenceKeys
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val args: ChatFragmentArgs by navArgs()
    private val viewModel: ChatViewModel by activityViewModels()
    private val preferences by lazy { PreferenceManager.getDefaultSharedPreferences(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        auth = Firebase.auth
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
                        ) else args.conversation?.receiver
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
        }
    }

    private fun showAttachmentDialog() {
//        TODO("Not yet implemented")
        showToast("You'll be able to send attachments soon...")
    }

    override fun onResume() {
        super.onResume()
        if (args.message != null)
            binding.etMessage.apply {
                setText("Hi Doctor ${args.conversation?.receiver?.name?.substringAfter("D_")}, my name is ${auth.currentUser!!.displayName}.\n\nRecently, i've been feeling ${args.message}")
            }
    }

    private fun recordAudio() {
//        TODO("Not yet implemented")
        showToast("Audio recording is on its way...")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}