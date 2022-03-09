package com.certified.covid19response.ui.chat

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.KeyListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.certified.covid19response.R
import com.certified.covid19response.adapter.ChatRecyclerAdapter
import com.certified.covid19response.data.model.Message
import com.certified.covid19response.databinding.FragmentUserChatBinding
import com.certified.covid19response.util.Extensions.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserChatFragment : Fragment() {

    private var _binding: FragmentUserChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val args: UserChatFragmentArgs by navArgs()
    private val viewModel: ChatViewModel by activityViewModels()
    private lateinit var messageKeyListener: KeyListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserChatBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.uiState = viewModel.uiState
        viewModel.getChat("${auth.currentUser!!.uid}_${args.doctor.id}")

        binding.apply {
            tvHeading.text = "You & Doctor ${args.doctor.first_name}"
            messageKeyListener = etMessage.keyListener
            btnBack.setOnClickListener { findNavController().navigate(UserChatFragmentDirections.actionUserChatFragmentToChatListFragment()) }
            fabAction.setOnClickListener {
                val message = etMessage.text.toString().trim()
                if (message.isBlank())
                    recordAudio()
                else
                    sendMessage(message)
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

    private fun sendMessage(text: String) {
        val id = "${auth.currentUser!!.uid}_${args.doctor.id}"
        val message = Message(
            id = id,
            message = text,
            senderId = auth.currentUser!!.uid,
            receiverId = args.doctor.id
        )
        val db = Firebase.firestore
        val messagesRef = db.collection("messages").document(id).collection("messages").document()
        val lastMessageRef =
            db.collection("lastMessages").document(auth.currentUser!!.uid).collection("messages")
                .document()
        messagesRef.set(message).addOnSuccessListener {
            binding.etMessage.setText("")
        }.addOnFailureListener {
            showToast("An error occurred: ${it.localizedMessage}")
        }
    }

    private fun recordAudio() {
//        TODO("Not yet implemented")
        showToast("Audio recording is on its way...")
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}