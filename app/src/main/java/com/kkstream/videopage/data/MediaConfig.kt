package com.kkstream.videopage.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaConfig(
    val url: String,
    val drmInfo: DrmInfo? = null
) : Parcelable
