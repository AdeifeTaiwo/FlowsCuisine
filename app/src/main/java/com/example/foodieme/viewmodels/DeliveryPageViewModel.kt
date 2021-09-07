package com.example.foodieme.viewmodels

import android.app.Application
import android.content.Context
import android.os.CountDownTimer
import android.os.SystemClock
import androidx.lifecycle.*
import com.example.foodieme.database.checkoutdatabase.CheckoutDatabaseDao
import com.example.foodieme.database.timedurationdatabase.TimeDurationDao

import com.example.foodieme.repository.MainMainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DeliveryPageViewModel @Inject constructor(
    private val mainRepository: MainMainRepository,
    private val timeDurationDao: TimeDurationDao,
    private val checkoutDatabaseDao: CheckoutDatabaseDao,
    application: Application
) : AndroidViewModel(application) {


    var cartCheckoutMenu = mainRepository.returnActiveOrdersInCheckOutMenu()
    private var getCartCheckoutMenu = Transformations.map(cartCheckoutMenu){
        it.isNotEmpty()
    }


    private var prefs =
        application.getSharedPreferences(
            "com.example.android.eggtimernotifications",
            Context.MODE_PRIVATE
        )


    private val TRIGGER_TIME = "TRIGGER_AT"
    private val minute: Long = 60_000L
    private val second: Long = 1_000L
    private lateinit var timer: CountDownTimer


    private val _elapsedTime = MutableLiveData<Long>()
    val elapsedTime: LiveData<Long>
        get() = _elapsedTime

    private var _alarmOn = MutableLiveData<Boolean?>()
    val isAlarmOn: LiveData<Boolean?>
        get() = _alarmOn


    private suspend fun startTimer() {


            if (returnAlarmStatus() == false) {
                updateAlarm(true)

                withContext(Dispatchers.IO) {
                    val triggerTime =
                        SystemClock.elapsedRealtime() + timeDurationDao.retrieveWithId(1).duration!! * second
                    saveTime(triggerTime)
                }

            }


        createTimer()

    }

    private suspend fun returnAlarmStatus(): Boolean? {

        if (timeDurationDao.getLatestAddition() != null) {
            return withContext(Dispatchers.IO) {
                timeDurationDao.retrieveWithId(1).isAlarmOn
            }
        }
        return true

    }

    private suspend fun updateAlarm(setValue: Boolean) {
        withContext(Dispatchers.IO) {
            if (timeDurationDao.getLatestAddition() != null) {
                val updateAlarm = timeDurationDao.retrieveWithId(1)
                updateAlarm.isAlarmOn = setValue
                timeDurationDao.update(updateAlarm)
            }
        }

    }

    private suspend fun clearCheckOutMenuWhenDone(){
        withContext(Dispatchers.IO){
           checkoutDatabaseDao.clear()
        }
    }


    init {

        getCartCheckoutMenu.observeForever {
            if (it) {
                viewModelScope.launch {
                    startTimer()
                }
            }
        }


    }


    private fun createTimer() {

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
        }
    }

    private fun resetTimer() {
        viewModelScope.launch {
            timer.cancel()
            _elapsedTime.value = 0
            updateAlarm(false)
            clearCheckOutMenuWhenDone()
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