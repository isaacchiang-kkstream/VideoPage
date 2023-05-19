package com.kkstream.videopage.extension

import android.net.Uri

private fun HashMap<String, String>.toKeyRequestProperties(): String? {
    return if (this.isNotEmpty()) {
        this.map {
            val encodedKey = Uri.encode(it.key)
            val encodedValue = Uri.encode(it.value)
            "$encodedKey=$encodedValue"
        }.reduce { acc, s -> "$s&$acc" }
    } else {
        null
    }
}