package nexora.punya.sahara

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

enum class AuthScreenState {
    PHONE_INPUT, OTP_VERIFICATION, SUCCESS
}

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    var screenState by mutableStateOf(AuthScreenState.PHONE_INPUT)
        private set

    var phoneNumber by mutableStateOf("")
    var otpCode by mutableStateOf("")
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    private var storedVerificationId: String = ""
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    fun updatePhoneNumber(number: String) {
        if (number.length <= 10) phoneNumber = number
    }

    fun updateOtp(code: String) {
        if (code.length <= 6) otpCode = code
    }

    fun sendVerificationCode(activity: Activity) {
        if (phoneNumber.length != 10) {
            errorMessage = "Enter a valid 10-digit number"
            return
        }

        isLoading = true
        errorMessage = null
        val formattedNumber = "+91$phoneNumber"

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(formattedNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Auto-retrieval completed
                    signInWithCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    isLoading = false
                    errorMessage = e.message
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    isLoading = false
                    storedVerificationId = verificationId
                    resendToken = token
                    screenState = AuthScreenState.OTP_VERIFICATION
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOtp() {
        if (otpCode.length != 6) {
            errorMessage = "Enter a valid 6-digit OTP"
            return
        }
        isLoading = true
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, otpCode)
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                isLoading = false
                if (task.isSuccessful) {
                        screenState = AuthScreenState.SUCCESS
                } else {
                    errorMessage = task.exception?.message
                }
            }
    }

    fun goBack() {
        screenState = AuthScreenState.PHONE_INPUT
        otpCode = ""
        errorMessage = null
    }
}
