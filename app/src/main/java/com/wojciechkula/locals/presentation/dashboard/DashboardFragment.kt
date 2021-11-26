package com.wojciechkula.locals.presentation.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.wojciechkula.locals.R
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
        initBottomNavigation()
    }

    private fun initBottomNavigation() {
        with(binding.bottomNavigationView) {
            setupWithNavController(getDashboardNestedNavController())
        }
    }

    private fun getDashboardNestedNavController(): NavController {
        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
        return navHostFragment.navController
    }
}