package com.booisajerk.tastedivetester.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseData(
    @Json(name = "Similar") val similar: Similar
)

@JsonClass(generateAdapter = true)
data class Similar(
    @Json(name = "Info") val info: List<Info>,
    @Json(name = "Results")  val results: List<Result>
)

@JsonClass(generateAdapter = true)
data class Info(
    @Json(name= "Name") val name: String,
    @Json(name = "Type") val type: String,
    val wTeaser: String,
    val wUrl: String,
    val yID: String,
    val yUrl: String
)

@JsonClass(generateAdapter = true)
data class Result(
    @Json(name= "Name") val name: String,
    @Json(name = "Type") val type: String,
    val wTeaser: String,
    val wUrl: String,
    val yID: String,
    val yUrl: String
)