package nexora.punya.sahara
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                MainHomeScreen()
            }
        }
    }
}

@Composable
fun MainHomeScreen() {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val uid = currentUser?.uid

    var isEmergencyTriggered by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // ====================================================================
    // 1. REAL-TIME DATABASE LISTENER (Listens for needHelp true/false)
    // ====================================================================
    DisposableEffect(uid) {
        if (uid == null) return@DisposableEffect onDispose {}

        val userDbRef = FirebaseDatabase.getInstance().getReference("users").child(uid)
        val helpListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // If needHelp changes to false in Realtime DB, this triggers and updates the UI
                val needHelpVal = snapshot.child("needHelp").getValue(Boolean::class.java) ?: false
                isEmergencyTriggered = needHelpVal
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }

        userDbRef.addValueEventListener(helpListener)

        // Clean up the listener when leaving screen
        onDispose {
            userDbRef.removeEventListener(helpListener)
        }
    }

    // Function to write Location, Timestamp, and needHelp = true to Realtime DB
    @SuppressLint("MissingPermission")
    fun sendEmergencySignal() {
        if (uid == null) return
        isLoading = true

        val userDbRef = FirebaseDatabase.getInstance().getReference("users").child(uid)

        // Helper function to push the payload
        fun pushPayload(lat: Double?, lng: Double?) {
            val emergencyData = hashMapOf<String, Any?>(
                "needHelp" to true,
                "timestamp" to System.currentTimeMillis(),
                "latitude" to lat,
                "longitude" to lng
            )

            userDbRef.updateChildren(emergencyData)
                .addOnSuccessListener {
                    isLoading = false
                    Toast.makeText(context, "Emergency alert sent!", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener { e ->
                    isLoading = false
                    Toast.makeText(context, "Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

        // 1. Check for BOTH Fine and Coarse permissions to handle "Approximate" choice
        val hasFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        if (hasFine || hasCoarse) {
            // 2. Check if device Location Services are enabled
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
            val isEnabled = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)

            if (!isEnabled) {
                Toast.makeText(context, "Please enable location services in settings.", Toast.LENGTH_SHORT).show()
                pushPayload(null, null)
                return
            }

            val priority = if (hasFine) Priority.PRIORITY_HIGH_ACCURACY else Priority.PRIORITY_BALANCED_POWER_ACCURACY
            val locationRequest = CurrentLocationRequest.Builder()
                .setPriority(priority)
                .build()

            // 3. Try to get a fresh location fix
            fusedLocationClient.getCurrentLocation(locationRequest, null)
                .addOnSuccessListener { location ->
                    if (location != null) {
                        Log.d("Sahara", "Location received: ${location.latitude}, ${location.longitude}")
                        pushPayload(location.latitude, location.longitude)
                    } else {
                        // 4. Fallback: If fresh fix is null (common after fresh grant), try lastLocation
                        fusedLocationClient.lastLocation.addOnSuccessListener { lastLoc ->
                            if (lastLoc != null) {
                                Log.d("Sahara", "Current Location null, using Last Location")
                                pushPayload(lastLoc.latitude, lastLoc.longitude)
                            } else {
                                Log.d("Sahara", "Both current and last location are null")
                                pushPayload(null, null)
                            }
                        }.addOnFailureListener {
                            pushPayload(null, null)
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Sahara", "Failed to get location", e)
                    // Try lastLocation as a final attempt on failure
                    fusedLocationClient.lastLocation.addOnSuccessListener { lastLoc ->
                        pushPayload(lastLoc?.latitude, lastLoc?.longitude)
                    }.addOnFailureListener {
                        pushPayload(null, null)
                    }
                }
        } else {
            // Send alert without location if permission wasn't granted
            pushPayload(null, null)
        }
    }

    // Permission Launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        // Proceed if any permission was granted
        if (results.values.any { it }) {
            sendEmergencySignal()
        } else {
            // Still send signal even if denied (location will be null)
            sendEmergencySignal()
        }
    }

    fun handleEmergencyButtonClick() {
        val hasFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        if (!hasFine && !hasCoarse) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            sendEmergencySignal()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF8F9FA)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 32.dp)
            ) {
                Text(
                    text = "SAHARA",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B2C49)
                )
                Text(
                    text = "Smart Care. Always There.",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            // Center Section: Emergency Button
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (isEmergencyTriggered) "HELP REQUESTED!" else "EMERGENCY ASSISTANCE",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isEmergencyTriggered) Color(0xFFD32F2F) else Color(0xFF1B2C49)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (isEmergencyTriggered)
                        "Responders have been notified of your location."
                    else
                        "Press the button below to send your location to emergency responders.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Pulsing/Outer Red Circle
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .background(
                            if (isEmergencyTriggered) Color(0xFFFFCDD2) else Color(0xFFFFEBEE)
                        )
                        .clickable(enabled = !isLoading && !isEmergencyTriggered) {
                            handleEmergencyButtonClick()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    // Inner Solid Red Button
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                            .background(
                                if (isEmergencyTriggered) Color(0xFFB71C1C) else Color(0xFFD32F2F)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(36.dp))
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Need Help",
                                    tint = Color.White,
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (isEmergencyTriggered) "ACTIVE" else "NEED HELP",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Status Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Status: ",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B2C49)
                    )
                    Text(
                        text = if (isEmergencyTriggered) "Help Alert Active" else "Safe",
                        fontWeight = FontWeight.SemiBold,
                        color = if (isEmergencyTriggered) Color(0xFFD32F2F) else Color(0xFF388E3C)
                    )
                }
            }
        }
    }
}
