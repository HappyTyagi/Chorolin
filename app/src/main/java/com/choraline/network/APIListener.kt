package com.choraline.network

import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

/**
 * Created by deepak Tyagi on 7/10/2017.
 */
interface APIListener {

    fun onApiSuccess(obj: Any, api: Int)
    fun onApiFailure(throwable: Throwable,api: Int)
}