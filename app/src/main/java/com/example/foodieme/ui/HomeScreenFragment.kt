package com.example.foodieme.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodieme.R
import com.example.foodieme.database.timedurationdatabase.TimeDurationDao
import com.example.foodieme.databinding.HomeScreenBinding
import com.example.foodieme.network.map.DirectionResponses
import com.example.foodieme.network.map.MapApi
import com.example.foodieme.repository.MainMainRepository
import com.example.foodieme.ui.allmenulistadapter.AllMenuListAdapter
import com.example.foodieme.ui.allmenulistadapter.MenuClickListener
import com.example.foodieme.viewmodels.HomeScreenViewModel
import com.example.foodieme.viewmodels.HomeViewModelFactory
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class HomeScreenFragment : Fragment(){
    private val LOCATION_PERMISSION_REQUEST = 1

    private val LOCATION_PERMISSION = "android.permission.ACCESS_FINE_LOCATION"

    val homelatlng = LatLng(7.48727572549505, 4.533131903751711)
    lateinit var duration : String
    lateinit var durationString : String
    lateinit var distance: String

    @Inject lateinit var mainMainRepository: MainMainRepository
    @Inject lateinit var durationDao: TimeDurationDao


    private val viewModel: HomeScreenViewModel by lazy {
        val application = requireNotNull(this.activity).application
        val homeScreenViewModelFactory =  HomeViewModelFactory(mainMainRepository, durationDao, application)

        ViewModelProvider(this, homeScreenViewModelFactory).get(HomeScreenViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: HomeScreenBinding = DataBindingUtil.inflate(
            inflater, R.layout.home_screen, container, false)

        val application = requireNotNull(this.activity).application

        val homeScreenViewModelFactory = HomeViewModelFactory(mainMainRepository,durationDao, application)
        var homeScreenViewModel = ViewModelProvider(this, homeScreenViewModelFactory).get(HomeScreenViewModel::class.java)

        homeScreenViewModel = viewModel
        binding.homeScreenViewModel = homeScreenViewModel

        binding.lifecycleOwner = this
        //binding.bottomNav.isVisible = true
        NavigationUI.setupWithNavController(binding.bottomNav, findNavController())
        binding.bottomNav.bottom



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
            .enqueue(object : Callback<DirectionResponses> {
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
        viewModel.setDurationAndDistance(duration.toLong(), distance)
        Log.e("duration", duration)


    }









}
