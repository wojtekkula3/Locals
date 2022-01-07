package com.wojciechkula.locals.presentation.group.dialog

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wojciechkula.locals.R
import com.wojciechkula.locals.databinding.LayoutGroupDialogBinding
import com.wojciechkula.locals.domain.model.GroupModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupDialogFragment(private val group: GroupModel) : DialogFragment() {

    private var _binding: LayoutGroupDialogBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: GroupDialogViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = true

        _binding = LayoutGroupDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (dialog != null) dialog!!.window!!.setLayout(550, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        initViews()
        observeViewModel()
    }

    private fun initViews() {
        with(binding) {
            groupNameOutput.text = group.name
            var hobbies = ""
            for (hobby in group.hobbies) {
                hobbies = "$hobbies$hobby, "
            }
            groupHobbiesOutput.text = hobbies
            leaveGroupButton.setOnClickListener { showAcceptDialog() }
            closeButton.setOnClickListener { closeGroupDialog() }
        }
    }

    private fun showAcceptDialog() {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setMessage(getString(R.string.group_are_you_sure_you_want_to_leave))
            .setCancelable(true)
            .setPositiveButton(getString(R.string.common_confirm)) { dialog, id ->
                viewModel.leaveGroup(group.id)
            }
            .setNegativeButton(getString(R.string.common_decline)) { dialog, id ->
                dialog.cancel()
            }

        val alert = dialog.create()
        alert.setTitle(getString(R.string.group_dialog_confirm_to_leave))
        alert.show()
    }

    private fun closeGroupDialog() {
        this@GroupDialogFragment.dismiss()
    }

    private fun observeViewModel() {
        viewModel.viewEvent.observe(viewLifecycleOwner, ::handleEvents)
    }

    private fun handleEvents(event: GroupDialogViewEvent) {
        when (event) {
            GroupDialogViewEvent.OpenMyGroups -> openMyGroups()
        }
    }

    private fun openMyGroups() {
        findNavController().popBackStack()
    }

    companion object {
        private const val TAG = "group_dialog_fragment"

        fun show(fragmentManager: FragmentManager, group: GroupModel): GroupDialogFragment {
            val fragment = GroupDialogFragment(group)
            fragment.show(fragmentManager, TAG)
            return fragment
        }
    }
}