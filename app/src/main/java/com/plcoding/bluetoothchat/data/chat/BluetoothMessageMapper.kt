package com.plcoding.bluetoothchat.data.chat

import com.plcoding.bluetoothchat.domain.chat.BluetoothMessage

// if this class were to get more complex, itd probably be smart to serialize w/ json
// but thats kind of overkill here, these messages are small
// also, this will break if the user sends a hashtag in their message
// in a real app youd want something more robust (json) but this is just meant to be a learning project
// so its fine for now :)

fun String.toBluetoothMessage(isFromLocalUser: Boolean): BluetoothMessage {
    val name = substringBeforeLast("#")
    val message = substringAfter("#")
    return BluetoothMessage(
        message = message,
        senderName = name,
        isFromLocalUser = isFromLocalUser
    )
}

fun BluetoothMessage.toByteArray(): ByteArray {
    return "$senderName#$message".encodeToByteArray()
}