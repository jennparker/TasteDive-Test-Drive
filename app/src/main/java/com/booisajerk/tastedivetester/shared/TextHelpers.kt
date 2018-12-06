package com.booisajerk.tastedivetester.shared

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan


object TextHelpers {
    fun capitalizeFirstLetter(text: String?): String? {
        return text?.capitalize()
    }

    /*
    * Trim leading and trailing new lines '\n'
    * Note: New lines within the text (e.g. paragraphs) are retained
    */
    fun trimNewLines(text: String): String {
        return text.trim('\n')
    }

    /*
    * Bold the name of the search item
    */
    private fun boldText(fullText: String?, boldText: String?): SpannableStringBuilder {
        val builder = SpannableStringBuilder()

        if (boldText?.trim(' ')?.isNotEmpty()!!) {
            val startingIndex = boldText.let { fullText?.indexOf(it) }
            val endingIndex = startingIndex?.plus(boldText.length)

            builder.append(fullText)
            if (startingIndex != null && endingIndex != null) {
                startingIndex.let { endingIndex.let { it1 -> builder.setSpan(StyleSpan(Typeface.BOLD), it, it1, 0) } }
            }
        } else {
            builder.append(fullText)
        }
        return builder
    }

    fun formattedResultTitleText(resultName: String?, responseString: Int, context: Context): SpannableStringBuilder {
        val formattedString: SpannableStringBuilder

        val searchResultString: String = String.format(
            context.getString(responseString)
            , resultName
        )
        formattedString = boldText(searchResultString, resultName)

        return formattedString
    }

    /*
    * Encode spaces and commas
    * */
    fun encodeQueryString(searchText: String): String {

        return searchText.replace(" ", "%20").replace(",", "%2C")
    }
}
