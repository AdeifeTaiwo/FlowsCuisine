package com.example.foodieme.ui

import android.Manifest
import android.annotation.TargetApi
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment

import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.example.foodieme.BuildConfig
import com.example.foodieme.MainActivityApplication
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
import com.example.foodieme.work.LocationWorker
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class HomeScreenFragment : Fragment() {
    private val LOCATION_PERMISSION_REQUEST = 1
    private val LOCATION_PERMISSION = "android.permission.ACCESS_FINE_LOCATION"
    private val COARSE_LOCATION_PERMISSION = "android.permission.ACCESS_COARSE_LOCATION"
    private val runningQOrLater =
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q
    private val homelatlng = LatLng(7.48727572549505, 4.533131903751711)
    lateinit var duration: String
    lateinit var durationString: String
    lateinit var distance: String

    @Inject
    lateinit var mainMainRepository: MainMainRepository

    @Inject
    lateinit var durationDao: TimeDurationDao

    private lateinit var binding2: HomeScreenBinding


    private var navController: NavController? = null
    private val nestedNavHostFragmentId = R.id.nested_nav_host_fragment_home

    private val viewModel: HomeScreenViewModel by lazy {
        val application = requireNotNull(this.activity).application
        val homeScreenViewModelFactory =
            HomeViewModelFactory(mainMainRepository, durationDao, application)

        ViewModelProvider(this, homeScreenViewModelFactory).get(HomeScreenViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: HomeScreenBinding = DataBindingUtil.inflate(
            inflater, R.layout.home_screen, container, false
        )

        binding2 = binding


        val homeScreenViewModel = viewModel

        binding.homeScreenViewModel = homeScreenViewModel


        navController = this.findNavController()

        binding.lifecycleOwner = this


        val popularAdapter = HomeScreenAdapter(FlowsMenuClickListener {
            homeScreenViewModel.onDetailScreenClicked(it)
        });


        val allItemAdapter = AllMenuListAdapter(MenuClickListener { flowsMenu ->
            homeScreenViewModel.onDetailScreenClicked(flowsMenu)
        });


        homeScreenViewModel.onQueryChanged("food").observe(viewLifecycleOwner, { allItem2 ->
                allItem2.apply {
                    allItemAdapter?.allItem = allItem2
                }
            })




        homeScreenViewModel.navigateToDetailScreen.observe(
            viewLifecycleOwner,
            { flowsMenu ->
                flowsMenu?.let {
                    if (navController?.currentDestination?.id != null) {
                        navController?.navigate(
                            HomeScreenFragmentDirections.actionHomescreenfragmentToDetailscreenfragment(
                                flowsMenu
                            )
                        )
                        homeScreenViewModel.onDetailScreenNavigated()
                    }


                }
            }
        )

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
                homeScreenViewModel.onQueryChanged(button.tag as String)
                    .observe(viewLifecycleOwner, Observer { allItems ->
                        allItems.apply {
                            allItemAdapter?.allItem = allItems
                        }

                    })
            }
            chip
        }

        chipGroup.removeAllViews()

        for (chip in children) {
            chipGroup.addView(chip)
        }


        binding.categoryAllMenuList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


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
        requestPermissions(
            arrayOf(LOCATION_PERMISSION, COARSE_LOCATION_PERMISSION),
            LOCATION_PERMISSION_REQUEST
        )
    }

    /**
     * Request the last location of this device, if known, otherwise start location updates.
     *
     * The last location is cached from the last application to request location.
     */


    private fun requestLastLocationOrStartLocationUpdates() {
        // if we don't have permission ask for it and wait until the user grants it
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                LOCATION_PERMISSION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            requestLocationPermission()
            return
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location == null) {
//              binding2.homeScreenConstraintLayout.visibility = View.GONE

                binding2.popularTextView.text = "Your Location Is Off"

                startLocationUpdates(fusedLocationClient)
            } else {
                binding2.popularTextView.text = "Popular"

                val latLng = LatLng(location.latitude, location.longitude)
                val usersLocation = latLng.latitude.toString() + "," + latLng.longitude.toString()
                val flozzyLocation =
                    homelatlng.latitude.toString() + "," + homelatlng.longitude.toString()

                apiResponse(usersLocation, flozzyLocation)


            }
        }
    }

    /**
     * Start location updates, this will ask the operating system to figure out the devices location.
     */
    private fun startLocationUpdates(fusedLocationClient: FusedLocationProviderClient) {
        // if we don't have permission ask for it and wait until the user grants it
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                LOCATION_PERMISSION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
            return
        }


        val request = LocationRequest()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(500)

        val callback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                val location = locationResult?.lastLocation ?: return

                val latLng = LatLng(location.latitude, location.longitude)
                val usersLocation = latLng.latitude.toString() + "," + latLng.longitude.toString()
                val flozzyLocation =
                    homelatlng.latitude.toString() + "," + homelatlng.longitude.toString()

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


    private fun apiResponse(usersLocation: String, flozzyLocation: String) {
        MapApi.retrofitService.getDistance(
            "AIzaSyDLyORFWLVIETjIiLItGg27xYX2ZeLIx6I",
            usersLocation,
            flozzyLocation
        )
            .enqueue(object : Callback<DirectionResponses> {
                override fun onResponse(
                    call: Call<DirectionResponses>,
                    response: Response<DirectionResponses>
                ) {
                    //drawPolyline(response)
                    getDuration(response)
                    Log.d("bisa dong oke", response.message())
                }

                override fun onFailure(call: Call<DirectionResponses>, t: Throwable) {
                    Log.e("anjir error", t.localizedMessage)
                }

            })
    }


    public fun getDuration(response: Response<DirectionResponses>) {
        distance = response.body()?.routes?.get(0)?.legs?.get(0)?.distance?.text.toString()
        duration = response.body()?.routes?.get(0)?.legs?.get(0)?.duration?.value.toString()
        durationString = response.body()?.routes?.get(0)?.legs?.get(0)?.duration?.text.toString()
        viewModel.setDurationAndDistance(duration.toLong(), distance)


    }


    companion object {
        private const val REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE = 33
        private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
        private const val REQUEST_TURN_DEVICE_LOCATION_ON = 29
        private const val TAG = "HuntMainActivity"
        private const val LOCATION_PERMISSION_INDEX = 0
        private const val BACKGROUND_LOCATION_PERMISSION_INDEX = 1
    }


    override fun onStart() {
        super.onStart()
        checkPermissionsAndStartGeofencing()

    }

    override fun onResume() {
        super.onResume()
        checkPermissionsAndStartGeofencing()

    }

    fun checkPermissionsAndStartGeofencing() {

        if (foregroundAndBackgroundLocationPermissionApproved()) {
            checkDeviceLocationSettingsAndStartGeoFence()
        } else {
            requestForegroundAndBackgroundLocationPermissions()
        }
    }

    @TargetApi(29)
    private fun foregroundAndBackgroundLocationPermissionApproved(): Boolean {
        val foregroundLocationApproved = (
                PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ))
        val backgroundPermissionApproved =
            if (runningQOrLater) {
                PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(
                            requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        )
            } else {
                true
            }
        return foregroundLocationApproved && backgroundPermissionApproved
    }


    private fun checkDeviceLocationSettingsAndStartGeoFence(resolve: Boolean = true) {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_LOW_POWER
            interval = 500
        }

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(requireActivity());
        val locationSettingsResonseTask = settingsClient.checkLocationSettings(builder.build())
        locationSettingsResonseTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException && resolve) {
                try {
                    exception.startResolutionForResult(
                        requireActivity(),
                        REQUEST_TURN_DEVICE_LOCATION_ON
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    Log.d("tag", "Error getting Location settings resolution: " + sendEx.message)
                }

            } else {
                Snackbar.make(
                    binding2.homeScreenConstraintLayout,
                    R.string.location_required_error, Snackbar.LENGTH_INDEFINITE
                ).setAction(android.R.string.ok) {
                    checkDeviceLocationSettingsAndStartGeoFence()
                }.show()
            }
            locationSettingsResonseTask.addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Location required successfully",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }


    @TargetApi(29)
    private fun requestForegroundAndBackgroundLocationPermissions() {
        if (foregroundAndBackgroundLocationPermissionApproved())
            return

        // Else request the permission
        // this provides the result[LOCATION_PERMISSION_INDEX]
        var permissionsArray = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

        val resultCode = when {
            runningQOrLater -> {
                // this provides the result[BACKGROUND_LOCATION_PERMISSION_INDEX]
                permissionsArray += Manifest.permission.ACCESS_BACKGROUND_LOCATION
                REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE
            }
            else -> REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
        }

        Log.d(TAG, "Request foreground only location permission")
        ActivityCompat.requestPermissions(
            requireActivity(),
            permissionsArray,
            resultCode
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


        if (grantResults.isEmpty() ||
            grantResults[LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_DENIED ||
            (requestCode == REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE &&
                    grantResults[BACKGROUND_LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_DENIED)

        ) {
            // Permission denied.
            Snackbar.make(
                binding2.homeScreenConstraintLayout,
                R.string.permission_denied_explanation,
                Snackbar.LENGTH_INDEFINITE
            ).setAction(R.string.settings) {
                // Displays App settings screen.
                startActivity(Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                })
            }.show()
        } else {
            checkDeviceLocationSettingsAndStartGeoFence()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TURN_DEVICE_LOCATION_ON)
            checkDeviceLocationSettingsAndStartGeoFence(false)

    }


}





