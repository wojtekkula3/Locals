package com.wojciechkula.locals.presentation.group.groupdialog

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.theartofdev.edmodo.cropper.CropImage
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

    private val cropActivityResultContract = object : ActivityResultContract<Any?, Uri?>() {
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity()
                .setAspectRatio(1, 1)
                .getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uri
        }
    }
    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Any?>
    private var previewBitmap: Bitmap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = true

        _binding = LayoutGroupDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (dialog != null) dialog!!.window!!.setLayout(580, 1000)
        setCropActivityForResult()
        initViews()
        observeViewModel()
    }

    private fun setCropActivityForResult() {
        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract) { uri ->
            if (uri != null) {
                val bitmap = BitmapFactory.decodeStream(context?.contentResolver?.openInputStream(uri))
                val previewWidth = 400
                val previewHeight = bitmap.height * previewWidth / bitmap.width
                previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false)
                viewModel.changeGroupImage(bitmap, group.id)
            }
        }
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

            avatarCardView.setOnClickListener {
                cropActivityResultLauncher.launch(null)
            }
            editAvatarImageView.setOnClickListener {
                cropActivityResultLauncher.launch(null)
            }
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

    companion object {
        private const val TAG = "group_dialog_fragment"

        fun show(fragmentManager: FragmentManager, group: GroupModel): GroupDialogFragment {
            val fragment = GroupDialogFragment(group)
            fragment.show(fragmentManager, TAG)
            return fragment
        }
    }
}