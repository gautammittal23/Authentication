package com.Authentication

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executors

/**
 * @author Gautam Mittal
 * 22/1/20
 */
class AuthenticationManager {
    companion object {
        val instance = AuthenticationManager()
        const val REQUEST_CODE_PIN = 100
        const val REQUEST_CODE_SETTINGS = 101
        fun isNegativeButtonClicked(errorCode: Int) =
            errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON


        fun noFingerprintScanner(errorCode: Int): Boolean {
            return (errorCode == BiometricPrompt.ERROR_HW_NOT_PRESENT)
        }



    }

    lateinit var callback: BiometricCallback

    fun setAuthCallback(callback: BiometricCallback) {
        this.callback = callback
    }

    private val mExecutor by lazy { Executors.newSingleThreadExecutor() }


    fun fingerprintScannerExist(context: Context): Boolean {

        val biometricManager = BiometricManager.from(context)
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                return true
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                return false
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                return false
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                return false
        }
        return true
    }

    fun createBiometricPromptInfo(
        title: String = "FingerPrint Auth.",
        subtitle: String = "Confirm FingerPrint to continue!!",
        negativeButtonText: String = "cancel"
    ): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setNegativeButtonText(negativeButtonText)
            .build()
    }

    fun initBiometricVerification(
        fragmentActivity: FragmentActivity,
        biometricPromptInfo: BiometricPrompt.PromptInfo

    ) {
        val biometricPrompt = BiometricPrompt(
            fragmentActivity,
            mExecutor,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    callback.onBioAuthenticationError(errorCode, errString)
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    callback.onBioAuthenticationSucceeded(result)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    callback.onBioAuthenticationFailed()
                }
            })

        biometricPrompt.authenticate(biometricPromptInfo)
    }

    interface BiometricCallback {

        fun onBioAuthenticationFailed()
        fun onBioAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult)
        fun onBioAuthenticationError(
            errorCode: Int,
            errString: CharSequence
        )

        fun onKeyGuardSuccess()
        fun onKeyGuardFailure()
    }

    fun createKeyguardIntent(
        context: Context,
        title: String = "Unlock",
        description: String = "Enter your Auth"
    ): Intent {
        val keyguardManager =
            context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        return keyguardManager.createConfirmDeviceCredentialIntent(title, description)
    }

    fun startActivityForKeyGuard(a: Activity, i: Intent) {
        a.startActivityForResult(i, REQUEST_CODE_PIN)
    }

    fun startActivityForSettings(a: Activity) {
        val intent = Intent(Settings.ACTION_SECURITY_SETTINGS)
        a.startActivityForResult(intent, REQUEST_CODE_SETTINGS)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_PIN -> if (resultCode == Activity.RESULT_OK) { //If screen lock authentication is success
                callback.onKeyGuardSuccess()

            } else if (resultCode == AppCompatActivity.RESULT_CANCELED) {
                callback.onKeyGuardFailure()

            }
            /*   101 ->  //When user is enabled Security settings then we don't get any kind of RESULT_OK
   //So we need to check whether device has enabled screen lock or not
                   if (isDeviceSecure()) { //If screen lock enabled show toast and start intent to authenticate user
                                         authenticateApp()
                   } else { //If screen lock is not enabled just update text
                       textView.setText(resources.getString(R.string.security_device_cancelled))
                   }*/
        }
    }


}
