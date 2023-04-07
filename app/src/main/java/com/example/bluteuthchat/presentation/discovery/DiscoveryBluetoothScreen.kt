package com.example.bluteuthchat.presentation.discovery

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.bluteuthchat.domain.chat.BluetoothDeviceDomain


@Composable
fun DiscoveryBluetoothScreen(
    state: DiscoveryBluetoothState,
    onStartScan:()->Unit,
    onStopScan:()->Unit,
    onDeviceClick:(BluetoothDeviceDomain)->Unit
) {

    Column (
        modifier = Modifier.fillMaxSize()
    ){
        LazyColumn(modifier = Modifier.weight(4f)){
            item { 
                Text(text = "paired devices", fontSize = 20.sp)
            }
            items(state.pairedDevices){
                Text(
                    modifier = Modifier.fillMaxWidth()
                        .clickable { onDeviceClick(it) },
                    text =it.name?:"(no Name)"
                )
            }
        }

        LazyColumn(modifier = Modifier.weight(4f)){
            item {
                Text(text = "available devices", fontSize = 20.sp)
            }
            items(state.scannedDevices){
                Text(
                    modifier = Modifier.fillMaxWidth()
                        .clickable { onDeviceClick(it) },
                    text =it.name?:"(no Name)"
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().weight(2f),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = onStartScan) {
                Text(text = "start scan")
            }
            Button(onClick = onStopScan) {
                Text(text = "stop scan")
            }
            Button(onClick = onStartScan) {
                Text(text = "clear")
            }
        }
    }

}