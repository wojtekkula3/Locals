package com.wojciechkula.locals.presentation.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.wojciechkula.locals.databinding.FragmentDashboardBinding
import com.wojciechkula.locals.navigation.DashboardNavigator

internal class DashboardFragment : Fragment() {

    lateinit var navigator: DashboardNavigator

    private var _binding: FragmentDashboardBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.logoutButton.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            auth.signOut()
        }
    }
}