package com.wojciechkula.locals.presentation.explore.dialog

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wojciechkula.locals.common.dialog.LoadingDialogFragment
import com.wojciechkula.locals.databinding.LayoutExploreDialogBinding
import com.wojciechkula.locals.domain.model.UserModel
import com.wojciechkula.locals.extension.showSnackbarError
import com.wojciechkula.locals.presentation.common.SharedViewModel
import com.wojciechkula.locals.presentation.common.SharedViewState
import com.wojciechkula.locals.presentation.explore.ExploreFragmentDirections
import com.wojciechkula.locals.presentation.explore.list.ExploreItem
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class ExploreDialogFragment(private val group: ExploreItem) : DialogFragment() {

    private var _binding: LayoutExploreDialogBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var user: UserModel

    private val viewModel: ExploreDialogViewModel by viewModels()
    private val sharedVewModel: SharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = true

        _binding = LayoutExploreDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (dialog != null) dialog!!.window!!.setLayout(550, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        initViews()
        observeViewModel()

    }

    @SuppressLint("SimpleDateFormat")
    private fun initViews() {
        with(binding)
        {
            if (group.avatarBitmap != null) {
                groupAvatarImage.setImageBitmap(group.avatarBitmap)
            }
            groupNameOutput.text = group.name
            groupSizeOutput.text = group.size.toString()
            val dateFormat = SimpleDateFormat("dd.MM.yyyy")
            val lastActivityDate = dateFormat.format(group.lastActivity.toDate())
            lastActivityOutput.text = lastActivityDate
            var hobbies: String = ""
            for (hobby in group.hobbies) {
                hobbies = "$hobbies$hobby, "
            }
            groupHobbiesOutput.text = hobbies
            groupDescriptionOutput.text = group.description
            closeButton.setOnClickListener {
                closeDialog()
            }
            joinGroupButton.setOnClickListener {
                viewModel.onJoinButtonClick(group.id)
            }
        }
    }

    private fun closeDialog() {
        this.dismiss()
    }

    private fun observeViewModel() {
        sharedVewModel.viewState.observe(viewLifecycleOwner, ::bindSharedState)
        viewModel.viewEvent.observe(viewLifecycleOwner, ::handleEvents)
        viewModel.showLoading.observe(viewLifecycleOwner, ::handleLoading)
    }

    private fun bindSharedState(state: SharedViewState) {
        user = state.user
    }

    private fun handleEvents(event: ExploreDialogViewEvent) {
        when (event) {
            ExploreDialogViewEvent.JoinGroup -> onJoinGroup()
            is ExploreDialogViewEvent.ShowError -> onError(event.exception)
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        LoadingDialogFragment.toggle(childFragmentManager, isLoading)
    }

    private fun onJoinGroup() {
        this.dismiss()
        findNavController().navigate(ExploreFragmentDirections.openJoinedGroup(group.id, group.name, user.id))
    }

    private fun onError(exception: Exception) {
        binding.showSnackbarError(exception.message.toString())
    }

    companion object {
        private const val TAG = "explore_dialog_fragment"

        fun show(fragmentManager: FragmentManager, group: ExploreItem): ExploreDialogFragment {
            val fragment = ExploreDialogFragment(group)
            fragment.show(fragmentManager, TAG)
            return fragment
        }
    }
}