package com.Authentication

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // calling fingerAuth Function
//         fingerAuth()

        // pinAndPattern()

        authenticateApp()

    }


    fun fingerAuth() {
        // For FingerPrint

        val executor = Executors.newSingleThreadExecutor()


// Check if we're running on Android 6.0 (M) or higher
        // Check if we're running on Android 6.0 (M) or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //Fingerprint API only available on from Android 6.0 (M)
            val fingerprintManager =
                this.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
            if (!fingerprintManager.isHardwareDetected) { // Device doesn't support fingerprint authentication
                Toast.makeText(
                    this,
                    "This Device Doesnot Support FingerPrint Auth",
                    Toast.LENGTH_LONG
                ).show()
            } else if (!fingerprintManager.hasEnrolledFingerprints()) { // User hasn't enrolled any fingerprints to authenticate with
                Toast.makeText(this, "This Device has not saved any Biometric ", Toast.LENGTH_LONG)
                    .show()
            } else { // Everything is ready for fingerprint authentication

                //  Toast.makeText(this,"This Device Support FingerPrint Auth",Toast.LENGTH_LONG).show()


                val biometricPrompt = BiometricPrompt(
                    this,
                    executor,
                    object : BiometricPrompt.AuthenticationCallback() {

                        override fun onAuthenticationError(
                            errorCode: Int,
                            errString: CharSequence
                        ) {
                            super.onAuthenticationError(errorCode, errString)
                            // Toast.makeText(applicationContext,"Called when an unrecoverable error has been encountered and the operation is complete",Toast.LENGTH_LONG).show()


                            // checking if cancel button is clicked

                            if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                                // user clicked negative button

                                finish()
                                System.exit(0)
                            }
                        }

                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            super.onAuthenticationSucceeded(result)
                            // Toast.makeText(applicationContext,"Called when a biometric is recognized.",Toast.LENGTH_LONG).show()

                            startActivity(Intent(applicationContext, WelcomeActivity::class.java))
                        }

                    })


                // Creating Instance of BiometricPrompt.PromptInfo

                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("FingerPrint Auth.")
                    .setSubtitle("Confirm FingerPrint to continue!!")
                    .setNegativeButtonText("cancel")
                    .build()


                biometricPrompt.authenticate(promptInfo)
            }


        }

    }


    fun pinAndPattern() {
        val keyguardManager =
            getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager



        return if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) && keyguardManager.isKeyguardSecure) {
            val i = keyguardManager.createConfirmDeviceCredentialIntent(
                "Unlock",
                "Enter your Credentials"
            )
            startActivityForResult(i, 100)

        } else {

        }


    }


    //method to authenticate app
    private fun authenticateApp() { //Get the instance of KeyGuardManager
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
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            100 -> if (resultCode === Activity.RESULT_OK) { //If screen lock authentication is success update text

                startActivity(Intent(applicationContext, WelcomeActivity::class.java))

            } else { //If screen lock authentication is failed update text

            }
            /*   101 ->  //When user is enabled Security settings then we don't get any kind of RESULT_OK
   //So we need to check whether device has enabled screen lock or not
                   if (isDeviceSecure()) { //If screen lock enabled show toast and start intent to authenticate user
                       Toast.makeText(
                           this,
                           resources.getString(R.string.device_is_secure),
                           Toast.LENGTH_SHORT
                       ).show()
                       authenticateApp()
                   } else { //If screen lock is not enabled just update text
                       textView.setText(resources.getString(R.string.security_device_cancelled))
                   }*/
        }
    }


}
