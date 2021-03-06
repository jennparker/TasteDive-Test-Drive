package com.booisajerk.tastedivetester.shared

class Constants {
    companion object {
        const val BASE_URL: String = "https://tastedive.com/"
        const val API:String = "api/similar?"
        const val QUERY_KEY:String = "q="
        const val TASTE_DIVE_API_KEY: String = "&k=INSERT KEY HERE"
        // info 0 (default) returns only media name and media type
        // info 1 returns name, type, description, Wikipedia link, YouTube link, YouTube ID
        const val INFO_LEVEL: String = "&info=1"

        const val INTENT_KEY = "key"
    }
}