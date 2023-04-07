package com.example.bluteuthchat.presentation.discovery

import com.example.bluteuthchat.domain.chat.BluetoothDeviceDomain

data class DiscoveryBluetoothState(
    val pairedDevices:List<BluetoothDeviceDomain> = emptyList(),
    val scannedDevices:List<BluetoothDeviceDomain> = emptyList()
)
