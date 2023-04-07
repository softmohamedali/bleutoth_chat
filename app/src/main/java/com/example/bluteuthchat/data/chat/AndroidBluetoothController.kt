package com.example.bluteuthchat.data.chat

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.example.bluteuthchat.domain.chat.BluetoothController
import com.example.bluteuthchat.domain.chat.BluetoothDeviceDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@SuppressLint("MissingPermission")
class AndroidBluetoothController(
    private val context: Context
):BluetoothController {

    private val bluetoothManger by lazy {
        context.getSystemService(BluetoothManager::class.java)
    }
    private val bluetoothAdapter by lazy {
        bluetoothManger?.adapter
    }

    private val _scannedDevices= MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
    override val scannedDevices: StateFlow<List<BluetoothDeviceDomain>>
        get() = _scannedDevices.asStateFlow()

    private val _pairedDevices= MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
    override val pairedDevices: StateFlow<List<BluetoothDeviceDomain>>
        get() = _pairedDevices.asStateFlow()

    private val foundedBluetoothDeviceReceiver=FoundedBluetoothDeviceReceiver{device->
        _scannedDevices.update {
            val foundedDevice=device.toBluetoothDeviceDomain()
            if (foundedDevice in _pairedDevices.value) it else it+foundedDevice
        }

    }
    init {
        updatePairedDevices()
    }

    override fun startDiscovery() {
        if (!hasPermission(Manifest.permission.BLUETOOTH_SCAN)){return}
        context.registerReceiver(
            foundedBluetoothDeviceReceiver,
            IntentFilter(BluetoothDevice.ACTION_FOUND)
        )
        updatePairedDevices()
        bluetoothAdapter?.startDiscovery()
    }

    override fun stopDiscovery() {
        if (!hasPermission(Manifest.permission.BLUETOOTH_SCAN)){return}
        bluetoothAdapter?.cancelDiscovery()
    }

    override fun release() {
        context.unregisterReceiver(
            foundedBluetoothDeviceReceiver,
        )
    }



    private fun updatePairedDevices(){
        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
            return
        }
        bluetoothAdapter?.bondedDevices
            ?.map { it.toBluetoothDeviceDomain() }
            ?.also { _pairedDevices.value=it }
    }
    private fun hasPermission(permission:String):Boolean{
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }
}