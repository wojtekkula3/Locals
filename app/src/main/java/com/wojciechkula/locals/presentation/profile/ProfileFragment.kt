package com.wojciechkula.locals.presentation.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.firebase.auth.FirebaseAuth
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

    private val PICK_IMAGE = 100

    @Inject
    lateinit var navigator: ProfileNavigator

    private val viewModel: ProfileViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeViewModel()
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
                val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery, PICK_IMAGE)
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

            if (state.user?.avatarReference != null) {
                binding.avatarImage.load(state.user.avatarReference)
            }

        }
    }

    private fun bindSharedState(state: SharedViewState) {
        viewModel.setUserInformation(state.user)
    }

    private fun handleEvents(event: ProfileViewEvent) {
        when (event) {
            is ProfileViewEvent.ShowImageChangeSuccess -> showImageChangeSuccess(event.uri)
            is ProfileViewEvent.ShowError -> showError(event.message)
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        LoadingDialogFragment.toggle(childFragmentManager, isLoading)
    }

    private fun setViewsWithUserData(state: ProfileViewState) {
        if (state.user != null) {
            with(state.user) {
                binding.fullNameOutput.text = "$name $surname"
                binding.nameOutput.text = name
                binding.surnameOutput.text = surname
                binding.emailOutput.text = email
                binding.avatarImage.setImageBitmap(avatar)

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
                        hobbiesString += "• ${hobby.name},\n"
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

//    private fun encodeImage(bitmap: Bitmap): String {
//        val previewWidth = 150
//        val previewHeight = bitmap.height * previewWidth / bitmap.width
//        val previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false)
//        val byteArrayOutputStream = ByteArrayOutputStream()
//        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
//        val bytes = byteArrayOutputStream.toByteArray()
//        return Base64.encodeToString(bytes, Base64.DEFAULT)
//    }

    private fun showImageChangeSuccess(uri: Uri?) {
        binding.avatarImage.load(uri)
        binding.showSnackbarInfo(getString(R.string.profile_image_changed_with_success))
    }

    private fun showError(message: String) {
        binding.showSnackbarError(message)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            val imageUri = data?.data
            val bitmap = BitmapFactory.decodeStream(imageUri?.let { context?.contentResolver?.openInputStream(it) })
            viewModel.changeUserPicture(bitmap)
        }
    }
}