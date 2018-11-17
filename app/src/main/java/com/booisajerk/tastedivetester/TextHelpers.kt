package com.booisajerk.tastedivetester

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

    fun formattedResultTitleText(resultName: String?, resultType: String?, context: Context): SpannableStringBuilder {
        val formattedString: SpannableStringBuilder

        val searchResultString: String = String.format(
            context.getString(R.string.results_for_text)
            , resultName
            , capitalizeFirstLetter(resultType)
        )
        formattedString = boldText(searchResultString, resultName)

        return formattedString
    }

    /*
    * Replace the spaces names with multiple words with %20 so the request url will work
    * */
    fun replaceSpaces(searchText: String): String {

        return searchText.replace(" ", "%20")
    }
}
