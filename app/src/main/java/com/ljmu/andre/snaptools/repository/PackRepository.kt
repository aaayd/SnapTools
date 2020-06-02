package com.ljmu.andre.snaptools.repository

import android.app.Activity
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jaqxues.akrolyb.prefs.Preference
import com.jaqxues.akrolyb.prefs.getPref
import com.ljmu.andre.snaptools.EventBus.Events.PackDeleteEvent
import com.ljmu.andre.snaptools.EventBus.Events.PackDownloadEvent
import com.ljmu.andre.snaptools.EventBus.Events.PackDownloadEvent.DownloadState
import com.ljmu.andre.snaptools.EventBus.Events.PackEventRequest
import com.ljmu.andre.snaptools.EventBus.Events.PackUnloadEvent
import com.ljmu.andre.snaptools.Framework.FrameworkManager
import com.ljmu.andre.snaptools.Framework.MetaData.FailedPackMetaData
import com.ljmu.andre.snaptools.Framework.MetaData.LocalPackMetaData
import com.ljmu.andre.snaptools.Framework.Utils.PackLoadState
<<<<<<< HEAD
import com.ljmu.andre.snaptools.Networking.Helpers.DownloadModulePack
import com.ljmu.andre.snaptools.Networking.Helpers.GetPackChangelog
import com.ljmu.andre.snaptools.Networking.Helpers.GetServerPacks
import com.ljmu.andre.snaptools.Networking.Packets.PackDataPacket
import com.ljmu.andre.snaptools.Networking.WebResponse
import com.ljmu.andre.snaptools.Utils.*
=======
>>>>>>> parent of 472732d... PackDownloaderFragment.java to PackDownloaderFragment.kt
import com.ljmu.andre.snaptools.Utils.FrameworkPreferencesDef.SELECTED_PACKS
import com.ljmu.andre.snaptools.Utils.PackUtils.getPackMetaData
import com.ljmu.andre.snaptools.Utils.PathProvider
import com.ljmu.andre.snaptools.Utils.PreferenceHelpers
import com.ljmu.andre.snaptools.Utils.Result
import timber.log.Timber
import java.io.File
import java.util.HashSet

/**
 * This file was created by Jacques Hoffmann (jaqxues) in the Project SnapTools.<br>
 * Date: 13.05.20 - Time 10:01.
 */
class PackRepository {
    // Private Mutable LiveData
    private val _eventDispatcher = MutableLiveData<Any>()
    private val _localMetadata = MutableLiveData<List<LocalPackMetaData>>()
<<<<<<< HEAD
    private val _remoteMetadata = MutableLiveData<Request<List<ServerPackMetaData>>>()

=======
>>>>>>> parent of 472732d... PackDownloaderFragment.java to PackDownloaderFragment.kt
    // Exposed public LiveData
    val eventDispatcher: LiveData<Any> = _eventDispatcher
    val localMetadata: LiveData<List<LocalPackMetaData>> = _localMetadata

    private val packDirectory = File(PathProvider.getModulesPath())

    fun refreshLocalMetadata(evtHandler: PackEventRequest.EventHandler) {
        val jarFileList = packDirectory.listFiles { _, name -> name.endsWith(".jar") }
        if (jarFileList.isNullOrEmpty()) {
            _localMetadata.postValue(emptyList())
            return
        }

        val packs = jarFileList.associate { file ->
            val metadata = try {
                getPackMetaData(file, evtHandler)
            } catch (t: Throwable) {
                FailedPackMetaData(evtHandler).run {
                    reason = t.message
                    name = file.name.replace(".jar", "")
                    completedBinding()
                }
            }
            metadata.name to metadata
        }

        val packsList = packs.values.toMutableList()

        // Selected Packs that have been deleted or cannot be found for some reason
        SELECTED_PACKS.getPref().forEach { packName ->
            if (packName in packs) return@forEach

            val failedMetadata = FailedPackMetaData(evtHandler).run {
                reason = "Pack is enabled but cannot be found in the installed list"
                name = packName
                completedBinding()
            }
            packsList.add(failedMetadata)
        }

        _localMetadata.postValue(packsList.sorted())
    }

    fun unloadPack(packName: String, activity: Activity): Result<String> {
        val modPack = FrameworkManager.getModulePack(packName)
        if (modPack == null) {
            val e = IllegalStateException("Pack was not loaded. Could not unload it")
            Timber.e(e)
            return Result.Error(e)
        }
        if (FrameworkManager.unloadModPack(packName)) {
            _eventDispatcher.value = PackUnloadEvent(modPack.packMetaData)
            return Result.Success(packName)
        }
        return Result.Error(Exception("Unknown Exception while trying to unload the Pack $packName"))
    }

    fun disablePack(packName: String, activity: Activity): Result<String> {
        val result = unloadPack(packName, activity)
        if (result is Result.Error)
            return result
        FrameworkManager.disableModPack(packName)
        return Result.Success(packName)
    }

    /**
     * Unloads, disables and deletes the pack
     */
    fun deletePack(packName: String, activity: Activity, evtHandler: PackEventRequest.EventHandler): Result<String> {
        val result = disablePack(packName, activity)
        if (result is Result.Error)
            return result
        FrameworkManager.deleteModPack(packName, activity)

        if (PreferenceHelpers.collectionContains(SELECTED_PACKS, packName)) {
            val e = IllegalStateException("Failed to delete Pack $packName")
            Timber.e(e)
            return Result.Error(e)
        }
        _eventDispatcher.value = PackDeleteEvent(packName)
        refreshLocalMetadata(evtHandler)
        return Result.Success(packName)
    }

    /**
     * Download Pack and update LiveData
     */
    fun downloadPack(activity: Activity, metaData: ServerPackMetaData) {
        val download = metaData.run {
            DownloadModulePack(
                    activity,
                    name, scVersion, type, isDeveloper, packVersion, flavour
            )
        }
        download.download { state, message, outputFile, responseCode ->
            _eventDispatcher.postValue(
                    PackDownloadEvent()
                            .setState(if (state) DownloadState.SUCCESS else DownloadState.FAIL)
                            .setMessage(message)
                            .setMetaData(metaData)
                            .setOutputFile(outputFile)
                            .setResponseCode(responseCode)
            )
        }
    }

    fun setTutorialPacks() {
        _localMetadata.value =
                listOf("10.0.0.0", "10.1.0.1", "10.12.1.0", "10.16.0.0").map {
                    LocalPackMetaData.getTutorialPack(it)
                }
    }

    fun clear() {
        _localMetadata.value = emptyList()
    }

    @WorkerThread
    fun enablePack(packName: String, activity: Activity) {
        PreferenceHelpers.addToCollection(SELECTED_PACKS, packName, activity)
        val packLoadEvent = FrameworkManager.loadModPack(activity, packName, PackLoadState(packName))
        _eventDispatcher.postValue(packLoadEvent)
    }
<<<<<<< HEAD

    @WorkerThread
    fun getChangelog(activity: Activity, pack: ServerPackMetaData, liveData: MutableLiveData<Request<PackDataPacket>>) {
        liveData.postValue(Request.Pending)
        GetPackChangelog.performCheck(
                activity,
                pack.type,
                pack.scVersion,
                pack.flavour,
                object : WebResponse.PacketResultListener<PackDataPacket> {
                    override fun success(message: String, packet: PackDataPacket) {
                        Timber.d(message)
                        liveData.postValue(Request.Loaded(Result.Success(packet)))
                    }

                    override fun error(message: String, t: Throwable?, errorCode: Int) {
                        Timber.e(t, message)
                        liveData.postValue(Request.Loaded(Result.Error(PacketResultException(
                                "Issue Getting Changelog",
                                message, errorCode
                        ))))
                    }
                }
        )
    }
=======
>>>>>>> parent of 472732d... PackDownloaderFragment.java to PackDownloaderFragment.kt
}