package com.example.foodieme.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodieme.R
import com.example.foodieme.databinding.HomeScreenBinding
import com.example.foodieme.ui.allmenulistadapter.AllMenuListAdapter
import com.example.foodieme.ui.allmenulistadapter.MenuClickListener
import com.example.foodieme.viewmodels.HomeScreenViewModel
import com.example.foodieme.viewmodels.HomeViewModelFactory
import com.google.android.material.chip.Chip

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


        NavigationUI.setupWithNavController(binding.bottomNav, findNavController())

        val popularAdapter = HomeScreenAdapter(FlowsMenuClickListener {
            homeScreenViewModel.onDetailScreenClicked(it)

        });

        val allItemAdapter = AllMenuListAdapter(MenuClickListener {flowsMenu ->

            homeScreenViewModel.onDetailScreenClicked(flowsMenu)

        });

        homeScreenViewModel.onQueryChanged("food").observe(viewLifecycleOwner, Observer {
            allItem2 -> allItem2.apply {
                allItemAdapter?.allItem = allItem2
        }
        })




        homeScreenViewModel.navigateToDetailScreen.observe(viewLifecycleOwner,  Observer { flowsMenu ->
            flowsMenu?.let {


                if (findNavController().currentDestination?.id == R.id.homescreenfragment) {
                    this.findNavController().navigate(HomeScreenFragmentDirections.actionHomescreenfragmentToDetailscreenfragment(flowsMenu))
                    homeScreenViewModel.onDetailScreenNavigated()
                }



            }
        })

        val types = listOf("stew", "food", "drinks", "cake", "milk", "", "swallow")

        val chipGroup = binding.regionList
        val inflator = LayoutInflater.from(chipGroup.context)


        binding.categoryAllMenuList.adapter = allItemAdapter

            //initial value for all menu adapter

        val children = types.map { eachType ->
            val chip = inflator.inflate(R.layout.region, chipGroup, false) as Chip
            chip.text = eachType
            chip.tag = eachType
            chip.setOnCheckedChangeListener { button, isChecked ->
                homeScreenViewModel.onQueryChanged(button.tag as String).observe(viewLifecycleOwner, Observer {
                    allItems -> allItems.apply {
                        allItemAdapter?.allItem = allItems
                }

                }) }
            chip
        }

        chipGroup.removeAllViews()

        for(chip in children){
            chipGroup.addView(chip)
        }


        binding.categoryAllMenuList.layoutManager = LinearLayoutManager( context, LinearLayoutManager.HORIZONTAL, false)


        binding.popularMenuList.adapter = popularAdapter
        //binding.popularMenuList.smoothScrollToPosition(5)

            homeScreenViewModel.popularFlowsMenu.observe(viewLifecycleOwner, Observer { popular ->
            popular.apply {
                popularAdapter?.popular = popular
            }
        })
        return binding.root



    }




}
