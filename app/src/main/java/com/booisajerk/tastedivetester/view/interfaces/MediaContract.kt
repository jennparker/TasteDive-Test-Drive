package com.booisajerk.tastedivetester.view.interfaces

import com.booisajerk.tastedivetester.model.Media

interface MediaContract {

    fun onMediaLoaded(media: List<Media>)

    fun onError(throwable: Throwable)

    fun isServerError()

    fun isNoResultsError()

    fun isNoSuchMediaError()

    fun showLoading()

    fun hideLoading()

    fun requestedTitle(title: String)
}