package com.ljmu.andre.snaptools.viewmodel

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ljmu.andre.snaptools.EventBus.Events.PackEventRequest
import com.ljmu.andre.snaptools.Framework.MetaData.LocalPackMetaData
import com.ljmu.andre.snaptools.Utils.Result
import com.ljmu.andre.snaptools.repository.PackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber


/**
 * This file was created by Jacques Hoffmann (jaqxues) in the Project SnapTools.<br>
 * Date: 13.05.20 - Time 10:40.
 */
class PackViewModel : ViewModel() {
    val localMetadata: LiveData<List<LocalPackMetaData>>
        get() = packRepo.localMetadata

    fun refreshLocalPacks(eventHandler: PackEventRequest.EventHandler) {
        viewModelScope.launch(Dispatchers.IO) {
            packRepo.refreshLocalMetadata(eventHandler)
        }
    }

    fun setTutorialPacks() {
        packRepo.setTutorialPacks()
    }

    fun clearPacks() {
        packRepo.clear()
    }

    fun getInfoFromName(packName: String): Pair<LocalPackMetaData, Int>? {
        val packs = localMetadata.value
        return packs?.find { it.name == packName }?.let {
            it to packs.indexOf(it)
        }
    }

    fun enablePack(packName: String, activity: Activity): LiveData<Result<String>> {
        val liveData = MutableLiveData<Result<String>>()
        viewModelScope.launch(Dispatchers.Default) {
            liveData.postValue(
                    try {
                        packRepo.enablePack(packName, activity)
                        Result.Success(packName)
                    } catch (e: Exception) {
                        Timber.e(e, "Could not load Pack")
                        Result.Error(e)
                    })
        }
        return liveData
    }

    fun unloadPack(packName: String, activity: Activity) = packRepo.unloadPack(packName, activity)

    fun deletePack(packName: String, activity: Activity, evtHandler: PackEventRequest.EventHandler) = packRepo.deletePack(packName, activity, evtHandler)

    fun downloadPack(activity: Activity, metadata: ServerPackMetaData) {
        viewModelScope.launch(Dispatchers.IO) {
            packRepo.downloadPack(activity, metadata)
        }
    }

    companion object {
        private val packRepo by lazy { PackRepository() }
    }
}