package com.example.foodieme.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodieme.database.checkoutdatabase.CheckoutDatabase.Companion.getInstance
import com.example.foodieme.database.getDatabase
import com.example.foodieme.database.timedurationdatabase.TimeAndDuration
import com.example.foodieme.database.timedurationdatabase.TimeDurationDatabase.Companion.getDurationInstance
import com.example.foodieme.domain.FlowsMenu
import com.example.foodieme.repository.FlowsMenuRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeScreenViewModel(application: Application) : AndroidViewModel(application) {




    private var latestAddition = MutableLiveData<TimeAndDuration?>()

    private val database = getDatabase(application)
    private val database2 = getInstance(application)
    private val database3 = getDurationInstance(application)
    private val menuRepository = FlowsMenuRepository(database, database2)


    private val _distance = MutableLiveData<String>()
    val distance: LiveData<String>
        get() = _distance

    private val _duration = MutableLiveData<Long>()
    val duration: LiveData<Long>
        get() = _duration




    init{
        viewModelScope.launch {
            menuRepository.refreshFlowsMenu()
        }

    }

    val popularFlowsMenu = menuRepository.popularFlowsMenu






    fun onQueryChanged(filter: String?) : LiveData<List<FlowsMenu>>{
       return menuRepository.getFlowMenuByCategory(filter)
    }


    private val _navigateToDetailScreen = MutableLiveData<FlowsMenu?>()

    val navigateToDetailScreen : LiveData<FlowsMenu?>
        get() = _navigateToDetailScreen

    fun onDetailScreenClicked(flowsMenu: FlowsMenu) {
        _navigateToDetailScreen.value = flowsMenu
    }

    fun onDetailScreenNavigated() {
        _navigateToDetailScreen.value = null
    }




    private suspend fun getLatestAdditonFromDatabase(): TimeAndDuration? {
        return database3.durationDatabaseDao.getLatestAddition()

    }

    fun setDurationAndDistance(duration: Long, distance: String) {
        _duration.value = duration
        Log.e("view", _duration.value.toString())
        _distance.value = distance

        initializeLatest(duration, distance)
    }



    private fun initializeLatest(duration: Long, distance: String) {
        viewModelScope.launch {

                val newCheckOut = TimeAndDuration()
                insert(newCheckOut)
                latestAddition.value = getLatestAdditonFromDatabase()

                onUpdateToCart(duration, distance)
        }
    }


    private suspend fun onUpdateToCart(duration: Long, distance: String) {

        withContext(Dispatchers.IO) {
            val time = latestAddition.value ?: return@withContext
            time.distance = distance
            time.duration = duration
            database3.durationDatabaseDao.update(time)

        }

    }

    private suspend fun update(night: TimeAndDuration) {
        withContext(Dispatchers.IO) {
            database3.durationDatabaseDao.update(night)
        }
    }

    private suspend fun insert(checkout: TimeAndDuration){
        withContext(Dispatchers.IO){
            database3.durationDatabaseDao.insert(checkout)
        }
    }




}