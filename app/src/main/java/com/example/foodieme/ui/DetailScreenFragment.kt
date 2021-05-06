package com.example.foodieme.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.foodieme.R
import com.example.foodieme.databinding.DetailscreenBinding
import com.example.foodieme.domain.FlowsMenu
import com.example.foodieme.viewmodels.DetailScreenViewModel
import com.example.foodieme.viewmodels.DetailScreenViewModelFactory

class DetailScreenFragment  : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: DetailscreenBinding = DataBindingUtil.inflate(
            inflater, R.layout.detailscreen, container, false

        )

        val arguments = DetailScreenFragmentArgs.fromBundle(requireArguments()).flowsMenu


        val detailScreenViewModelFactory = DetailScreenViewModelFactory(arguments)

        val detailScreenViewModel = ViewModelProvider(this, detailScreenViewModelFactory).get(DetailScreenViewModel::class.java)

        binding.detailScreenViewModel  = detailScreenViewModel




        binding.lifecycleOwner = this



        return binding.root
    }

}
