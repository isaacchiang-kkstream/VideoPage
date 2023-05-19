package com.kkstream.videopage.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DrmInfo(
    val drmServer: String,
    val headers: HashMap<String, String>? = null
) : Parcelable
