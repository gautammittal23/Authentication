package com.fingerprintauth

import android.content.Context
import android.content.Intent
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // calling fingerAuth Function
        fingerAuth()



    }


    fun fingerAuth()
    {
        // For FingerPrint

        val executor = Executors.newSingleThreadExecutor()


// Check if we're running on Android 6.0 (M) or higher
        // Check if we're running on Android 6.0 (M) or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //Fingerprint API only available on from Android 6.0 (M)
            val fingerprintManager =
                this.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
            if (!fingerprintManager.isHardwareDetected) { // Device doesn't support fingerprint authentication
                Toast.makeText(this,"This Device Doesnot Support FingerPrint Auth", Toast.LENGTH_LONG).show()
            } else if (!fingerprintManager.hasEnrolledFingerprints()) { // User hasn't enrolled any fingerprints to authenticate with
                Toast.makeText(this,"This Device has not saved any Biometric ", Toast.LENGTH_LONG).show()
            } else { // Everything is ready for fingerprint authentication

                //  Toast.makeText(this,"This Device Support FingerPrint Auth",Toast.LENGTH_LONG).show()


                val biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
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

                        startActivity(Intent(applicationContext,WelcomeActivity::class.java))
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        // Toast.makeText(applicationContext,"Called when a biometric is valid but not recognized.",Toast.LENGTH_LONG).show()
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
}
