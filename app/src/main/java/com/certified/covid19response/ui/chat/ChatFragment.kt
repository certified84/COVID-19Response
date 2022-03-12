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
import androidx.recyclerview.widget.LinearLayoutManager
import com.certified.covid19response.R
import com.certified.covid19response.adapter.ChatRecyclerAdapter
import com.certified.covid19response.data.model.DoctorConversation
import com.certified.covid19response.data.model.Message
import com.certified.covid19response.data.model.UserConversation
import com.certified.covid19response.databinding.FragmentChatBinding
import com.certified.covid19response.util.Extensions.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val args: ChatFragmentArgs by navArgs()
    private val viewModel: ChatViewModel by activityViewModels()

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
        viewModel.getChat("${auth.currentUser!!.uid}_${args.doctor.id}")

        binding.apply {
            tvHeading.text = "You & Doctor ${args.doctor.first_name}"
            btnBack.setOnClickListener { findNavController().navigate(ChatFragmentDirections.actionUserChatFragmentToUserChatListFragment()) }
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
        val db = Firebase.firestore

        val message = Message(
            id = id,
            message = text,
            senderId = auth.currentUser!!.uid,
            receiverId = args.doctor.id
        )

        val messagesRef = db.collection("messages").document(id).collection("messages").document()
        val senderLastMessageRef =
            db.collection("last_messages").document(auth.currentUser!!.uid).collection("messages")
                .document(id)
        val receiverLastMessageRef =
            db.collection("last_messages").document(args.doctor.id).collection("messages")
                .document(id)
        db.runBatch {
            it.set(messagesRef, message)
            it.set(
                senderLastMessageRef,
                UserConversation(senderLastMessageRef.id, args.doctor, message)
            )
            it.set(
                receiverLastMessageRef,
                DoctorConversation(receiverLastMessageRef.id, args.user, message)
            )
        }.addOnSuccessListener {
            binding.etMessage.setText("")
        }.addOnFailureListener {
            showToast("An error occurred: ${it.localizedMessage}")
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