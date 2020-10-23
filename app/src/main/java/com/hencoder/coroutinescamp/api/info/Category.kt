package com.hencoder.coroutinescamp.api.info

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

/**
 * Created by xiaojianjun on 2019-11-12.
 */
@Keep
@Parcelize
data class Category(
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int,
    val children: MutableList<Category>
) : Parcelable