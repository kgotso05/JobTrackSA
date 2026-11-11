
package com.example.jobtracksa.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.CustomCredential
import com.example.jobtracksa.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class GoogleAuthManager(context: Context) {

    private val credentialManager = CredentialManager.create(context)
    private val auth = FirebaseAuth.getInstance()
    private val appContext = context.applicationContext

    suspend fun signIn(): Result<String> {
        return try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(
                    appContext.getString(R.string.default_web_client_id)
                )
                .setAutoSelectEnabled(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(
                context = appContext,
                request = request
            )

            val credential = result.credential

            if (credential !is CustomCredential ||
                credential.type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                return Result.failure(
                    Exception("Invalid Google credential")
                )
            }

            val googleCredential =
                GoogleIdTokenCredential.createFrom(credential.data)

            val firebaseCredential = GoogleAuthProvider.getCredential(
                googleCredential.idToken,
                null
            )

            val authResult = auth.signInWithCredential(
                firebaseCredential
            ).await()

            val firebaseUser = authResult.user
                ?: throw Exception("Firebase user not found")

            val idToken = firebaseUser.getIdToken(true).await().token
                ?: throw Exception("Could not retrieve Firebase ID token")

            Result.success(idToken)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        auth.signOut()
    }
}
