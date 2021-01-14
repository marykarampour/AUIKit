package com.prometheussoftware.auikit.utility.kotlin

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GSONUtility {
    companion object {
        inline fun <reified T> fromJsonInline(json: String): T {
            val type = object : TypeToken<T>() {}.type
            return Gson().fromJson(json, type)
        }

        fun fromJson(json: String): Any {
            return fromJsonInline(json)
        }
    }
}