package com.example.foodieme.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodieme.database.timedurationdatabase.TimeAndDuration
import com.example.foodieme.database.timedurationdatabase.TimeDurationDao
import com.example.foodieme.domain.FlowsMenu
import com.example.foodieme.repository.MainMainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor( private val mainRepository: MainMainRepository,
                                               private val durationDao: TimeDurationDao,
                                               application: Application) : AndroidViewModel(application) {






    private var latestAddition = MutableLiveData<TimeAndDuration?>()
    private val _distance = MutableLiveData<String>()
    val distance: LiveData<String>
        get() = _distance

    private val _duration = MutableLiveData<Long>()
    val duration: LiveData<Long>
        get() = _duration




    init{
        viewModelScope.launch {
            mainRepository.refreshFlowsMenu()
        }

    }

    val popularFlowsMenu = mainRepository.returnPopularFlowMenu()


    fun onQueryChanged(filter: String?) : LiveData<List<FlowsMenu>>{
       return mainRepository.getFlowMenuByCategory(filter)
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

        if(durationDao.getLatestAddition() == null) {
            val newCheckOut = TimeAndDuration()
            insert(newCheckOut)
        }

        return durationDao.getLatestAddition()

    }

    fun setDurationAndDistance(duration: Long, distance: String) {
        _duration.value = duration
        _distance.value = distance
        initializeLatest(duration, distance)

    }



    private fun initializeLatest(duration: Long, distance: String) {

        viewModelScope.launch {

                //val newCheckOut = TimeAndDuration()
                //insert(newCheckOut)
                latestAddition.value = getLatestAdditonFromDatabase()
                onUpdateToCart(duration, distance)
        }
    }


    private suspend fun onUpdateToCart(duration: Long, distance: String) {

        withContext(Dispatchers.IO) {
            val time = latestAddition.value ?: return@withContext
            time.distance = distance
            time.duration = duration
            durationDao.update(time)

        }

    }

    private suspend fun update(night: TimeAndDuration) {
        withContext(Dispatchers.IO) {
            durationDao.update(night)
        }
    }

    private suspend fun insert(checkout: TimeAndDuration){
        withContext(Dispatchers.IO){
            durationDao.insert(checkout)
        }
    }




}