package com.booisajerk.tastedivetester

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan


object TextHelpers {
    fun capitalizeFirstLetter(text: String): String {
        return Character.toUpperCase(text[0]) + text.substring(1)
    }

    /*
    * Trim leading and trailing new lines '\n'
    * Note: New lines within the text (e.g. paragraphs) are retained
    */
    fun trimNewLines(text: String): String {
        return text.trim('\n')
    }

    /*
    * Bold the name of the search it
    */
    private fun boldText(fullText: String, boldText: String): SpannableStringBuilder {
        val builder = SpannableStringBuilder()

        if (boldText.trim(' ').isNotEmpty()) {
            val startingIndex = fullText.indexOf(boldText)
            val endingIndex = startingIndex + boldText.length

            builder.append(fullText)
            if (startingIndex >= 0 && endingIndex >= 0) {
                builder.setSpan(StyleSpan(Typeface.BOLD), startingIndex, endingIndex, 0)
            }
        } else {
            builder.append(fullText)
        }
        return builder
    }

    fun formattedResultTitleText(resultName: String, resultType: String, context: Context): SpannableStringBuilder {
        val formattedString: SpannableStringBuilder

        val searchMovieString: String = String.format(
            context.getString(R.string.results_for_text)
            , resultName
            , capitalizeFirstLetter(resultType)
        )
        formattedString = boldText(searchMovieString, resultName)

        return formattedString
    }
}
