package com.example.bluteuthchat.presentation.discovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bluteuthchat.domain.chat.BluetoothController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class DiscoveryBluetoothViewModel @Inject constructor(
    private val bluetoothController: BluetoothController
) :ViewModel(){

    private val _state = MutableStateFlow(DiscoveryBluetoothState())
    val state = combine(
        bluetoothController.scannedDevices,
        bluetoothController.pairedDevices,
        _state
    ){ scanned,paired,myState->
        myState.copy(
            pairedDevices = paired,
            scannedDevices = scanned
        )

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)


    fun startScan() {
        bluetoothController.startDiscovery()
    }

    fun stopScan() {
        bluetoothController.stopDiscovery()
    }


}