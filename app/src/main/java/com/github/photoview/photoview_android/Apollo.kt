package com.github.photoview.photoview_android

import android.content.Context
import android.os.Looper
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.github.photoview.photoview_android.MainActivity
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

private var instance: ApolloClient? = null

fun apolloClient(context: Context, instanceUrl: String?): ApolloClient? {
    if (instance != null) {
        return instance!!
    }

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthorizationInterceptor(context))
        .build()

    val url = instanceUrl ?: context.appSharedPreferences().getInstanceUrl()
    return if (url != null) {
        ApolloClient.Builder()
            .serverUrl(url)
//            .webSocketServerUrl("wss://apollo-fullstack-tutorial.herokuapp.com/graphql")
            .okHttpClient(okHttpClient)
            .build()
    } else {
        return null
    }
}

private class AuthorizationInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

//        request.addHeader("Authorization", User.getToken(context) ?: "")

        return chain.proceed(request.build())
    }
}
