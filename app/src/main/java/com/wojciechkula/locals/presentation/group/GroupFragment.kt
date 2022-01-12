package com.wojciechkula.locals.presentation.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wojciechkula.locals.databinding.FragmentGroupBinding
import com.wojciechkula.locals.presentation.common.SharedViewModel
import com.wojciechkula.locals.presentation.common.SharedViewState
import com.wojciechkula.locals.presentation.group.dialog.GroupDialogFragment
import com.wojciechkula.locals.presentation.group.list.MessagesListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GroupFragment : Fragment() {

    private var _binding: FragmentGroupBinding? = null
    private val binding
        get() = _binding!!

    private val args: GroupFragmentArgs by navArgs()

    private val viewModel: GroupViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val adapter by lazy {
        MessagesListAdapter(currentUserId = args.userId) { messageId -> }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGroupBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeViewModel()
        viewModel.getMessagesAndGroup(args.groupId)
    }

    private fun initViews() {
        with(binding) {
            messagesRecyclerView.adapter = adapter
            nameOutput.text = args.groupName
            backButton.setOnClickListener { findNavController().popBackStack() }
            infoButton.setOnClickListener { viewModel.openInfo() }
            sendButton.setOnClickListener {
                if (messageInput.text.isNotEmpty()) {
                    viewModel.sendMessage(messageInput.text.toString(), args.groupId)
                }
            }
        }
    }

    private fun observeViewModel() {
        sharedViewModel.viewState.observe(viewLifecycleOwner, ::bindSharedState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect(::bindState)
            }
        }
        viewModel.viewEvent.observe(viewLifecycleOwner, ::handleEvents)
    }

    private fun bindSharedState(state: SharedViewState) {
        viewModel.setUser(state.user)
    }

    private fun bindState(state: GroupViewState) {
        adapter.submitList(state.messages)
        binding.messagesRecyclerView.smoothScrollToPosition(state.messages.size)
    }

    private fun handleEvents(event: GroupViewEvent) {
        when (event) {
            GroupViewEvent.ShowNoMessagesYetLabel -> showNoMessagesYetLabel()
            is GroupViewEvent.OpenInfo -> openInfo(event)
        }
    }

    private fun showNoMessagesYetLabel() {
        binding.noMessagesInfoLabel.visibility = View.VISIBLE
    }

    private fun openInfo(event: GroupViewEvent.OpenInfo) {
        GroupDialogFragment.show(childFragmentManager, event.group)
    }

}