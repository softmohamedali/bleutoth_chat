package com.example.bluteuthchat.data.chat

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class FoundedBluetoothDeviceReceiver(
    private val onDeviceFounded:(BluetoothDevice)->Unit
):BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            BluetoothDevice.ACTION_FOUND -> {
                val device=if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableArrayExtra(
                        BluetoothDevice.EXTRA_DEVICE,
                        BluetoothDevice::class.java
                    )
                }else{
                    intent.getParcelableArrayExtra(
                        BluetoothDevice.EXTRA_DEVICE,
                    )
                }
                device?.let { onDeviceFounded }
            }
        }

    }
}