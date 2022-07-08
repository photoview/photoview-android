package com.github.photoview.photoview_android

import android.content.SharedPreferences

class AppSharedPreferences(val sharedPreferences: SharedPreferences) {
    fun getInstanceUrl(): String? {
        return this.sharedPreferences.getString("instance_url", null)
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