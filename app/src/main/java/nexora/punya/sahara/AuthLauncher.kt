package nexora.punya.sahara
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

enum class AppStartState {
    LOADING,
    NOT_AUTHENTICATED,
    NEEDS_BASIC_DETAILS,
    AUTHENTICATED
}
private val SaharaGreen = Color(0xFF5A7B5E)
@Composable
fun AppSessionRouter(
    onNavigateToLogin: () -> Unit,
    onNavigateToBasicDetails: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    var startState by remember { mutableStateOf(AppStartState.LOADING) }

    LaunchedEffect(Unit) {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser == null) {
            // No user logged in -> Go to Login/OTP
            startState = AppStartState.NOT_AUTHENTICATED
        } else {
            // User logged in -> Check if Basic Details are saved in Firestore
            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists() && document.getBoolean("profileCompleted") == true) {
                        startState = AppStartState.AUTHENTICATED
                    } else {
                        startState = AppStartState.NEEDS_BASIC_DETAILS
                    }
                }
                .addOnFailureListener {
                    // If network fails or document doesn't exist, route to details or home as needed
                    startState = AppStartState.NEEDS_BASIC_DETAILS
                }
        }
    }

    when (startState) {
        AppStartState.LOADING -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = SaharaGreen)

            }
        }
        AppStartState.NOT_AUTHENTICATED -> {
            LaunchedEffect(Unit) { onNavigateToLogin() }
        }
        AppStartState.NEEDS_BASIC_DETAILS -> {
            LaunchedEffect(Unit) { onNavigateToBasicDetails() }
        }
        AppStartState.AUTHENTICATED -> {
            LaunchedEffect(Unit) { onNavigateToHome() }
        }
    }
}