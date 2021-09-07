package com.example.foodieme.work

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.tasks.Tasks
import java.util.concurrent.ExecutionException

class LocationWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {


    companion object {
        const val WORK_NAME = "LocationWorker"
    }

    override suspend fun doWork(): Result {

        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_LOW_POWER
            interval = 500
        }

        val locationClient =
            LocationServices.getFusedLocationProviderClient(applicationContext)

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("failed", "failed")
            return Result.failure()
        }

        try {
            val result = locationClient.lastLocation
            Tasks.await(result) ?: return Result.retry()
            Log.e("retry", "retry")


        } catch (bug: ExecutionException) {
            bug.printStackTrace()
            return Result.retry()
        }
        Log.e("succeeded", "success")
        Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()
        return Result.success()
    }




}