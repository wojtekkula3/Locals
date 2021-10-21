package com.wojciechkula.locals.presentation.register.hobbies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wojciechkula.locals.databinding.FragmentRegisterHobbiesBinding
import com.wojciechkula.locals.navigation.RegisterHobbiesNavigator

internal class RegisterHobbiesFragment : Fragment() {

    lateinit var navigator: RegisterHobbiesNavigator

    private var _binding: FragmentRegisterHobbiesBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterHobbiesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}