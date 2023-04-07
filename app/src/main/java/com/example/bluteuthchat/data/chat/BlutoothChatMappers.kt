package com.example.bluteuthchat.data.chat

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.example.bluteuthchat.domain.chat.BluetoothDeviceDomain

@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDeviceDomain():BluetoothDeviceDomain{
    return BluetoothDeviceDomain(
        name=this.name,
        address = this.address
    )
}