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
import com.example.foodieme.R
import com.example.foodieme.databinding.DeliveryPageFragmentBinding
import com.example.foodieme.network.map.DirectionResponses
import com.example.foodieme.network.map.MapApi
import com.example.foodieme.viewmodels.DeliveryPageViewModel
import com.example.foodieme.viewmodels.DeliveryPageViewModelFactory
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeliveryPageFragment : Fragment() {

    private val LOCATION_PERMISSION_REQUEST = 1

    private val LOCATION_PERMISSION = "android.permission.ACCESS_FINE_LOCATION"

    val homelatlng = LatLng(7.5180632199244055, 4.52874358169682)
    lateinit var duration : String
   lateinit var durationString : String
    lateinit var distance: String

    //private val application = requireNotNull(this.activity).application
    private val viewModel: DeliveryPageViewModel by lazy {
        val application = requireNotNull(this.activity).application
        val deliveryPageViewModelFactory =  DeliveryPageViewModelFactory(application)

        ViewModelProvider(this, deliveryPageViewModelFactory).get(DeliveryPageViewModel::class.java)
    }





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        val binding: DeliveryPageFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.delivery_page_fragment, container, false)




        val application = requireNotNull(this.activity).application
        val deliveryPageViewModelFactory =  DeliveryPageViewModelFactory(application)

        val deliveryPageViewModel = ViewModelProvider(this, deliveryPageViewModelFactory).get(DeliveryPageViewModel::class.java)

        binding.deliveryPageViewModel = viewModel

        val adapter = CartListAdapter()
        binding.recyclerView.adapter = adapter


        binding.lifecycleOwner = this

        viewModel.cartCheckoutMenu.observe(viewLifecycleOwner, Observer { cartItem ->
            cartItem.apply {
                adapter?.checkoutMenu = cartItem
            }
        })




        return binding.root



    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestLastLocationOrStartLocationUpdates()

    }

    /**
     * Show the user a dialog asking for permission to use location.
     */
    private fun requestLocationPermission() {
        requestPermissions(arrayOf(LOCATION_PERMISSION), LOCATION_PERMISSION_REQUEST)
    }

    /**
     * Request the last location of this device, if known, otherwise start location updates.
     *
     * The last location is cached from the last application to request location.
     */
    private fun requestLastLocationOrStartLocationUpdates() {
        // if we don't have permission ask for it and wait until the user grants it
        if (ContextCompat.checkSelfPermission(requireContext(), LOCATION_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission()
            return
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location == null) {
                startLocationUpdates(fusedLocationClient)
            } else {

                val latLng = LatLng(location.latitude, location.longitude)
                val usersLocation = latLng.latitude.toString() + "," + latLng.longitude.toString()
                val flozzyLocation = homelatlng.latitude.toString()+"," + homelatlng.longitude.toString()

                apiResponse(usersLocation, flozzyLocation)



            }
        }
    }

    /**
     * Start location updates, this will ask the operating system to figure out the devices location.
     */
    private fun startLocationUpdates(fusedLocationClient: FusedLocationProviderClient) {
        // if we don't have permission ask for it and wait until the user grants it
        if (ContextCompat.checkSelfPermission(requireContext(), LOCATION_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission()
            return
        }


        val request = LocationRequest().setPriority(LocationRequest.PRIORITY_LOW_POWER)
        val callback = object: LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                val location = locationResult?.lastLocation ?: return
                //viewModel.onLocationUpdated(location)
                viewModel.createTimer()
                val latLng = LatLng(location.latitude, location.longitude)
                val usersLocation = latLng.latitude.toString() + "," + latLng.longitude.toString()
                val flozzyLocation = homelatlng.latitude.toString()+"," + homelatlng.longitude.toString()

                apiResponse(usersLocation, flozzyLocation)

            }
        }
        fusedLocationClient.requestLocationUpdates(request, callback, null)
    }

    /**
     * This will be called by Android when the user responds to the permission request.
     *
     * If granted, continue with the operation that the user gave us permission to do.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            LOCATION_PERMISSION_REQUEST -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLastLocationOrStartLocationUpdates()
                }
            }
        }
    }

    private fun apiResponse(usersLocation: String, flozzyLocation: String){
        MapApi.retrofitService.getDistance("AIzaSyDLyORFWLVIETjIiLItGg27xYX2ZeLIx6I", usersLocation, flozzyLocation)
            .enqueue(object : Callback<DirectionResponses>{
                override fun onResponse(call: Call<DirectionResponses>, response: Response<DirectionResponses>) {
                    //drawPolyline(response)

                    getDuration(response)

                    Log.d("bisa dong oke", response.message())
                }

                override fun onFailure(call: Call<DirectionResponses>, t: Throwable) {
                    Log.e("anjir error", t.localizedMessage)
                }

            })
    }

    fun getDuration(response: Response<DirectionResponses>){
        distance = response.body()?.routes?.get(0)?.legs?.get(0)?.distance?.text.toString()
        duration = response.body()?.routes?.get(0)?.legs?.get(0)?.duration?.value.toString()
        durationString = response.body()?.routes?.get(0)?.legs?.get(0)?.duration?.text.toString()
        viewModel.setDistance(distance)

        viewModel.update(duration.toLong())







    }





}