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
import androidx.annotation.RequiresApi
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialRequest.Builder
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.launch
import org.hinsun.domain.usecases.OAuthUseCase
import timber.log.Timber

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val oAuthUseCase: OAuthUseCase
) : ViewModel() {

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

    private fun handleSignIn(result: GetCredentialResponse) {
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
        const val TAG = "AuthViewModel"
    }
}