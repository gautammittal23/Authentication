package com.Authentication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt


class MainActivity : AppCompatActivity(), AuthenticationManager.BiometricCallback {
    val authManager by lazy {
        AuthenticationManager.instance
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        authManager.setAuthCallback(this)

        if (authManager.fingerprintScannerExist(this)) {
            authManager.initBiometricVerification(
                this,
                authManager.createBiometricPromptInfo()
            )
        } else {
            authManager.startActivityForKeyGuard(this, authManager.createKeyguardIntent(this))
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        authManager.onActivityResult(requestCode, resultCode, data)
    }


    override fun onBioAuthenticationFailed() {
        runOnUiThread {
            showToast("onBioAuthenticationFailed")
        }
        finish()
        System.exit(0)

    }

    override fun onBioAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        runOnUiThread {
            showToast("onBioAuthenticationSucceeded")
        }
        finish()
        startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
    }

    override fun onBioAuthenticationError(errorCode: Int, errString: CharSequence) {
        runOnUiThread {
            showToast("onBioAuthenticationError")
            finish()
            System.exit(0)
        }

    }

    override fun onKeyGuardSuccess() {
        showToast("onKeyGuardSuccess")
        finish()
        startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
    }

    override fun onKeyGuardFailure() {
        showToast("onKeyGuardFailure")
        finish()
        System.exit(0)
    }


}
