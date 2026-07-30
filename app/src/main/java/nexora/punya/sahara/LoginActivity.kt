package nexora.punya.sahara

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Female
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Male
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.Unit
import kotlin.apply
import kotlin.let
import kotlin.repeat
import kotlin.toString

class LoginActivity : AppCompatActivity() {
    private val SaharaDarkBlue = Color(0xFF1B2C49)
    private val SaharaGreen = Color(0xFF5A7B5E)
    private val SaharaLightGreen = Color(0xFFD6E0D7)
    private val BackgroundWhite = Color(0xFFF8F9FA)

    private val authViewModel: AuthViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var currentScreen by remember { mutableStateOf("ROUTER") }

            when (currentScreen) {
                "ROUTER" -> {
                    AppSessionRouter(
                        onNavigateToLogin = {
                            currentScreen = "LOGIN"
                        },
                        onNavigateToBasicDetails = {
                            currentScreen = "BASIC_DETAILS"
                        },
                        onNavigateToHome = {
                            // User is already logged in and profile is complete -> Go to MainActivity
                            val intent =
                                Intent(this@LoginActivity, MainActivity::class.java).apply {
                                    flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                }
                            startActivity(intent)
                            finish()
                        }
                    )
                }

                "LOGIN" -> {
                    // Your Login/OTP Composable
                    SaharaAuthScreen()
                }

                "BASIC_DETAILS" -> {
                    // Basic Details Composable
                    BasicDetailsScreen(
                        onNavigateNext = {
                            val intent =
                                Intent(this@LoginActivity, MainActivity::class.java).apply {
                                    flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                }
                            startActivity(intent)
                            finish()
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun SaharaAuthScreen(viewModel: AuthViewModel = viewModel()) {
        val context = LocalContext.current as Activity

        LaunchedEffect(viewModel.errorMessage) {
            viewModel.errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = BackgroundWhite
        ) {
            when (viewModel.screenState) {
                AuthScreenState.PHONE_INPUT -> PhoneInputScreen(viewModel, context)
                AuthScreenState.OTP_VERIFICATION -> OtpVerificationScreen(viewModel)
                AuthScreenState.SUCCESS -> {
                    BasicDetailsScreen(
                        viewModel = profileViewModel,
                        onNavigateNext = {
                            val intent = Intent(context, MainActivity::class.java).apply {
                                flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PhoneInputScreen(viewModel: AuthViewModel, activity: Activity) {
        val keyboardController = LocalSoftwareKeyboardController.current
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "SAHARA",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = SaharaDarkBlue
            )
            Text(text = "Smart Care. Always There.", fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(24.dp))

            Image(
                painter = painterResource(id = R.drawable.budhabudhiya),
                contentDescription = "Illustration of elderly couple",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(color = SaharaLightGreen, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp)),

                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Welcome to SAHARA",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = SaharaDarkBlue
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Enter your mobile number to\nget started",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Mobile Number",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SaharaDarkBlue
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = viewModel.phoneNumber,
                    onValueChange = { 
                        viewModel.updatePhoneNumber(it)
                        if (it.length == 10) {
                            keyboardController?.hide()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    leadingIcon = {
                        Row(
                            modifier = Modifier.padding(start = 16.dp, end = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("+91", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            VerticalDivider(
                                modifier = Modifier.height(24.dp),
                                thickness = 1.dp,
                                color = Color.LightGray
                            )
                        }
                    },
                    placeholder = { Text("Enter mobile number") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SaharaGreen,
                        unfocusedBorderColor = SaharaGreen
                    ),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.sendVerificationCode(activity) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SaharaGreen),
                enabled = !viewModel.isLoading
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Send OTP", fontSize = 16.sp, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.CheckCircle,
                    contentDescription = "Secure",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "We will send you a One Time Password\n(OTP) to verify your number",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }

    @Composable
    fun OtpVerificationScreen(viewModel: AuthViewModel) {
        val keyboardController = LocalSoftwareKeyboardController.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = { viewModel.goBack() },
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.align(Alignment.TopCenter)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "SAHARA",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = SaharaDarkBlue
                    )
                    Text(text = "Smart Care. Always There.", fontSize = 10.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = "Verify Your Number",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = SaharaDarkBlue
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "We have sent a 6-digit OTP to",
                fontSize = 16.sp,
                color = Color.Gray
            )
            Text(
                text = "+91 ${viewModel.phoneNumber}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = SaharaGreen
            )

            Spacer(modifier = Modifier.height(32.dp))

            BasicTextField(
                value = viewModel.otpCode,
                onValueChange = { 
                    viewModel.updateOtp(it)
                    if (it.length == 6) {
                        keyboardController?.hide()
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                decorationBox = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(6) { index ->
                            val char = when {
                                index >= viewModel.otpCode.length -> ""
                                else -> viewModel.otpCode[index].toString()
                            }
                            val isFocused = viewModel.otpCode.length == index

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(0.8f)
                                    .border(
                                        width = if (isFocused) 2.dp else 1.dp,
                                        color = if (isFocused) SaharaGreen else Color.LightGray,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .background(Color.White, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = char.ifEmpty { "-" },
                                    color = if (char.isEmpty()) Color.LightGray else SaharaDarkBlue,
                                    fontSize = 24.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Resend OTP in 00:45",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))

            val isOtpComplete = viewModel.otpCode.length == 6
            Button(
                onClick = { viewModel.verifyOtp() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isOtpComplete) SaharaGreen else SaharaLightGreen,
                    contentColor = if (isOtpComplete) Color.White else SaharaGreen
                ),
                enabled = isOtpComplete && !viewModel.isLoading
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(color = SaharaGreen, modifier = Modifier.size(24.dp))
                } else {
                    Text("Verify OTP", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = "Help",
                    tint = SaharaGreen,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Didn't receive OTP?\nTry again or check your number",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun BasicDetailsScreen(
        viewModel: ProfileViewModel = viewModel(),
        onNavigateNext: () -> Unit // Call this to go to Home Screen after saving
    ) {
        val context = LocalContext.current
        val scrollState = rememberScrollState()
        val keyboardController = LocalSoftwareKeyboardController.current

        // Handle side effects (Toasts & Navigation)
        LaunchedEffect(viewModel.errorMessage) {
            viewModel.errorMessage?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
        }
        LaunchedEffect(viewModel.isSaveSuccessful) {
            if (viewModel.isSaveSuccessful) {
                Toast.makeText(context, "Profile Saved!", Toast.LENGTH_SHORT).show()
                onNavigateNext()
            }
        }

        Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF8F9FA)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Bar & Logo
                Box(modifier = Modifier.fillMaxWidth()) {
                    IconButton(
                        onClick = { /* Handle Back */ },
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = SaharaDarkBlue
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.align(Alignment.TopCenter)
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "SAHARA",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = SaharaDarkBlue
                        )
                        Text(
                            text = "Smart Care. Always There.",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Header Texts
                Text(
                    text = "Basic Details",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = SaharaDarkBlue
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Please provide some basic information to\nhelp us assist you better.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Full Name Field
                CustomTextField(
                    label = "Full Name",
                    value = viewModel.fullName,
                    onValueChange = { viewModel.fullName = it },
                    placeholder = "Enter your full name",
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Person,
                            contentDescription = null,
                            tint = SaharaDarkBlue
                        )
                    }
                )

                // Age Field
                CustomTextField(
                    label = "Age",
                    value = viewModel.age,
                    onValueChange = { viewModel.age = it },
                    placeholder = "Enter your age",
                    keyboardType = KeyboardType.Number,
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.DateRange,
                            contentDescription = null,
                            tint = SaharaDarkBlue
                        )
                    },
                    trailingIcon = {
                        Text(
                            "Years",
                            modifier = Modifier.padding(end = 16.dp),
                            color = Color.Gray
                        )
                    }
                )

                // Gender Dropdown
                var genderExpanded by remember { mutableStateOf(false) }
                val genderOptions = listOf("Male", "Female", "Other")

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Icon(
                            if (viewModel.gender == "Female") Icons.Outlined.Female else Icons.Outlined.Male,
                            contentDescription = null,
                            tint = SaharaDarkBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Gender",
                            fontWeight = FontWeight.SemiBold,
                            color = SaharaDarkBlue,
                            fontSize = 14.sp
                        )
                    }
                    ExposedDropdownMenuBox(
                        expanded = genderExpanded,
                        onExpandedChange = { genderExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = viewModel.gender,
                            onValueChange = {},
                            readOnly = true,
                            placeholder = { Text("Select your gender") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SaharaGreen,
                                unfocusedBorderColor = Color.LightGray,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = genderExpanded,
                            onDismissRequest = { genderExpanded = false }
                        ) {
                            genderOptions.forEach { selectionOption ->
                                DropdownMenuItem(
                                    text = { Text(selectionOption) },
                                    onClick = {
                                        viewModel.gender = selectionOption
                                        genderExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Blood Group Dropdown
                var bloodGroupExpanded by remember { mutableStateOf(false) }
                val bloodGroups =
                    listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-", "Don't Know")

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Icon(
                            Icons.Outlined.WaterDrop,
                            contentDescription = null,
                            tint = SaharaDarkBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Blood Group",
                            fontWeight = FontWeight.SemiBold,
                            color = SaharaDarkBlue,
                            fontSize = 14.sp
                        )
                    }
                    ExposedDropdownMenuBox(
                        expanded = bloodGroupExpanded,
                        onExpandedChange = { bloodGroupExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = viewModel.bloodGroup,
                            onValueChange = {},
                            readOnly = true,
                            placeholder = { Text("Select your blood group") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = bloodGroupExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SaharaGreen,
                                unfocusedBorderColor = Color.LightGray,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = bloodGroupExpanded,
                            onDismissRequest = { bloodGroupExpanded = false }
                        ) {
                            bloodGroups.forEach { selectionOption ->
                                DropdownMenuItem(
                                    text = { Text(selectionOption) },
                                    onClick = {
                                        viewModel.bloodGroup = selectionOption
                                        bloodGroupExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Medical History Field
                CustomTextField(
                    label = "Past Medical History",
                    value = viewModel.medicalHistory,
                    onValueChange = { viewModel.medicalHistory = it },
                    placeholder = "Mention any past illnesses or medical conditions\n(if any)",
                    singleLine = false,
                    modifier = Modifier.height(100.dp),
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Assignment,
                            contentDescription = null,
                            tint = SaharaDarkBlue
                        )
                    }
                )

                // Emergency Contact Number
                CustomTextField(
                    label = "Emergency Contact Number",
                    value = viewModel.emergencyContact,
                    onValueChange = { 
                        if (it.length <= 10) {
                            viewModel.emergencyContact = it 
                            if (it.length == 10) {
                                keyboardController?.hide()
                            }
                        }
                    },
                    placeholder = "Enter emergency contact number",
                    keyboardType = KeyboardType.NumberPassword,
                    leadingIcon = {
                        Row(
                            modifier = Modifier.padding(start = 16.dp, end = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Outlined.Phone,
                                contentDescription = null,
                                tint = SaharaDarkBlue,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("+91", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Icon(
                                Icons.Outlined.KeyboardArrowDown,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Divider(
                                modifier = Modifier
                                    .height(24.dp)
                                    .width(1.dp)
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Save & Continue Button
                Button(
                    onClick = { viewModel.saveUserProfile() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C7A6A)), // Deep Teal from image
                    enabled = !viewModel.isLoading
                ) {
                    if (viewModel.isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.CheckCircle,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Save & Continue",
                                fontSize = 16.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Security Footer
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SaharaLightGreen, RoundedCornerShape(8.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Outlined.Lock,
                        contentDescription = "Secure",
                        tint = SaharaGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Your information is secure and will only be used\nto help you in emergencies.",
                        fontSize = 11.sp,
                        color = SaharaGreen,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    // Helper Composable for reusable text fields to keep code clean
    @Composable
    fun CustomTextField(
        label: String,
        value: String,
        onValueChange: (String) -> Unit,
        placeholder: String,
        keyboardType: KeyboardType = KeyboardType.Text,
        singleLine: Boolean = true,
        modifier: Modifier = Modifier,
        leadingIcon: @Composable (() -> Unit)? = null,
        trailingIcon: @Composable (() -> Unit)? = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                leadingIcon?.let {
                    it()
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    label,
                    fontWeight = FontWeight.SemiBold,
                    color = SaharaDarkBlue,
                    fontSize = 14.sp
                )
            }
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = modifier.fillMaxWidth(),
                placeholder = { Text(placeholder, color = Color.Gray, fontSize = 14.sp) },
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                singleLine = singleLine,
                trailingIcon = trailingIcon,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SaharaGreen,
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
        }
    }
}
