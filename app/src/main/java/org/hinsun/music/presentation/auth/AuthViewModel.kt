package org.hinsun.music.presentation.auth

import android.content.Context
import android.credentials.GetCredentialException
import androidx.lifecycle.ViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import dagger.hilt.android.lifecycle.HiltViewModel
import org.hinsun.music.BuildConfig
import javax.inject.Inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialRequest.Builder
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.Auth
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.hinsun.core.https.HttpResponse
import org.hinsun.core.storage.CryptoStorage
import org.hinsun.domain.models.OAuthRequest
import org.hinsun.domain.usecases.OAuthUseCase
import timber.log.Timber
import java.util.UUID

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val oAuthUseCase: OAuthUseCase,
    private val cryptoStorage: CryptoStorage
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    @OptIn(ExperimentalUuidApi::class)
    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(true)
        .setServerClientId(BuildConfig.WEB_CLIENT_ID)
        .setFilterByAuthorizedAccounts(false)
        .setAutoSelectEnabled(false)
        .setNonce(Uuid.random().toString())
        .build()

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun signIn(context: Context) {
        viewModelScope.launch {
            try {
                val credentialManager = CredentialManager.create(context)

                val request: GetCredentialRequest = Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result = credentialManager.getCredential(
                    request = request,
                    context = context,
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                e.printStackTrace()
            }
        }
    }

    fun signInWithBiometric(context: Context) {
        val isBiometricAvailable = isBiometricAvailable(context)
        if (!isBiometricAvailable) {
            Toast.makeText(context, "Biometric authentication not available", Toast.LENGTH_SHORT)
                .show()
            return
        }

        Toast.makeText(context, "Biometric authentication available", Toast.LENGTH_SHORT).show()
    }

    // Create BiometricPrompt.PromptInfo with customized display text
    private fun getPromptInfo(context: FragmentActivity): BiometricPrompt.PromptInfo {
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Authenticate to access your account")
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

    // Register user biometrics by encrypting a randomly generated token
    fun register(
        context: FragmentActivity,
        onSuccess: (authResult: BiometricPrompt.AuthenticationResult) -> Unit = {}
    ) {
        val cipher = cryptoStorage.initEncryptionCipher()

        val biometricPrompt = getBiometricPrompt(context) { authResult ->
            authResult.cryptoObject?.cipher?.let { cipher ->
                // Dummy token for now(in production app, generate a unique and genuine token
                // for each user registration or consider using token received from authentication server)
                val token = UUID.randomUUID().toString()
                Toast.makeText(context, "Encrypting token: $token", Toast.LENGTH_SHORT).show()
                val encryptedToken = cryptoStorage.encrypt(token, cipher)

                cryptoStorage.writeWithEncrypt(
                    key = "ENCRYPTED_FILE_NAME",
                    value = encryptedToken
                )

                // Execute custom action on successful registration
                onSuccess(authResult)
            }
        }

        biometricPrompt.authenticate(getPromptInfo(context), BiometricPrompt.CryptoObject(cipher))
    }

    // Authenticate user using biometrics by decrypting stored token
    fun authenticate(
        context: FragmentActivity,
        onSuccess: (plainText: String) -> Unit
    ) {

        val encryptedData = cryptoStorage.readWithDecrypt(key = "ENCRYPTED_FILE_NAME")

        encryptedData?.let { data ->
            val cipher = cryptoStorage.initDecryptionCipher(data.initializationVector)

            val biometricPrompt = getBiometricPrompt(context) { authResult ->
                authResult.cryptoObject?.cipher?.let { cipher ->
                    val plainText = cryptoStorage.decrypt(data.ciphertext, cipher)
                    // Execute custom action on successful authentication
                    Toast.makeText(context, "Decrypted token: $plainText", Toast.LENGTH_SHORT)
                        .show()
                    onSuccess(plainText)
                }
            }

            val promptInfo = getPromptInfo(context)
            biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
        }
    }

    private fun isBiometricAvailable(context: Context): Boolean {
        val biometricManager = BiometricManager.from(context)
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> false
        }
    }

    private fun callToServer(idToken: String) {
        viewModelScope.launch {
            val response = oAuthUseCase.invoke(
                OAuthRequest(
                    idToken = idToken,
                    deviceToken = idToken
                )
            )

            response.collect { httpResponse ->
                when (httpResponse) {
                    is HttpResponse.HttpSuccess -> {
                        Timber.tag(TAG).d("Success: $httpResponse")
                    }

                    is HttpResponse.HttpFailure -> {
                        Timber.tag(TAG).d("Failure: $httpResponse")
                    }

                    is HttpResponse.HttpProcess -> {
                        Timber.tag(TAG).d("Process: $httpResponse")
                    }
                }
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {

        Timber.tag(TAG).d("Result: $result and stating flow")

        // Handle the successfully returned credential.
        when (val credential = result.credential) {
            // Passkey credential
            is PublicKeyCredential -> {
                // Share responseJson such as a GetCredentialResponse on your server to
                // validate and authenticate
                val response = credential.authenticationResponseJson
                // You can use the members of response directly for UX purposes, but don't use
                // them to store or control access to user data. For that you first need to
                // validate the response:
                // pass responseJson to the backend server.
                Timber.tag(TAG).d("responseJson: $response")
            }

            // Password credential
            is PasswordCredential -> {
                // Send ID and password to your server to validate and authenticate.
                val username = credential.id
                val password = credential.password
            }

            // GoogleIdToken credential
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        // Use googleIdTokenCredential and extract the ID to validate and
                        // authenticate on your server.
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)

                        Timber.tag(TAG)
                            .d("googleIdTokenCredential: ${googleIdTokenCredential.idToken}")
                        // You can use the members of googleIdTokenCredential directly for UX
                        // purposes, but don't use them to store or control access to user
                        // data. For that you first need to validate the token:
                        // pass googleIdTokenCredential.getIdToken() to the backend server.


                        callToServer(googleIdTokenCredential.idToken)

                    } catch (e: GoogleIdTokenParsingException) {
                        e.printStackTrace()
                    }
                } else {
                    // Catch any unrecognized custom credential type here.
                    Timber.tag(TAG).d("Unexpected type of credential")
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Timber.tag(TAG).d("Unexpected type of credential")
            }
        }
    }

    companion object {
        const val TAG = "HinsunMusic"
    }
}