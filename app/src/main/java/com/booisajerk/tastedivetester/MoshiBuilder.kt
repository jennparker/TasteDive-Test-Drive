package com.booisajerk.tastedivetester

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object MoshiBuilder {
    val moshiInstance: Moshi
        get() = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
}
