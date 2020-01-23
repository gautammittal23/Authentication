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

<<<<<<< HEAD
        if (authManager.fingerprintScannerExist(this)) {
            authManager.initBiometricVerification(
                this,
                authManager.createBiometricPromptInfo()
            )
        } else {
            authManager.startActivityForKeyGuard(this, authManager.createKeyguardIntent(this))
        }

=======
        // calling fingerAuth Function
//         fingerAuth()

        
        // Pin or Pattern Auth Function
        
        pinPatternAuthenticateApp()
>>>>>>> e1a79dbd3f1d55bacafff20df3dfa71c94152211

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


<<<<<<< HEAD
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
=======
>>>>>>> e1a79dbd3f1d55bacafff20df3dfa71c94152211

    override fun onBioAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        runOnUiThread {
            showToast("onBioAuthenticationSucceeded")
        }
        finish()
        startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
    }

<<<<<<< HEAD
    override fun onBioAuthenticationError(errorCode: Int, errString: CharSequence) {
        runOnUiThread {
            showToast("onBioAuthenticationError")
            finish()
            System.exit(0)
=======
    //method to authenticate app
    private fun pinPatternAuthenticateApp() { //Get the instance of KeyGuardManager
        val keyguardManager =
            getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        //Check if the device version is greater than or equal to Lollipop(21)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //Create an intent to open device screen lock screen to authenticate
//Pass the Screen Lock screen Title and Description
            val i = keyguardManager.createConfirmDeviceCredentialIntent("Unlock", "Enter your Auth")
            try { //Start activity for result
                startActivityForResult(i, 100)
            } catch (e: Exception) { //If some exception occurs means Screen lock is not set up please set screen lock
//Open Security screen directly to enable patter lock
                val intent = Intent(Settings.ACTION_SECURITY_SETTINGS)
                try { //Start activity for result
                    startActivityForResult(intent, 101)
                } catch (ex: Exception) { //If app is unable to find any Security settings then user has to set screen lock manually

                }
            }
>>>>>>> e1a79dbd3f1d55bacafff20df3dfa71c94152211
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
