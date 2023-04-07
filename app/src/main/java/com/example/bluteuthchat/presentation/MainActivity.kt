package com.example.bluteuthchat.presentation

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bluteuthchat.presentation.discovery.DiscoveryBluetoothScreen
import com.example.bluteuthchat.presentation.discovery.DiscoveryBluetoothViewModel
import com.example.bluteuthchat.ui.theme.BluteuthchatTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val bluetoothManger by lazy {
        applicationContext.getSystemService(BluetoothManager::class.java)
    }
    private val bluetoothAdapter by lazy {
        bluetoothManger?.adapter
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bluetoothEnableLuncher=registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){}

        val permissionLauncher=registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ){perms->
            val canEnableBluetooth=if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                perms[Manifest.permission.BLUETOOTH_CONNECT]==true
            }else true
            if (canEnableBluetooth && !bluetoothAdapter?.isEnabled!!){
                bluetoothEnableLuncher.launch(
                    Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                )
            }

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN,
                )
            )
        }
        setContent {
            BluteuthchatTheme {
                val viewmodel:DiscoveryBluetoothViewModel= hiltViewModel()
                val state by viewmodel.state.collectAsState()


                DiscoveryBluetoothScreen(
                    state = state,
                    onStartScan = viewmodel::startScan,
                    onStopScan = viewmodel::startScan,
                    onDeviceClick = {}
                )
            }
        }
    }
}
