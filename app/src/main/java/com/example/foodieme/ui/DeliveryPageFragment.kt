package com.example.foodieme.ui

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.foodieme.R
import com.example.foodieme.database.timedurationdatabase.TimeDurationDao
import com.example.foodieme.databinding.DeliveryPageFragmentBinding
import com.example.foodieme.network.map.DirectionResponses
import com.example.foodieme.network.map.MapApi
import com.example.foodieme.repository.MainMainRepository
import com.example.foodieme.viewmodels.DeliveryPageViewModel
import com.example.foodieme.viewmodels.DeliveryPageViewModelFactory
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class DeliveryPageFragment : Fragment() {

    @Inject
    lateinit var adapter: CartListAdapter
    @Inject  lateinit var mainRepository: MainMainRepository
    @Inject  lateinit var timeDurationDao: TimeDurationDao



    private val viewModel: DeliveryPageViewModel by lazy {
        val application = requireNotNull(this.activity).application
        val deliveryPageViewModelFactory =  DeliveryPageViewModelFactory(mainRepository, timeDurationDao, application)

        ViewModelProvider(this, deliveryPageViewModelFactory).get(DeliveryPageViewModel::class.java)
    }





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        val binding: DeliveryPageFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.delivery_page_fragment, container, false)




        //val application = requireNotNull(this.activity).application
        //val deliveryPageViewModelFactory =  DeliveryPageViewModelFactory(application)

        //val deliveryPageViewModel = ViewModelProvider(this, deliveryPageViewModelFactory).get(DeliveryPageViewModel::class.java)

        binding.deliveryPageViewModel = viewModel

        binding.recyclerView.adapter = adapter


        binding.lifecycleOwner = this


        viewModel.cartCheckoutMenu.observe(viewLifecycleOwner, Observer { cartItem ->
            cartItem.apply {
                adapter?.checkoutMenu = cartItem
            }

        })



        viewModel.navigateToAddToMapScreen.observe(viewLifecycleOwner, Observer {
            if(it==true){
                if (findNavController().currentDestination?.id == R.id.deliveryPageFragment) {
                    this.findNavController().navigate(DeliveryPageFragmentDirections.actionDeliveryPageFragmentToMapsFragment())
                    viewModel.onMapScreenNavigated()
                }
            }
        })



        return binding.root





    }









}