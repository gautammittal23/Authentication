# Authentication
## Bio Authentication and pin or pattern Authentication

#### Official Link [ BioPrompt](https://developer.android.com/reference/androidx/biometric/BiometricPrompt.html)

### Add Dependency


    implementation 'androidx.biometric:biometric:1.0.1'


### Add the following to Manifest file 
```
 <uses-permission android:name="android.permission.USE_BIOMETRIC" />
    <uses-permission android:name="android.permission.USE_FINGERPRINT" />
```


Here We will use System security to secure our apps . If Sytsem has FingerPrint then it will Prompt Dialog to unlock 
with Biometric, else it will use System pin or pattern.


  
  ### Code Snippet

        
   ```
    fun fingerprintScannerExist(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //Fingerprint API only available on from Android 6.0 (M)
            /*
            * Deprecated in API 28  (@use BioPrompt and handle errorcode)
            * */
            val fingerprintManager =
                context.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
            return fingerprintManager.isHardwareDetected && fingerprintManager.hasEnrolledFingerprints()
        }
            return false
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

  
   
   ```
   
   For Pin or Pattern
   
   ```
       fun createKeyguardIntent(
        context: Context,
        title: String = "Unlock",
        description: String = "Enter your Auth"
    ): Intent {
        val keyguardManager =
            context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        return keyguardManager.createConfirmDeviceCredentialIntent(title, description)
    }
   
   ```
