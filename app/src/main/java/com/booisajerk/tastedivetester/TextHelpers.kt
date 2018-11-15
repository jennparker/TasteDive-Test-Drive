package com.booisajerk.tastedivetester

object TextHelpers {
     fun capitalizeFirstLetter(line: String): String {
        return Character.toUpperCase(line[0]) + line.substring(1)
    }

    // Trim leading and trailing new lines '\n'
    // Note: New lines within the text (e.g. paragraphs) are retained
     fun trimNewLines(text: String): String {
        return text.trim('\n')
    }
}