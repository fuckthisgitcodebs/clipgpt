package com.ultrasalt.edgeclipplus.util

object ClipClassifier {

    fun classify(text: String): String {
        val t = text.trim()

        return when {
            t.startsWith("http://") || t.startsWith("https://") -> "URL"
            t.matches(Regex("^[0-9]{4,8}$")) -> "OTP"
            t.matches(Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) -> "EMAIL"
            t.matches(Regex("^\\+?[0-9\\-\\s()]{7,}$")) -> "PHONE"
            else -> "TEXT"
        }
    }
}
