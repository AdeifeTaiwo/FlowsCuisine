package com.example.foodieme.viewmodels

import android.app.Application
import android.content.Context
import android.os.CountDownTimer
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.*
import com.example.foodieme.database.checkoutdatabase.Checkout
import com.example.foodieme.database.checkoutdatabase.CheckoutDatabase
import com.example.foodieme.database.getDatabase
import com.example.foodieme.domain.CheckoutMenu
import com.example.foodieme.domain.FlowsMenu
import com.example.foodieme.repository.FlowsMenuRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates

class DeliveryPageViewModel(application: Application) : AndroidViewModel(application) {

     var startTime : Boolean? = true
    private val database = getDatabase(application)
    private val database2 = CheckoutDatabase.getInstance(application)
    private val menuRepository = FlowsMenuRepository(database, database2)
    var cartCheckoutMenu = menuRepository.checkoutMenu

    private var prefs =
        application.getSharedPreferences("com.example.android.eggtimernotifications", Context.MODE_PRIVATE)
    private val REQUEST_CODE = 0
    private val TRIGGER_TIME = "TRIGGER_AT"

    private val minute: Long = 60_000L
    private val second: Long =1_000L
    private lateinit var timer: CountDownTimer




    private val _distance = MutableLiveData<String>()
    val distance: LiveData<String>
    get() = _distance



    private val _elapsedTime = MutableLiveData<Long>()
    val elapsedTime: LiveData<Long>
        get() = _elapsedTime




    fun setDistance(distance: String) {
        _distance.value = distance


    }
    private val _duration = MutableLiveData<Long>()
    val duration: LiveData<Long>
        get() = _duration
    fun setDuration(duration: Long) {
        _duration.value = duration



    }






    fun update(duration: Long){
        viewModelScope.launch {
            updateDuration(duration)
        }
    }

    private  suspend fun updateDuration(duration: Long){
        withContext(Dispatchers.IO){
            val allincart = database2.checkoutDatabaseDao.getAllNightsNonLivedata()
            allincart.let { it ->
                it.forEach {
                    withContext(Dispatchers.IO) {
                        //updatetheduration(it.checkOutId)
                        val toUpdateDuration = database2.checkoutDatabaseDao.getNightWithId(it.checkOutId)

                        toUpdateDuration.duration = duration
                        database2.checkoutDatabaseDao.update(toUpdateDuration)

                    }
                }

            }

        }
    }





     fun createTimer(){
        viewModelScope.launch {
            val triggerTime =
                SystemClock.elapsedRealtime() + database2.checkoutDatabaseDao.getLatestAddition()!!.duration * second
            saveTime(triggerTime)
            startTimer()

        }


    }



init {
    startTimer()
}

    fun startTimer() {

            viewModelScope.launch {
                val triggerTime = loadTime()

                timer = object : CountDownTimer(triggerTime, second) {
                    override fun onTick(millisUntilFinished: Long) {
                        _elapsedTime.value = triggerTime - SystemClock.elapsedRealtime()
                        if (_elapsedTime.value!! <= 0) {
                            resetTimer()
                        }
                    }

                    override fun onFinish() {
                        resetTimer()
                    }
                }
                timer.start()

                Log.e("triggertime", triggerTime.toString())
            }




    }

    private fun resetTimer() {
        timer.cancel()
        _elapsedTime.value = 0
    }


    private suspend fun saveTime(triggerTime: Long) =
        withContext(Dispatchers.IO) {
            prefs.edit().putLong(TRIGGER_TIME, triggerTime).apply()
        }

    private suspend fun loadTime(): Long =
        withContext(Dispatchers.IO) {
            prefs.getLong(TRIGGER_TIME, 0)
        }













}