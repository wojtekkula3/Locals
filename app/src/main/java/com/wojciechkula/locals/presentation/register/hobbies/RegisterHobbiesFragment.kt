package com.wojciechkula.locals.presentation.register.hobbies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.wojciechkula.locals.databinding.FragmentRegisterHobbiesBinding
import com.wojciechkula.locals.navigation.RegisterHobbiesNavigator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
internal class RegisterHobbiesFragment : Fragment() {

    @Inject
    lateinit var navigator: RegisterHobbiesNavigator

    private var _binding: FragmentRegisterHobbiesBinding? = null
    private val binding
        get() = _binding!!

    private val args: RegisterHobbiesFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterHobbiesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("This is my args: ${args.toString()}")
    }

}