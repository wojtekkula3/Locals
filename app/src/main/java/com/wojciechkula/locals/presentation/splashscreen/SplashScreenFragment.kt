package com.wojciechkula.locals.presentation.splashscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.wojciechkula.locals.databinding.FragmentSplashScreenBinding
import com.wojciechkula.locals.navigation.SplashScreenNavigator

internal class SplashScreenFragment : Fragment() {

    lateinit var navigator: SplashScreenNavigator

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigator = SplashScreenNavigator()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener{ navigator.openLogin(findNavController())}
    }

}