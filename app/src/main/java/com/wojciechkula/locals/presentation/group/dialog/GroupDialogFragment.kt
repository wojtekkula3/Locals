package com.wojciechkula.locals.presentation.group.dialog

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.wojciechkula.locals.R
import com.wojciechkula.locals.common.dialog.LoadingDialogFragment
import com.wojciechkula.locals.databinding.LayoutGroupDialogBinding
import com.wojciechkula.locals.domain.model.GroupModel
import com.wojciechkula.locals.extension.showSnackbarError
import com.wojciechkula.locals.extension.showSnackbarInfo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupDialogFragment(private val group: GroupModel) : DialogFragment() {

    private var _binding: LayoutGroupDialogBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: GroupDialogViewModel by viewModels()

    private val PICK_IMAGE = 100
    private var previewBitmap: Bitmap? = null

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
            if (!group.avatar.isNullOrEmpty()) {
                groupAvatarImage.load(group.avatar)
            }
            groupNameOutput.text = group.name
            var hobbies = ""
            for (hobby in group.hobbies) {
                hobbies = "$hobbies$hobby, "
            }
            groupHobbiesOutput.text = hobbies
            groupDescriptionOutput.text = group.description

            editAvatarImageView.setOnClickListener {
                onImageClick()
            }
            leaveGroupButton.setOnClickListener { showAcceptDialog() }
            closeButton.setOnClickListener { closeGroupDialog() }
        }
    }

    private fun onImageClick() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PICK_IMAGE)
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
        viewModel.showLoading.observe(viewLifecycleOwner, ::handleLoading)
    }

    private fun handleLoading(isLoading: Boolean) {
        LoadingDialogFragment.toggle(childFragmentManager, isLoading)
    }

    private fun handleEvents(event: GroupDialogViewEvent) {
        when (event) {
            GroupDialogViewEvent.ShowImageChangeSuccess -> onShowImageChangeSuccess()
            GroupDialogViewEvent.OpenMyGroups -> openMyGroups()
            is GroupDialogViewEvent.ShowError -> onError(event.exception)
        }
    }

    private fun onShowImageChangeSuccess() {
        binding.groupAvatarImage.load(previewBitmap)
        binding.showSnackbarInfo(getString(R.string.group_image_changed_with_success))
    }

    private fun openMyGroups() {
        findNavController().popBackStack()
    }

    private fun onError(exception: Exception) {
        binding.showSnackbarError(exception.message.toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            val imageUri = data?.data
            val bitmap =
                BitmapFactory.decodeStream(imageUri?.let { context?.contentResolver?.openInputStream(it) })
            val previewWidth = 400
            val previewHeight = bitmap.height * previewWidth / bitmap.width
            previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false)
            viewModel.changeGroupImage(bitmap, group.id)
        }
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