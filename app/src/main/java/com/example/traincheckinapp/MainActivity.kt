package com.example.traincheckinapp

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import com.example.traincheckinapp.models.Login
import com.example.traincheckinapp.models.APIResponse
import com.example.traincheckinapp.models.LoginResponse
import com.example.traincheckinapp.models.SignUpResponse
import com.example.traincheckinapp.models.User


import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// MainActivity that sets up the Composables
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
                title = { Text("Train Check-In App") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF6200EE))
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
            composable("forgot_password") { ForgotPasswordPage(navController) }
            composable("check_in") { CheckInPage(navController) }
            composable("pnr_list") { PNRListPage(navController) }
            composable("biometric_check_in") { BiometricCheckInPage(navController) }
            composable("otp_check_in") { OTPCheckInPage(navController) }
            composable("journey_details") { JourneyDetailsPage(navController) }
            composable("passenger_care") { PassengerCarePage(navController) }
        }
    }
}

@Composable
fun PassengerCarePage(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF3F4F6)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Passenger Care",
            style = MaterialTheme.typography.headlineLarge,
            color = Color(0xFF6200EE)
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Add content for passenger care details here
        Text("Contact Us: +1234567890", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Email: passenger.care@example.com", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun ForgotPasswordPage(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }

    fun sendPasswordResetEmail() {
        if (email.isEmpty()) {
            emailError = "Please enter your email address."
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Please enter a valid email address."
        } else {
            // Simulate sending password reset link
            emailError = ""
            message = "Password reset link has been sent to your email."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF3F4F6)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Forgot Password",
            style = MaterialTheme.typography.headlineLarge,
            color = Color(0xFF6200EE)
        )
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
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
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
            color = Color(0xFF6200EE),
            modifier = Modifier.clickable {
                navController.navigate("passenger_care")
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
            .background(Color(0xFFF3F4F6)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Create Account",
            style = MaterialTheme.typography.headlineLarge,
            color = Color(0xFF6200EE)
        )
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

        // Add Mobile Number field
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

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

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
                    // Check for which field is invalid and show specific error messages
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
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up", color = Color.White)
        }
    }
}


@Composable
fun LoginPage(navController: NavHostController) {

    val context = LocalContext.current;

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
            .background(Color(0xFFF3F4F6)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Welcome Back!",
            style = MaterialTheme.typography.headlineLarge,
            color = Color(0xFF6200EE)
        )
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
            Text(
                text = passwordError,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {


                if (email.isNotEmpty() && password.isNotEmpty() ) {


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
                                    Log.d("Login", response.body()?.message.toString())
                                    navController.navigate("check_in")
                                }
                            }
                        })
                } else {
                    errorMessage = "Invalid email or password. Please try again."
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { navController.navigate("signup") },
            content = { Text("Create an Account") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { navController.navigate("forgot_password") },
            content = { Text("Forgot Password?") }
        )
    }
}

@Composable
fun OTPCheckInPage(navController: NavHostController) {
    var otp by remember { mutableStateOf("") }
    var otpStatus by remember { mutableStateOf("OTP Pending...") }
    var isGeofenceEntered by remember { mutableStateOf(false) }
    var generatedOTP by remember { mutableStateOf("") }

    // Simulate OTP generation
    fun generateOTP(): String {
        return (100000..999999).random().toString()
    }

    val geofencingClient = LocationServices.getGeofencingClient(LocalContext.current)
    val geofenceRadius = 100f // 100 meters
    val geofenceLatitude = 12.9716 // Example coordinates
    val geofenceLongitude = 77.5946 // Example coordinates

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

    val intent = Intent(LocalContext.current, GeofenceService::class.java)
    val pendingIntent =
        PendingIntent.getService(LocalContext.current, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

//    geofencingClient.addGeofences(geofencingRequest, pendingIntent)
//        .addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                Log.d("Geofence", "Geofence added successfully")
//            } else {
//                Log.e("Geofence", "Failed to add geofence")
//            }
//        }

    if (isGeofenceEntered) {
        // Generate OTP and update status
        generatedOTP = generateOTP()

        fun verifyOTP() {
            otpStatus = if (otp == generatedOTP) {
                "OTP Verified Successfully!"
            } else {
                "Invalid OTP"
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color(0xFFF3F4F6)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "OTP Check-In",
                style = MaterialTheme.typography.headlineLarge,
                color = Color(0xFF6200EE)
            )
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = otp,
                onValueChange = { otp = it },
                label = { Text("Enter OTP") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { verifyOTP() }),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(Color.White)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { verifyOTP() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Verify OTP", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                otpStatus,
                color = if (otpStatus.contains("Successfully")) Color.Green else Color.Red
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color(0xFFF3F4F6)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "OTP Check-In",
                style = MaterialTheme.typography.headlineLarge,
                color = Color(0xFF6200EE)
            )
            Spacer(modifier = Modifier.height(32.dp))

            Text("You are not inside the geofenced area. Please enter the designated area to proceed.")
        }
    }
}

class GeofenceService {

}

@Composable
fun CheckInPage(navController: NavHostController) {
    var checkInStatus by remember { mutableStateOf("Check-In Pending...") }

    fun performCheckIn() {
        checkInStatus = "Check-In Successful! Welcome aboard."
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF3F4F6)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Check-In", style = MaterialTheme.typography.headlineLarge, color = Color(0xFF6200EE))
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { performCheckIn() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Check-In", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            checkInStatus,
            color = if (checkInStatus.contains("Successful")) Color.Green else Color.Red
        )
    }
}

@Composable
fun PNRListPage(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF3F4F6)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("PNR List", style = MaterialTheme.typography.headlineLarge, color = Color(0xFF6200EE))
        Spacer(modifier = Modifier.height(32.dp))

        // Add your list of PNRs here
        Text("PNR Number: 1234567890", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun BiometricCheckInPage(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF3F4F6)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Biometric Check-In",
            style = MaterialTheme.typography.headlineLarge,
            color = Color(0xFF6200EE)
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Add your biometric check-in logic here
        Text("Biometric Authentication Pending", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun JourneyDetailsPage(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF3F4F6)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Journey Details",
            style = MaterialTheme.typography.headlineLarge,
            color = Color(0xFF6200EE)
        )
        Spacer(modifier = Modifier.height(32.dp))

        Text("Train No: 12345", style = MaterialTheme.typography.bodyLarge)
        Text("Departure: 10:00 AM", style = MaterialTheme.typography.bodyLarge)
        Text("Arrival: 1:00 PM", style = MaterialTheme.typography.bodyLarge)
    }
}

