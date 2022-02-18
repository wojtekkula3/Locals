package com.wojciechkula.locals.presentation.group.userdialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import coil.load
import com.wojciechkula.locals.databinding.LayoutUserDialogBinding
import com.wojciechkula.locals.extension.showSnackbarError
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UserDialogFragment(private val userId: String) : DialogFragment() {

    private var _binding: LayoutUserDialogBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: UserDialogViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = true
        _binding = LayoutUserDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (dialog != null) dialog!!.window!!.setLayout(580, 900)
        viewModel.getUser(userId)
        initViews()
        observeViewModel()
    }

    private fun initViews() {
        binding.closeButton.setOnClickListener {
            closeDialog()
        }
    }

    private fun closeDialog() {
        this.dismiss()
    }

    private fun observeViewModel() {
        viewModel.viewState.observe(viewLifecycleOwner, ::bindState)
        viewModel.viewEvent.observe(viewLifecycleOwner, ::handleEvents)
    }

    private fun bindState(state: UserDialogViewState) {
        with(state.user) {
            Timber.d("user: $this")

            if (!avatarReference.isNullOrEmpty()) {
                binding.avatarImageView.load(avatarReference)
            }
            binding.userNameOutput.text = name + " " + surname

            if (elementsVisibility.email || elementsVisibility.phoneNumber) {
                if (elementsVisibility.email) {
                    binding.emailOutput.text = email
                } else {
                    binding.emailLabel.visibility = View.GONE
                    binding.emailOutput.visibility = View.GONE
                }

                if (elementsVisibility.phoneNumber) {
                    if (!phoneNumber.isNullOrEmpty()) {
                        binding.phoneOutput.text = phoneNumber
                    } else {
                        binding.phoneLabel.visibility = View.GONE
                        binding.phoneOutput.visibility = View.GONE
                    }
                } else {
                    binding.phoneLabel.visibility = View.GONE
                    binding.phoneOutput.visibility = View.GONE
                }

                if (!elementsVisibility.email && phoneNumber.isNullOrEmpty()) {
                    binding.contactLayout.visibility = View.GONE
                }
            } else {
                binding.contactLayout.visibility = View.GONE
            }

            var hobbiesString = ""
            if (hobbies != null) {
                for (hobby in hobbies) {
                    hobbiesString += "$hobby, "
                }
            }
            if (elementsVisibility.hobbies) {
                binding.hobbiesOutput.text = hobbiesString
            } else {
                binding.hobbyLayout.visibility = View.GONE
            }

        }
    }

    private fun handleEvents(event: UserDialogViewEvent) {
        when (event) {
            is UserDialogViewEvent.ShowError -> onError(event.exception)
        }
    }

    private fun onError(exception: Exception) {
        binding.showSnackbarError(exception.message.toString())
    }

    companion object {
        private const val TAG = "user_dialog_fragment"

        fun show(fragmentManager: FragmentManager, userId: String): UserDialogFragment {
            val fragment = UserDialogFragment(userId)
            fragment.show(fragmentManager, TAG)
            return fragment
        }
    }
}