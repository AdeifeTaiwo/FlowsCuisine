package com.example.foodieme.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.foodieme.R
import com.example.foodieme.databinding.HomeScreenBinding
import com.example.foodieme.viewmodels.HomeScreenViewModel
import com.example.foodieme.viewmodels.HomeViewModelFactory

class HomeScreenFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: HomeScreenBinding = DataBindingUtil.inflate(
            inflater, R.layout.home_screen, container, false)

        val application = requireNotNull(this.activity).application

        val homeScreenViewModelFactory = HomeViewModelFactory(application)

        val homeScreenViewModel = ViewModelProvider(this, homeScreenViewModelFactory).get(HomeScreenViewModel::class.java)

        binding.homeScreenViewModel = homeScreenViewModel

        binding.lifecycleOwner = this

        val popularAdapter = HomeScreenAdapter(FlowsMenuClickListener {

        });

        binding.popularMenuList.adapter = popularAdapter

        homeScreenViewModel.flowsMenu.observe(viewLifecycleOwner, Observer { popular ->
            popular.apply {
                popularAdapter?.popular = popular
            }
        })
        return binding.root



    }



}
