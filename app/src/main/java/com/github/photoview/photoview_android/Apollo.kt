package com.github.photoview.photoview_android

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.squareup.picasso.OkHttp3Downloader
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response


private var instance: ApolloClient? = null

fun apolloClient(context: Context, graphqlEndpoint: String? = null): ApolloClient? {
    if (instance != null) {
        return instance!!
    }

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthorizationInterceptor(context))
        .build()

    val url = graphqlEndpoint ?: context.appSharedPreferences().getGraphqlEndpoint()
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

        val token = context.appSharedPreferences().getToken()
        if (token != null) {
            request.addHeader("Cookie", "auth-token=${token}")
        }

        return chain.proceed(request.build())
    }
}

fun protectedMediaDownloader(context: Context): OkHttp3Downloader {
    val token = context.appSharedPreferences().getToken()

    val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(Interceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                .addHeader("Cookie", "auth-token=${token}")
                .build()
            chain.proceed(newRequest)
        })
        .build()

    return OkHttp3Downloader(client)
}
