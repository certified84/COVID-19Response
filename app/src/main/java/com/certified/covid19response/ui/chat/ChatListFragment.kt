package com.certified.covid19response.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.certified.covid19response.adapter.ChatListRecyclerAdapter
import com.certified.covid19response.data.model.User
import com.certified.covid19response.data.model.UserConversation
import com.certified.covid19response.databinding.FragmentChatListBinding
import com.certified.covid19response.util.PreferenceKeys
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatListFragment : Fragment() {

    private var _binding: FragmentChatListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChatViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentChatListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.uiState = viewModel.chatListUiState
        viewModel.getUserConversations(Firebase.auth.currentUser!!.uid)

        binding.apply {
            val adapter = ChatListRecyclerAdapter()
            adapter.setOnItemClickedListener(object :
                ChatListRecyclerAdapter.OnItemClickedListener {
                override fun onItemClick(conversation: UserConversation) {

                    val preferenceManager =
                        PreferenceManager.getDefaultSharedPreferences(requireContext())
                    preferenceManager.apply {
                        val user = User(
                            getString(PreferenceKeys.USER_ID_KEY, "")!!,
                            getString(PreferenceKeys.USER_NAME_KEY, "")!!,
                            getString(PreferenceKeys.USER_EMAIL_KEY, "")!!,
                            getString(PreferenceKeys.USER_PROFILE_IMAGE_KEY, "")!!,
                            getString(PreferenceKeys.USER_LOCATION_KEY, "")!!,
                            getString(PreferenceKeys.USER_NIN_KEY, "")!!,
                            getString(PreferenceKeys.USER_BIO_KEY, "")!!
                        )
                        findNavController().navigate(
                            ChatListFragmentDirections.actionUserChatListFragmentToUserChatFragment(
                                user, conversation.doctor
                            )
                        )
                    }
                }
            })
            recyclerViewChats.adapter = adapter
            recyclerViewChats.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}