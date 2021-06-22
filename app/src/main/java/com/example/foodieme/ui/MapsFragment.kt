package com.example.foodieme.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.foodieme.R
import com.example.foodieme.databinding.FragmentMapsBinding
import com.example.foodieme.network.map.DirectionResponses
import com.example.foodieme.network.map.MapApi
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class MapsFragment : Fragment() {

    private lateinit var map: GoogleMap
    private val REQUEST_LOCATION_PERMISSION = 1
    val homelatlng = LatLng(7.48727572549505, 4.533131903751711)
    lateinit var duration : String
    lateinit var durationString : String
    lateinit var distance: String

    private val callback = OnMapReadyCallback { googleMap ->

        map = googleMap
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        val zoomLevel = 13f

        googleMap.addMarker(MarkerOptions().position(homelatlng).title("Flozzy Cusine"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(homelatlng, zoomLevel))
        setPoiClick(googleMap)
        enableMyLocation(googleMap)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMapsBinding.inflate(inflater)

        return binding.root




    }

    private fun setPoiClick(map: GoogleMap){
        map.setOnPoiClickListener { poi ->
            val poiMarker = map.addMarker(
                MarkerOptions()
                    .position(poi.latLng)
                    .title(poi.name)
            )
            poiMarker.showInfoWindow()
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun enableMyLocation(map: GoogleMap){

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)  {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        else {
            map.setMyLocationEnabled(true)
            Log.e("tag", "katikati")



            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location == null) {
                    val request = LocationRequest().setPriority(LocationRequest.PRIORITY_LOW_POWER)
                    val callback = object: LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult?) {
                            val location = locationResult?.lastLocation ?:return

                            val latLng = LatLng(location.latitude, location.longitude)
                            val usersLocation = latLng.latitude.toString() + "," + latLng.longitude.toString()
                            val flozzyLocation = homelatlng.latitude.toString()+"," + homelatlng.longitude.toString()
                            map.addMarker(
                                MarkerOptions().
                                position(latLng).
                                title("Your Address")
                            )

                            apiResponse(usersLocation, flozzyLocation)


                        }

                    }
                    fusedLocationClient.requestLocationUpdates(request, callback, null)

                } else {
                    val latLng = LatLng(location.latitude, location.longitude)
                    val usersLocation = latLng.latitude.toString() + "," + latLng.longitude.toString()
                    val flozzyLocation = homelatlng.latitude.toString()+"," + homelatlng.longitude.toString()

                    map.addMarker(
                        MarkerOptions().
                        position(latLng).title("Your Address")
                    )
                    apiResponse(usersLocation, flozzyLocation)

                }
            }

        }

    }

    private fun apiResponse(usersLocation: String, flozzyLocation: String){
        MapApi.retrofitService.getDistance("AIzaSyDLyORFWLVIETjIiLItGg27xYX2ZeLIx6I", usersLocation, flozzyLocation)
            .enqueue(object : Callback<DirectionResponses> {
                override fun onResponse(call: Call<DirectionResponses>, response: Response<DirectionResponses>) {
                    drawPolyline(response)
                    getDuration(response)
                    Log.d("bisa dong oke", response.message())
                }

                override fun onFailure(call: Call<DirectionResponses>, t: Throwable) {
                    Log.e("anjir error", t.localizedMessage)
                }

            })
    }


    private fun drawPolyline(response: Response<DirectionResponses>) {
        val shape = response.body()?.routes?.get(0)?.overviewPolyline?.points
        val polyline = PolylineOptions()
            .addAll(PolyUtil.decode(shape))
            .width(8f)
            .color(Color.RED)
        map.addPolyline(polyline)
    }




    fun getDuration(response: Response<DirectionResponses>){
        distance = response.body()?.routes?.get(0)?.legs?.get(0)?.distance?.value.toString()
        duration = response.body()?.routes?.get(0)?.legs?.get(0)?.duration?.value.toString()
        durationString = response.body()?.routes?.get(0)?.legs?.get(0)?.duration?.text.toString()
        Log.e("duration", duration)
        Log.e("duration", durationString)
        Log.e("tag", distance)

    }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        if(requestCode == REQUEST_LOCATION_PERMISSION){
            if(grantResults.size >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                enableMyLocation(map)
            }
        }


    }


}