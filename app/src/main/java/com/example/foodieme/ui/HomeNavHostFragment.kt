package com.example.foodieme.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.findNavController

import com.example.foodieme.R
import com.example.foodieme.databinding.FragmentHomeNavHostBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeNavHostFragment : Fragment() {

    private var navController: NavController? = null
    private val nestedNavHostFragmentId = R.id.nested_nav_host_fragment_home


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentHomeNavHostBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home_nav_host, container, false
        )


        /*
            🔥 This is navController we get from findNavController not the one required
            for navigating nested fragments
         */
        val mainNavController =
            Navigation.findNavController(requireActivity(), R.id.myNavHostFragment)


        val nestedNavHostFragment =
            childFragmentManager.findFragmentById(nestedNavHostFragmentId) as? NavHostFragment

        navController = nestedNavHostFragment?.navController

/*
        🔥 Alternative 1
        Navigate to HomeFragment1 if there is no current destination and current destination
        is start destination. Set start destination as this fragment so it needs to
        navigate next destination.

        If start destination is NavHostFragment it's required to navigate to first
 */
       if (navController!!.currentDestination == null || navController!!.currentDestination!!.id == navController!!.graph.startDestination) {
            navController?.navigate(R.id.detailscreenfragment)
           Log.e("null", "null")
       }
        else{
            Log.e("navC", navController!!.currentDestination?.id.toString())
            navController?.navigate(R.id.detailscreenfragment)
        }

        binding.lifecycleOwner = this

        return binding.root
    }
}


