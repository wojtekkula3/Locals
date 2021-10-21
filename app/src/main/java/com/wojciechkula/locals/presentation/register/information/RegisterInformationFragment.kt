package com.wojciechkula.locals.presentation.register.information

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.wojciechkula.locals.databinding.FragmentRegisterInformationBinding
import com.wojciechkula.locals.navigation.RegisterInformationNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class RegisterInformationFragment : Fragment() {

    @Inject
    lateinit var navigator: RegisterInformationNavigator

    private var _binding: FragmentRegisterInformationBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterInformationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigator = RegisterInformationNavigator()
        initViews()
    }

    private fun initViews() {
        binding.nextButton.setOnClickListener { navigator.openRegisterData(findNavController()) }
    }
}