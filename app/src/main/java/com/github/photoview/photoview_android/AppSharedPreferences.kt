package com.github.photoview.photoview_android

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

fun Context.appSharedPreferences() = AppSharedPreferences(
    this.getSharedPreferences(packageName + "_preferences", AppCompatActivity.MODE_PRIVATE))

class AppSharedPreferences(val sharedPreferences: SharedPreferences) {
    fun getInstanceUrl(): String? {
        return this.sharedPreferences.getString("instance_url", null)
    }

    fun getGraphqlEndpoint(): String? {
        val instanceUrl = getInstanceUrl()
        return if (instanceUrl != null) {
            "${instanceUrl.trimEnd('/')}/api/graphql"
        } else {
            null
        }
    }

    fun getToken(): String? {
        return this.sharedPreferences.getString("token", null)
    }

    fun saveAuthorization(token: String, instanceUrl: String) {
        this.sharedPreferences.edit()
            .putString("instance_url", instanceUrl)
            .putString("token", token)
            .apply()
    }
}