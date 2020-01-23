package com.Authentication

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricFragment
import androidx.biometric.BiometricPrompt
import java.security.AccessController.getContext


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




    fun pinpassword()
    {
        val builder =
            android.hardware.biometrics.BiometricPrompt.Builder(baseContext)
        builder.setTitle("")
            .setSubtitle("").setDeviceCredentialAllowed(true)

        // Set builder flags introduced in Q.
        // Set builder flags introduced in Q.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            builder.setConfirmationRequired(
                mBundle.getBoolean(
                    BiometricPrompt.KEY_REQUIRE_CONFIRMATION,
                    true
                )
            )
            builder.setDeviceCredentialAllowed(allowDeviceCredential)
        }

        if (allowDeviceCredential) {
            mStartRespectingCancel = false
            mHandler.postDelayed(Runnable {
                // Hack almost over 9000, ignore cancel signal if it's within the first
                // quarter second.
                mStartRespectingCancel = true
            }, 250 /* ms */)
        }

        mBiometricPrompt = builder.build()
        mCancellationSignal = CancellationSignal()
        if (mCryptoObject == null) {
            mBiometricPrompt.authenticate(
                mCancellationSignal, mExecutor,
                mAuthenticationCallback
            )
        } else {
            mBiometricPrompt.authenticate(
                BiometricFragment.wrapCryptoObject(mCryptoObject), mCancellationSignal,
                mExecutor, mAuthenticationCallback
            )
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
