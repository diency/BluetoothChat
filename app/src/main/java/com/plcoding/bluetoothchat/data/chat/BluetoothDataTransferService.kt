package com.plcoding.bluetoothchat.data.chat

import android.bluetooth.BluetoothSocket
import com.plcoding.bluetoothchat.domain.chat.BluetoothMessage
import com.plcoding.bluetoothchat.domain.chat.TransferFailedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class BluetoothDataTransferService(private val socket: BluetoothSocket) {


    fun listenForOncomingMessages(): Flow<BluetoothMessage> {
        return flow{
            if(!socket.isConnected){
                return@flow
            }
            val buffer = ByteArray(1024) //1kb for messages
            //keep connection alive to listen for incoming data
            while(true){
                val bytecount = try{
                    socket.inputStream.read(buffer)
                }catch(e: Exception){
                    throw TransferFailedException()
                }
                //if we reach here, we successfully read a message
                emit(buffer.decodeToString(
                    endIndex = bytecount
                ).toBluetoothMessage(false))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun sendMessage(bytes: ByteArray): Boolean {
        return withContext(Dispatchers.IO){
            try{
                socket.outputStream.write(bytes)
            }catch(e: Exception){
                e.printStackTrace()
                return@withContext false
            }

            //assume success
            true
        }
    }
}