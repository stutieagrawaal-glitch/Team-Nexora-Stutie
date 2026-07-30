package nexora.punya.sahara

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val realtimeDb = FirebaseDatabase.getInstance().getReference("users")

    var fullName by mutableStateOf("")
    var age by mutableStateOf("")
    var bloodGroup by mutableStateOf("")
    var medicalHistory by mutableStateOf("")
    var emergencyContact by mutableStateOf("")
    var gender by mutableStateOf("")

    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var isSaveSuccessful by mutableStateOf(false)
        private set

    fun saveUserProfile() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            errorMessage = "User not authenticated"
            return
        }

        if (fullName.isBlank() || age.isBlank() || emergencyContact.isBlank()) {
            errorMessage = "Please fill in all required fields"
            return
        }

        isLoading = true
        errorMessage = null

        // Create a data map to save to Firestore
        val userProfile = hashMapOf(
            "uid" to currentUser.uid,
            "phoneNumber" to currentUser.phoneNumber, // from auth session
            "fullName" to fullName,
            "age" to age,
            "gender" to gender,
            "bloodGroup" to bloodGroup,
            "medicalHistory" to medicalHistory,
            "emergencyContact" to "+91$emergencyContact",
            "profileCompleted" to true

        )

        // Save to Firestore under the 'users' collection using the UID as the document ID
        db.collection("users").document(currentUser.uid)
            .set(userProfile)
            .addOnSuccessListener {
                isLoading = false
                isSaveSuccessful = true
                // 2. Initialize Realtime Database with needHelp = false under user UID
                realtimeDb.child(currentUser.uid).child("needHelp").setValue(false)
                    .addOnSuccessListener {
                        isLoading = false
                        isSaveSuccessful = true
                    }
                    .addOnFailureListener { e ->
                        isLoading = false
                        errorMessage = "Realtime DB error: ${e.message}"
                    }
            }
            .addOnFailureListener { e ->
                isLoading = false
                errorMessage = "Firestore error: ${e.message}"
            }
            .addOnFailureListener { e ->
                isLoading = false
                errorMessage = e.message
            }
    }
}