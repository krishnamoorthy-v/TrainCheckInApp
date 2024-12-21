package com.example.traincheckinapp

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import java.util.Calendar
import java.util.concurrent.Executor

import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.traincheckinapp.models.Login
import com.example.traincheckinapp.models.LoginResponse
import com.example.traincheckinapp.models.OTPResponse
import com.example.traincheckinapp.models.SignUpResponse
import com.example.traincheckinapp.models.User
import com.example.traincheckinapp.storage.UserSession
import com.example.traincheckinapp.storage.UserSession.userId

import com.google.gson.Gson
import com.example.traincheckinapp.models.OTP
import com.example.traincheckinapp.models.PsgTicketList
import com.example.traincheckinapp.models.TicketInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrainCheckInApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainCheckInApp() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_train),
                            contentDescription = "Train Logo",
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Rail-Inn", color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFFDC143C))
            )
        },
        bottomBar = { Footer(navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("login") { LoginPage(navController) }
            composable("signup") { SignupPage(navController) }
            composable("forgot_password") { ForgotPasswordPage() }
            composable("pnr_list") { PNRListPage(navController) }
            composable("biometric_check_in") { BiometricCheckInPage() }
            composable("otp_check_in") { OTPCheckInPage(navController) }
            composable("journey_details") { JourneyDetailsPage() }
            composable("passenger_care") { PassengerCarePage(navController) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(title: String, navController: NavHostController) {
    CenterAlignedTopAppBar(
        title = {
            Text(title, style = MaterialTheme.typography.titleLarge, color = Color.White)
        },
        navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFFDC143C)
        )
    )
}

@SuppressLint("QueryPermissionsNeeded")

@Composable
fun PassengerCarePage(navController: NavHostController) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CustomTopAppBar(title = "Customer Support", navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Contact Us: +1234567890", style = MaterialTheme.typography.bodyLarge, color = Color(0xFFDC143C))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Email: passenger.care@example.com", style = MaterialTheme.typography.bodyLarge, color = Color(0xFFDC143C))
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val contactNumber = "+1234567890"
                    val whatsappUrl = "https://wa.me/${contactNumber.removePrefix("+")}"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(whatsappUrl))
                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(context, "WhatsApp is not installed.", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_whatsapp),
                    contentDescription = "WhatsApp Icon",
                    tint = Color.Green,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Contact on WhatsApp", color = Color.White)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ForgotPasswordPage() {
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }

    fun sendPasswordResetEmail() {
        if (email.isEmpty()) {
            emailError = "Please enter your email address."
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Please enter a valid email address."
        } else {
            emailError = ""
            message = "Password reset link has been sent to your email."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Forgot Password", style = MaterialTheme.typography.headlineLarge, color = Color(0xFFDC143C))
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Enter your Email Address") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.White),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (emailError.isNotEmpty()) {
            Text(text = emailError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { sendPasswordResetEmail() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC143C)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Send Reset Link", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (message.isNotEmpty()) {
            Text(message, color = Color.Green, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun Footer(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Passenger Care",
            color = Color(0xFFDC143C),
            modifier = Modifier.clickable {
                navController.navigate("passenger_care") {
                    popUpTo("login") {
                        inclusive = true
                    }
                }
            }
        )
    }
}

@Composable
fun SignupPage(navController: NavHostController) {
    val context = LocalContext.current;

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }  // Mobile number field
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var aadhaarNumber by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // Validate Aadhaar number (12 digits)
    fun validateAadhaar(aadhaar: String): Boolean {
        return aadhaar.matches(Regex("^[0-9]{12}$"))
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .imePadding()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create Account", style = MaterialTheme.typography.headlineLarge, color = Color(0xFFDC143C))
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.White),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.White),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = mobile,
            onValueChange = { mobile = it },
            label = { Text("Mobile Number") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.White),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = aadhaarNumber,
            onValueChange = { aadhaarNumber = it },
            label = { Text("Aadhaar Number (12 digits)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.White),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.White),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.White),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.isNotEmpty()) Text(text = errorMessage, color = Color.Red, style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (name.isNotEmpty() && email.isNotEmpty() && mobile.isNotEmpty() && aadhaarNumber.isNotEmpty() &&
                    password.isNotEmpty() && confirmPassword.isNotEmpty() &&
                    validateAadhaar(aadhaarNumber) && password == confirmPassword
                ) {


                    val user = User(
                        name = name,
                        email = email.trim(),
                        mobile = mobile.toLong(),
                        aadhaar = aadhaarNumber.toLong(),
                        password = password
                    )

                    RetrofitClient.instance.signUp(user)
                        .enqueue(object : Callback<SignUpResponse> {
                            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                                Log.e("Signup", t.message.toString())
                            }

                            override fun onResponse(
                                call: Call<SignUpResponse>,
                                response: Response<SignUpResponse>
                            ) {
                                if (response.code() == 400) {
                                    val gson = Gson()
                                    val errorResponse: SignUpResponse = gson.fromJson(
                                        response.errorBody()?.string(),
                                        SignUpResponse::class.java
                                    )
                                    errorMessage = errorResponse.message
                                    Log.e("Sigup", errorResponse.message)

                                } else if (response.code() == 200) {
                                    Toast.makeText(
                                        context,
                                        response.body()?.message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                    Log.d("Signup", response.body()?.message.toString())
                                    navController.navigate("login")
                                }
                            }
                        })

                } else {
                    errorMessage = when {
                        name.isEmpty() -> "Name is required."
                        email.isEmpty() -> "Email is required."
                        mobile.isEmpty() -> "Mobile number is required."
                        aadhaarNumber.isEmpty() -> "Aadhaar number is required."
                        password.isEmpty() -> "Password is required."
                        confirmPassword.isEmpty() -> "Confirm Password is required."
                        !validateAadhaar(aadhaarNumber) -> "Aadhaar number must be exactly 12 digits."
                        password != confirmPassword -> "Passwords do not match."
                        else -> "Please fill all the fields correctly."
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC143C)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up", color = Color.White)
        }
    }
}

@Composable
fun LoginPage(navController: NavHostController) {

    val context = LocalContext.current;

    var isAuthenticated by remember { mutableStateOf(false) }
    var isBiometricAvailable by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    fun validatePassword(): String {
        if (password.length < 8) {
            return "Password must contain at least 8 characters."
        } else {
            return ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Your Journey Awaits", style = MaterialTheme.typography.headlineLarge, color = Color(0xFFDC143C))
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.White),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = validatePassword()
            },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(Color.White),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (passwordError.isNotEmpty()) {
            Text(text = passwordError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {


                    val login = Login(
                        email = email.trim(),
                        password = password
                    )

                    RetrofitClient.instance.login(login)
                        .enqueue(object : Callback<LoginResponse> {
                            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                                Log.e("Login", t.message.toString())
                            }

                            override fun onResponse(
                                call: Call<LoginResponse>,
                                response: Response<LoginResponse>
                            ) {
                                if (response.code() == 400) {
                                    val gson = Gson()
                                    val errorResponse: SignUpResponse = gson.fromJson(
                                        response.errorBody()?.string(),
                                        SignUpResponse::class.java
                                    )
                                    errorMessage = errorResponse.message
                                    Log.e("Login", errorResponse.message)

                                } else if (response.code() == 200) {
                                    Toast.makeText(
                                        context,
                                        "successfully logged in",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    UserSession.userId = response.body()?.message?.id
                                    UserSession.mobile = response.body()?.message?.mobile
                                    UserSession.email = response.body()?.message?.email
                                    UserSession.aadhaar = response.body()?.message?.aadhaar

                                    Log.d("Login", response.body().toString())


                                    navController.navigate("pnr_list")
                                }
                            }
                        })
                } else {
                    errorMessage = "Invalid email or password. Please try again."
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC143C)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { navController.navigate("signup") },
            content = { Text("Create an Account", color = Color(0xFFDC143C)) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { navController.navigate("forgot_password") },
            content = { Text("Forgot Password?", color = Color(0xFFDC143C)) }
        )
    }
}


@Composable
fun OTPCheckInPage(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: OTPViewModel = viewModel()

    // Observing the state from ViewModel
    val otp by remember { viewModel.otp }
    val otpStatus by remember { viewModel.otpStatus }
    val isGeofenceEntered by remember { viewModel.isGeofenceEntered }
    val errorMessage by remember { viewModel.errorMessage }

    // Geofencing setup
    val geofencingClient = LocationServices.getGeofencingClient(context)
    val geofenceRadius = 100f
    val geofenceLatitude = 12.9716
    val geofenceLongitude = 77.5946

    val geofence = Geofence.Builder()
        .setRequestId("check_in_area")
        .setCircularRegion(geofenceLatitude, geofenceLongitude, geofenceRadius)
        .setExpirationDuration(Geofence.NEVER_EXPIRE)
        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
        .build()

    val geofencingRequest = GeofencingRequest.Builder()
        .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
        .addGeofence(geofence)
        .build()
    Log.d("OTP Check In page", " Suspect no 2 OTPCheck success")
    val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    Log.d("OTP Check In page", "OTPCheck success")
    // Add geofence in LaunchedEffect
    LaunchedEffect(Unit) {
        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Geofence", "Geofence added successfully")
                } else {
                    Log.e("Geofence", "Failed to add geofence.")
                }
            }
    }
    Log.d("OTP Check In page", " Suspect end")
    // OTP flow only if inside geofence
    if (isGeofenceEntered) {
        viewModel.sendOTP()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("OTP Check-In", style = MaterialTheme.typography.headlineLarge, color = Color(0xFFDC143C))
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = otp,
                onValueChange = { viewModel.updateOtp(it) },
                label = { Text("Enter OTP") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { viewModel.verifyOTP() }),
                modifier = Modifier.fillMaxWidth().padding(8.dp).background(Color.White)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.verifyOTP() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC143C)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Verify OTP", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(otpStatus, color = if (otpStatus.contains("Successfully")) Color.Green else Color.Red)
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("OTP Check-In", style = MaterialTheme.typography.headlineLarge, color = Color(0xFFDC143C))
            Spacer(modifier = Modifier.height(32.dp))

            Text("You are not inside the geofence area. Please enter the designated area to proceed.")
        }
    }
}

// ViewModel for OTP logic and Geofence status
class OTPViewModel : ViewModel() {
    private val _otp = mutableStateOf("")
    val otp: State<String> get() = _otp

    private val _otpStatus = mutableStateOf("OTP Pending...")
    val otpStatus: State<String> get() = _otpStatus

    private val _isGeofenceEntered = mutableStateOf(true)  // Default to true, can be updated by geofence transition
    val isGeofenceEntered: State<Boolean> get() = _isGeofenceEntered

    private val _errorMessage = mutableStateOf("")
    val errorMessage: State<String> get() = _errorMessage

    // Update OTP field
    fun updateOtp(newOtp: String) {
        _otp.value = newOtp
    }

    // Simulate sending OTP via API
    fun sendOTP() {
        val userId = UserSession.userId
        RetrofitClient.instance.generateOTP(userId.toString())
            .enqueue(object : Callback<OTPResponse> {
                override fun onFailure(call: Call<OTPResponse>, t: Throwable) {
                    _errorMessage.value = t.message.toString()
                    Log.e("OTPViewModel", t.message.toString())
                }

                override fun onResponse(call: Call<OTPResponse>, response: Response<OTPResponse>) {
                    if (response.isSuccessful) {
                        _otpStatus.value = "OTP sent to ${UserSession.mobile}"
                        Log.d("OTPViewModel", "OTP sent to ${UserSession.mobile}")
//                        Toast.makeText(LocalContext.current, "OTP sent to ${userPhoneNumber}", Toast.LENGTH_LONG).show()
                    } else {
                        handleError(response)
                    }
                }
            })
    }

    // Simulate verifying OTP via API
    fun verifyOTP() {
        val OTP = OTP(otp = _otp.value)
        val userId = UserSession.userId

        RetrofitClient.instance.verifyOTP(userId.toString(), OTP)
            .enqueue(object : Callback<OTPResponse> {
                override fun onFailure(call: Call<OTPResponse>, t: Throwable) {
                    _errorMessage.value = t.message.toString()
                    Log.e("OTPViewModel Verified", t.message.toString())
                }

                override fun onResponse(call: Call<OTPResponse>, response: Response<OTPResponse>) {
                    if (response.isSuccessful) {
                        _otpStatus.value = "OTP Verified"
                        Log.d("OTPViewModel Verified", "OTP Verified")
//                        Toast.makeText(LocalContext.current, "OTP Verified", Toast.LENGTH_LONG).show()
                    } else {
                        handleError(response)
                    }
                }
            })
    }

    // Handle error messages
    private fun handleError(response: Response<OTPResponse>) {
        if (response.code() == 400) {
            val gson = Gson()
            val errorResponse: SignUpResponse = gson.fromJson(response.errorBody()?.string(), SignUpResponse::class.java)
            _errorMessage.value = errorResponse.message
            Log.e("OTPViewModel Error", errorResponse.message)
        }
    }
}

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent != null) {
            if (!geofencingEvent.hasError()) {
                val geofenceTransition = geofencingEvent.geofenceTransition
                when (geofenceTransition) {
                    Geofence.GEOFENCE_TRANSITION_ENTER -> {
                        Log.d("GeofenceReceiver", "Entered geofence area")
                    }
                    Geofence.GEOFENCE_TRANSITION_EXIT -> {
                        Log.d("GeofenceReceiver", "Exited geofence area")
                    }
                }
            } else {
                Log.e("GeofenceReceiver", "Error in geofence event: ${geofencingEvent.errorCode}")
            }
        }
    }
}

@Composable
fun PNRListPage(navController: NavHostController) {
    val context = LocalContext.current
    var errorMessage by remember { mutableStateOf("") }
    var pnrList2 by remember { mutableStateOf<List<Long>?>(null) }
    var selectedDate by remember { mutableStateOf(LocalDate.now().toString()) }
    var showDatePicker by remember { mutableStateOf(false) }

    // Converted PNR List with assumed dates
    val convertedPnrList = pnrList2?.map { pnrNumber ->
        Pair(pnrNumber.toString(), selectedDate)
    } ?: emptyList()

    var pnrList = convertedPnrList

    // Fetch PNR List based on the selected date
    fun getPnrList() {
        RetrofitClient.instance.getPassengerPNRList(aadhaar = UserSession.aadhaar.toString(), date = selectedDate)
            .enqueue(object : Callback<PsgTicketList> {
                override fun onFailure(call: Call<PsgTicketList>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                    Log.e("PNR List", t.message.toString())
                }

                override fun onResponse(
                    call: Call<PsgTicketList>, response: Response<PsgTicketList>
                ) {
                    if (response.code() == 400) {
                        val gson = Gson()
                        val errorResponse: SignUpResponse = gson.fromJson(
                            response.errorBody()?.string(),
                            SignUpResponse::class.java
                        )
                        errorMessage = errorResponse.message
                        Log.e("PNR List", errorResponse.message)
                    } else if (response.code() == 200) {
                        Toast.makeText(context, "PNR List Updated", Toast.LENGTH_SHORT).show()
                        Log.d("PNR List", response.body().toString())
                        pnrList2 = response.body()?.pnr_number?.toList()
                        UserSession.pnr_list = pnrList2
                        Log.d("PNR List UserSession pnr list", UserSession.pnr_list.toString())
                    }
                }
            })
    }

    // Initial fetch of PNRs
    LaunchedEffect(Unit) {
        getPnrList()
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(title = "PNR List", navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Your PNRs", style = MaterialTheme.typography.headlineLarge, color = Color(0xFFDC143C))
            Spacer(modifier = Modifier.height(16.dp))

            // Date Picker Button
            Button(onClick = { showDatePicker = true }) {
                Text("Select Date: $selectedDate")
            }

            // Show Date Picker Dialog
            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    onDateSelected = { date ->
                        selectedDate = date
                        showDatePicker = false
                        getPnrList() // Fetch PNRs for the selected date
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display PNRs
            if (pnrList.isNotEmpty()) {
                pnrList.filter { it.second == selectedDate }.forEach { (pnr, _) ->
                    var showDialog by remember { mutableStateOf(false) }

                    PNRItem(pnr) {
                        UserSession.pnr_clicked = pnr
                        showDialog = true
                    }

                    if (showDialog) {
                        CheckInOptionsDialog(
                            onDismiss = { showDialog = false },
                            onOtpCheckInSuccess = {
                                showDialog = false
                                navController.navigate("otp_check_in")
                            },
                            onBiometricCheckInSuccess = {
                                showDialog = false
                                navController.navigate("biometric_check_in")
                            }
                        )
                    }
                }
            } else {
                Text(
                    text = "No PNRs available for the selected date.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}


@Composable
fun PNRItem(pnr: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC143C))
    ) {
        Text("PNR: $pnr", color = Color.White)
    }
}

@Composable
fun CheckInOptionsDialog(
    onDismiss: () -> Unit,
    onOtpCheckInSuccess: () -> Unit,
    onBiometricCheckInSuccess: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Check-In Method") },
        text = {
            Column {
                Button(
                    onClick = {
                        onOtpCheckInSuccess() // Call the success callback for OTP Check-In
                        onDismiss() // Dismiss the dialog
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC143C)),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) {
                    Text("OTP Check-In", color = Color.White)
                }
                Button(
                    onClick = {
                        onBiometricCheckInSuccess() // Call the success callback for Biometric Check-In
                        onDismiss() // Dismiss the dialog
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC143C)),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) {
                    Text("Biometric Check-In", color = Color.White)
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@SuppressLint("DefaultLocale")
@Composable
fun DatePickerDialog(onDismissRequest: () -> Unit, onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Create a DatePickerDialog
    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
            onDateSelected(selectedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Show the DatePickerDialog
    datePickerDialog.setOnDismissListener { onDismissRequest() }
    datePickerDialog.show()
}

@Composable
fun BiometricCheckInPage() {
    var authenticationState by remember { mutableStateOf<AuthenticationState>(AuthenticationState.Pending) }

    val context = LocalContext.current
    val activity = context as? FragmentActivity

    if (activity != null) {
        val executor: Executor = ContextCompat.getMainExecutor(context)
        val biometricPrompt = remember {
            BiometricPrompt(
                activity,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        authenticationState = AuthenticationState.Success
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        authenticationState = AuthenticationState.Failed
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        authenticationState = AuthenticationState.Error(errString.toString())
                    }
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Biometric Check-In", style = MaterialTheme.typography.headlineLarge, color = Color(0xFFDC143C))
            Spacer(modifier = Modifier.height(32.dp))

            when (authenticationState) {
                is AuthenticationState.Pending -> {
                    Text("Biometric Authentication Pending", style = MaterialTheme.typography.bodyLarge, color = Color(0xFFDC143C))
                    Button(onClick = { startBiometricAuthentication(biometricPrompt) }) {
                        Text("Start Authentication", color = Color.White)
                    }
                }
                is AuthenticationState.Success -> {
                    Text("Authentication Succeeded ", style = MaterialTheme.typography.bodyLarge, color = Color(0xFFDC143C))
                }
                is AuthenticationState.Failed -> {
                    Text("Authentication Failed", style = MaterialTheme.typography.bodyLarge, color = Color(0xFFDC143C))
                    Button(onClick = { startBiometricAuthentication(biometricPrompt) }) {
                        Text("Retry Authentication", color = Color.White)
                    }
                }
                is AuthenticationState.Error -> {
                    Text("Error: ${(authenticationState as AuthenticationState.Error).message}", style = MaterialTheme.typography.bodyLarge, color = Color(0xFFDC143C))
                }
            }
        }
    } else {
        Text("Biometric authentication not supported in this context.", style = MaterialTheme.typography.bodyLarge, color = Color(0xFFDC143C))
    }
}

fun startBiometricAuthentication(biometricPrompt: BiometricPrompt) {
    val biometricPromptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometric Authentication")
        .setSubtitle("Log in using your biometric data")
        .setNegativeButtonText("Cancel")
        .build()

    biometricPrompt.authenticate(biometricPromptInfo)
}

sealed class AuthenticationState {
    data object Pending : AuthenticationState()
    data object Success : AuthenticationState()
    data object Failed : AuthenticationState()
    data class Error(val message: String) : AuthenticationState()
}

@Composable
fun JourneyDetailsPage() {

    val context = LocalContext.current
    var errorMessage by remember { mutableStateOf("") }
    var TicketInfo by remember { mutableStateOf<TicketInfo?>(null) }

    fun getTicket() {
        Log.d("Journey details", "getTicket called ${UserSession.pnr_clicked}")
        RetrofitClient.instance.getTicket(pnr_num = UserSession.pnr_clicked.toString())
            .enqueue(object : Callback<TicketInfo> {
                override fun onFailure(call: Call<TicketInfo>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                    Log.e("Journey details", t.message.toString())
                }

                override fun onResponse(call: Call<TicketInfo>, response: Response<TicketInfo>) {
                    if (response.isSuccessful) {
                        TicketInfo = TicketInfo(
                            pnr_number = response.body()?.pnr_number.toString(),
                            boarding_point = response.body()?.boarding_point.toString(),
                            droping_point = response.body()?.droping_point.toString(),
                            train_time = response.body()?.train_time.toString()
                        )
                    } else {
                        // Handle API error
                    }
                }

            })

    }
    LaunchedEffect(Unit) {
        getTicket()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Journey Details", style = MaterialTheme.typography.headlineLarge, color = Color(0xFFDC143C))
        Spacer(modifier = Modifier.height(32.dp))
        // Additional journey details can be added here

        Text("Train No: ${TicketInfo?.pnr_number}", style = MaterialTheme.typography.bodyLarge)
        Text(
            "Boarding Point: ${TicketInfo?.boarding_point}",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            "Dropping Point: ${TicketInfo?.droping_point}",
            style = MaterialTheme.typography.bodyLarge
        )
        Text("Train Time: ${TicketInfo?.train_time}", style = MaterialTheme.typography.bodyLarge)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TrainCheckInApp()
}