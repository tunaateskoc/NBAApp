package com.rocknhoney.nbaapp.util

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.widget.TextView

object Util {
    const val HEADER_HOST = "x-rapidapi-host"
    const val API_HOST = "free-nba.p.rapidapi.com"
    const val HEADER_KEY = "x-rapidapi-key"
    const val API_KEY = "" // your api key
    const val API_URL = "https://free-nba.p.rapidapi.com/"
    const val NO_NETWORK = "No Internet. Please pull down to refresh"
    const val NO_DATA = "No Data. Please pull down to refresh"

    fun setSpannableString(firstString: String, secondString: String, textView: TextView){
        val str = SpannableString(firstString + secondString)
        str.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            firstString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textView.text = str
    }
}