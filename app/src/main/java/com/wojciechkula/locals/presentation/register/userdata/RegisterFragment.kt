package com.wojciechkula.locals.presentation.register.userdata

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wojciechkula.locals.databinding.FragmentRegisterBinding
import com.wojciechkula.locals.navigation.RegisterNavigator

internal class RegisterFragment : Fragment() {

    lateinit var navigator: RegisterNavigator

    private var _binding: FragmentRegisterBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}