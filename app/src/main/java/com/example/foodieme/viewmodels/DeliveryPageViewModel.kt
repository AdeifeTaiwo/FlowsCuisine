package com.example.foodieme.viewmodels

import android.app.Application
import android.content.Context
import android.os.CountDownTimer
import android.os.SystemClock
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.*
import com.example.foodieme.database.checkoutdatabase.CheckoutDatabase
import com.example.foodieme.database.getDatabase
import com.example.foodieme.database.timedurationdatabase.TimeDurationDatabase.Companion.getDurationInstance
import com.example.foodieme.repository.FlowsMenuRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeliveryPageViewModel(application: Application) : AndroidViewModel(application) {

    private var newTime: Long? = 0L
    private val database = getDatabase(application)
    private val database2 = CheckoutDatabase.getInstance(application)
    private val menuRepository = FlowsMenuRepository(database, database2)

    var cartCheckoutMenu = menuRepository.activeOrder

    private var prefs =
        application.getSharedPreferences(
            "com.example.android.eggtimernotifications",
            Context.MODE_PRIVATE
        )
    private val REQUEST_CODE = 0
    private val TRIGGER_TIME = "TRIGGER_AT"
    private val TRIGGER_TIME2 = "TRIGGER_AT2"

    private val minute: Long = 60_000L
    private val second: Long = 1_000L
    private lateinit var timer: CountDownTimer


    private val database3 = getDurationInstance(application)
    private val _elapsedTime = MutableLiveData<Long>()
    val elapsedTime: LiveData<Long>
        get() = _elapsedTime

    private var _alarmOn = MutableLiveData<Boolean>(false)
    val isAlarmOn: LiveData<Boolean>
        get() = _alarmOn


    private suspend fun startTimer() {
        _alarmOn.value?.let {
            if (!it) {
                _alarmOn.value = true

                withContext(Dispatchers.IO) {
                    val triggerTime =
                        SystemClock.elapsedRealtime() + database3.durationDatabaseDao.retrieveWithId(1).duration!! * second
                    saveTime(triggerTime)
                }
                createTimer()
            }

        }


        Log.e("timer", "timerStarted")
    }


    init {



            viewModelScope.launch {
                startTimer()
            }



    }


    private fun createTimer() {

        Log.e("timer", "timerStarted")

        viewModelScope.launch {
            val triggerTime = loadTime()
            Log.e("time", loadTime().toString())

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
        viewModelScope.launch {
            timer.cancel()
            _elapsedTime.value = 0
            _alarmOn.value = false
        }
    }


    private suspend fun saveTime(triggerTime: Long) =
        withContext(Dispatchers.IO) {
            prefs.edit().putLong(TRIGGER_TIME, triggerTime).apply()
        }

    private suspend fun loadTime(): Long =
        withContext(Dispatchers.IO) {
            prefs.getLong(TRIGGER_TIME, 0)
        }


    private val _navigateToAddToMapScreen = MutableLiveData<Boolean?>()

    val navigateToAddToMapScreen: LiveData<Boolean?>
        get() = _navigateToAddToMapScreen

    fun onMapScreenClicked() {
        _navigateToAddToMapScreen.value = true
    }

    fun onMapScreenNavigated() {
        _navigateToAddToMapScreen.value = null
    }


}