package com.github.photoview.photoview_android

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.photoview.photoview_android.databinding.ActivityWelcomeBinding
import java.lang.Exception

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        binding.connectButton.setOnClickListener {
            var fieldsOk = true

            var instanceUrl = binding.instanceField.editText!!.text.toString().trim()
            val username = binding.usernameField.editText!!.text.toString().trim()
            val password = binding.passwordField.editText!!.text.toString()

            if (instanceUrl.isEmpty()) {
                binding.instanceField.error = "Please enter the URL of your instance."
                fieldsOk = false
            }

            if (username.isEmpty()) {
                binding.usernameField.error = "Please enter your username."
                fieldsOk = false
            }

            if (password.isEmpty()) {
                binding.passwordField.error = "Please enter your password."
                fieldsOk = false
            }

            instanceUrl = "${instanceUrl.trim().trimEnd('/')}/api/graphql"

            if (!fieldsOk) return@setOnClickListener

            lifecycleScope.launchWhenResumed {
                val result = try {
                    apolloClient(baseContext, instanceUrl)!!.mutation(AuthorizeMutation(username, password)).execute()
                } catch (e: Exception) {
                    binding.errorMessage.text = "Connection error: ${e.message}"
                    binding.errorMessage.visibility = View.VISIBLE
                    return@launchWhenResumed
                }

                if (result.errors != null) {
                    val firstErr = result.errors!!.first().message
                    binding.errorMessage.text = "Error: $firstErr"
                    binding.errorMessage.visibility = View.VISIBLE
                    return@launchWhenResumed
                }

                val authUser = result.data!!.authorizeUser

                if (!authUser.success) {
                    binding.errorMessage.text = "Authorization failed: ${authUser.status}"
                    binding.errorMessage.visibility = View.VISIBLE
                    return@launchWhenResumed
                }

                val token = authUser.token
                if (token == null) {
                    Log.w("WelcomeActivity", "Token from server was null")
                    return@launchWhenResumed
                }

                baseContext.appSharedPreferences().saveAuthorization(token, instanceUrl)
                finish()
            }

        }
    }
}