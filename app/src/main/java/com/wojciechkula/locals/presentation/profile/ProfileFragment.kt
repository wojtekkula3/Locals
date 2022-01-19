package com.wojciechkula.locals.presentation.profile

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.firebase.auth.FirebaseAuth
import com.theartofdev.edmodo.cropper.CropImage
import com.wojciechkula.locals.R
import com.wojciechkula.locals.common.dialog.LoadingDialogFragment
import com.wojciechkula.locals.databinding.FragmentProfileBinding
import com.wojciechkula.locals.extension.showSnackbarError
import com.wojciechkula.locals.extension.showSnackbarInfo
import com.wojciechkula.locals.navigation.ProfileNavigator
import com.wojciechkula.locals.presentation.common.SharedViewModel
import com.wojciechkula.locals.presentation.common.SharedViewState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    @Inject
    lateinit var navigator: ProfileNavigator

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCropActivityForResult()
        initViews()
        observeViewModel()
    }

    private fun setCropActivityForResult() {
        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract) { uri ->
            if (uri != null) {
                val bitmap = BitmapFactory.decodeStream(context?.contentResolver?.openInputStream(uri))
                viewModel.changeUserPicture(bitmap)
            }
        }
    }

    private fun initViews() {
        with(binding) {
            setCollapsingViews()
            logoutButton.setOnClickListener {
                val auth = FirebaseAuth.getInstance()
                auth.signOut()
                navigator.openLogin(findNavController())
            }
        }
    }

    private fun setCollapsingViews() {
        with(binding) {
            aboutMeArrowDown.setOnClickListener {
                it.visibility = View.GONE
                aboutMeArrowUp.visibility = View.VISIBLE
                aboutMeOutput.visibility = View.VISIBLE
                aboutMeSeparator.visibility = View.VISIBLE
                aboutMeButton.visibility = View.VISIBLE
            }

            aboutMeArrowUp.setOnClickListener {
                it.visibility = View.GONE
                aboutMeArrowDown.visibility = View.VISIBLE
                aboutMeOutput.visibility = View.GONE
                aboutMeSeparator.visibility = View.GONE
                aboutMeButton.visibility = View.GONE
            }

            myHobbiesArrowDown.setOnClickListener {
                it.visibility = View.GONE
                myHobbiesArrowUp.visibility = View.VISIBLE
                myHobbiesOutput.visibility = View.VISIBLE
                myHobbiesSeparator.visibility = View.VISIBLE
                myHobbiesButton.visibility = View.VISIBLE
            }

            myHobbiesArrowUp.setOnClickListener {
                it.visibility = View.GONE
                myHobbiesArrowDown.visibility = View.VISIBLE
                myHobbiesOutput.visibility = View.GONE
                myHobbiesSeparator.visibility = View.GONE
                myHobbiesButton.visibility = View.GONE
            }

            visibilityArrowDown.setOnClickListener {
                it.visibility = View.GONE
                visibilityArrowUp.visibility = View.VISIBLE
                publicVisibilityConstraint.visibility = View.VISIBLE
            }

            visibilityArrowUp.setOnClickListener {
                it.visibility = View.GONE
                visibilityArrowDown.visibility = View.VISIBLE
                publicVisibilityConstraint.visibility = View.GONE
            }

            avatarImage.setOnClickListener {
                cropActivityResultLauncher.launch(null)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.viewState.observe(viewLifecycleOwner, ::bindState)
        sharedViewModel.viewState.observe(viewLifecycleOwner, ::bindSharedState)
        viewModel.viewEvent.observe(viewLifecycleOwner, ::handleEvents)
        viewModel.showLoading.observe(viewLifecycleOwner, ::handleLoading)
    }

    private fun bindState(state: ProfileViewState) {
        with(state) {
            setViewsWithUserData(this)

            binding.emailVisibilityButton.setImageDrawable(setVisibility(emailVisibility))
            binding.phoneVisibilityButton.setImageDrawable(setVisibility(phoneNumberVisibility))
            binding.myHobbiesVisibilityButton.setImageDrawable(setVisibility(hobbiesVisibility))

            binding.emailVisibilityButton.setOnClickListener {
                viewModel.changeEmailVisibility(!emailVisibility)
            }
            binding.phoneVisibilityButton.setOnClickListener {
                viewModel.changePhoneVisibility(!phoneNumberVisibility)
            }
            binding.myHobbiesVisibilityButton.setOnClickListener {
                viewModel.changeHobbiesVisibility(!hobbiesVisibility)
            }
        }
    }

    private fun bindSharedState(state: SharedViewState) {
        viewModel.setUserInformation(state.user)
    }

    private fun handleEvents(event: ProfileViewEvent) {
        when (event) {
            is ProfileViewEvent.ShowImageChangeSuccess -> showImageChangeSuccess(event.uri)
            is ProfileViewEvent.ShowError -> onError(event.exception)
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        LoadingDialogFragment.toggle(childFragmentManager, isLoading)
    }

    private fun setViewsWithUserData(state: ProfileViewState) {
        if (state.user != null) {
            with(state.user) {

                if (!avatarReference.isNullOrEmpty()) {
                    binding.avatarImage.load(avatarReference)
                }

                binding.fullNameOutput.text = "$name $surname"
                binding.nameOutput.text = name
                binding.emailOutput.text = email

                if (surname.isNullOrEmpty()) {
                    binding.surnameOutput.text = "-"
                } else {
                    binding.surnameOutput.text = surname
                }

                if (phoneNumber.isNullOrEmpty()) {
                    binding.phoneOutput.text = "-"
                } else {
                    binding.phoneOutput.text = phoneNumber
                }

                if (about.isNullOrEmpty()) {
                    binding.aboutMeOutput.setTypeface(null, Typeface.ITALIC)
                    binding.aboutMeOutput.setTextColor(ContextCompat.getColor(context!!, R.color.gray_500))
                    binding.aboutMeOutput.text = getString(R.string.profile_empty)
                } else {
                    binding.aboutMeOutput.setTypeface(null, Typeface.NORMAL)
                    binding.aboutMeOutput.text = about
                }

                if (hobbies != null) {
                    var hobbiesString = ""
                    for (hobby in hobbies) {
                        hobbiesString += "• ${hobby},\n"
                    }
                    binding.myHobbiesOutput.text = hobbiesString
                }
            }
        }
    }

    private fun setVisibility(elementVisibility: Boolean) =
        if (elementVisibility) {
            ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_visibility, null)
        } else {
            ResourcesCompat.getDrawable(activity!!.resources, R.drawable.ic_visibility_off, null)
        }

    private fun showImageChangeSuccess(uri: Uri?) {
        binding.avatarImage.load(uri)
        binding.showSnackbarInfo(getString(R.string.profile_image_changed_with_success))
    }

    private fun onError(exception: Exception) {
        binding.showSnackbarError(exception.message.toString())
    }
}