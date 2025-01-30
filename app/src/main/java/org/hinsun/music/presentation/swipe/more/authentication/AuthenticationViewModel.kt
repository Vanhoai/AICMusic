package org.hinsun.music.presentation.swipe.more.authentication

import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.hinsun.core.storage.AppStorage
import org.hinsun.core.storage.CryptoStorage
import org.hinsun.music.BuildConfig
import org.hinsun.music.presentation.auth.AuthViewModel.Companion.TAG
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val appStorage: AppStorage,
    private val cryptoStorage: CryptoStorage,
) : ViewModel() {
    fun loadIsEnableBiometric(): Boolean = appStorage.readIsEnableBiometric()

    // Register user biometrics by encrypting a randomly generated token
    fun register(
        context: FragmentActivity,
        plainText: String,
        onSuccess: (authResult: BiometricPrompt.AuthenticationResult) -> Unit = {}
    ) {
        val cipher = cryptoStorage.initEncryptionCipher()

        val biometricPrompt = getBiometricPrompt(context) { authResult ->
            authResult.cryptoObject?.cipher?.let { cipher ->
                // Dummy token for now(in production app, generate a unique and genuine token
                // for each user registration or consider using token received from authentication server)
                val encryptedToken = cryptoStorage.encrypt(plainText, cipher)

                cryptoStorage.writeWithEncrypt(
                    key = BuildConfig.BIOMETRIC_KEY,
                    value = encryptedToken
                )

                // Execute custom action on successful registration
                onSuccess(authResult)
            }
        }

        biometricPrompt.authenticate(getPromptInfo(), BiometricPrompt.CryptoObject(cipher))
    }

    // Create BiometricPrompt.PromptInfo with customized display text
    private fun getPromptInfo(): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle("Hinsun Music")
            .setSubtitle("Biometric Authentication")
            .setDescription("Please authenticate to access your account")
            .setConfirmationRequired(false)
            .setNegativeButtonText("Cancel")
            .build()
    }

    // Retrieve a BiometricPrompt instance with a predefined callback
    private fun getBiometricPrompt(
        context: FragmentActivity,
        onAuthSucceed: (BiometricPrompt.AuthenticationResult) -> Unit
    ): BiometricPrompt {
        val biometricPrompt =
            BiometricPrompt(
                context,
                ContextCompat.getMainExecutor(context),
                object : BiometricPrompt.AuthenticationCallback() {
                    // Handle successful authentication
                    override fun onAuthenticationSucceeded(
                        result: BiometricPrompt.AuthenticationResult
                    ) {
                        Timber.tag(TAG).d("Authentication Succeeded: ${result.cryptoObject}")
                        // Execute custom action on successful authentication
                        onAuthSucceed(result)
                    }

                    // Handle authentication errors
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        Timber.tag(TAG).d("Authentication Error: $errorCode $errString")
                    }

                    // Handle authentication failures
                    override fun onAuthenticationFailed() {
                        Timber.tag(TAG).d("Authentication Failed")
                    }
                }
            )
        return biometricPrompt
    }
}