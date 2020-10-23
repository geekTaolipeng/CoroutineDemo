package com.hencoder.coroutinescamp.api.info

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

/**
 * Created by xiaojianjun on 2019-11-07.
 */
@Keep
@Parcelize
data class Tag(
    var articleId: Long = 0,
    var name: String = "",
    var url: String = ""
) : Parcelable