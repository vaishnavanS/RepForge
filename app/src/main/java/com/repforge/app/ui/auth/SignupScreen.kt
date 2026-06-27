package com.repforge.app.ui.auth

<<<<<<< HEAD
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
=======
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
>>>>>>> 72daa16 (Modified for better version)
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
<<<<<<< HEAD
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
=======
import androidx.compose.ui.graphics.Brush
>>>>>>> 72daa16 (Modified for better version)
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SignupScreen(
    onSignupSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

<<<<<<< HEAD
    val NavyBlue = Color(0xFF000080)
    val LightBg = Color(0xFFF0F4FF)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBg)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 28.dp)
            .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        // Logo — same as LoginScreen
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(NavyBlue),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "RF",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "REPFORGE",
            fontSize = 30.sp,
            fontWeight = FontWeight.Black,
            color = NavyBlue,
            letterSpacing = 4.sp
        )
        Text(
            text = "Intelligent Strength Assistant",
            fontSize = 13.sp,
            color = NavyBlue.copy(alpha = 0.5f),
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        // White card — same style as LoginScreen
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(24.dp)
        ) {
            Column {
                Text(
                    "Create Account",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NavyBlue
                )
                Text(
                    "Join and start your strength journey",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Full Name
                Text(
                    "FULL NAME",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyBlue.copy(alpha = 0.6f),
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Your full name", color = Color.Gray) },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = null,
                            tint = NavyBlue.copy(alpha = 0.5f)
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NavyBlue,
                        unfocusedBorderColor = Color.LightGray,
                        focusedTextColor = NavyBlue,
                        unfocusedTextColor = Color.DarkGray
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Email
                Text(
                    "EMAIL",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyBlue.copy(alpha = 0.6f),
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("your@email.com", color = Color.Gray) },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Email,
                            contentDescription = null,
                            tint = NavyBlue.copy(alpha = 0.5f)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NavyBlue,
                        unfocusedBorderColor = Color.LightGray,
                        focusedTextColor = NavyBlue,
                        unfocusedTextColor = Color.DarkGray
=======
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surface
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f)),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Create Account",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
>>>>>>> 72daa16 (Modified for better version)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

<<<<<<< HEAD
                // Password
                Text(
                    "PASSWORD",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyBlue.copy(alpha = 0.6f),
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Min 6 characters", color = Color.Gray) },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Lock,
                            contentDescription = null,
                            tint = NavyBlue.copy(alpha = 0.5f)
                        )
                    },
                    trailingIcon = {
                        TextButton(onClick = { passwordVisible = !passwordVisible }) {
                            Text(
                                if (passwordVisible) "Hide" else "Show",
                                fontSize = 12.sp,
                                color = NavyBlue
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NavyBlue,
                        unfocusedBorderColor = Color.LightGray,
                        focusedTextColor = NavyBlue,
                        unfocusedTextColor = Color.DarkGray
=======
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
>>>>>>> 72daa16 (Modified for better version)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

<<<<<<< HEAD
                // Confirm Password
                Text(
                    "CONFIRM PASSWORD",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyBlue.copy(alpha = 0.6f),
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Repeat password", color = Color.Gray) },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Lock,
                            contentDescription = null,
                            tint = NavyBlue.copy(alpha = 0.5f)
                        )
                    },
                    trailingIcon = {
                        TextButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Text(
                                if (confirmPasswordVisible) "Hide" else "Show",
                                fontSize = 12.sp,
                                color = NavyBlue
                            )
                        }
                    },
                    visualTransformation = if (confirmPasswordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NavyBlue,
                        unfocusedBorderColor = Color.LightGray,
                        focusedTextColor = NavyBlue,
                        unfocusedTextColor = Color.DarkGray
                    )
                )

                // Error message
                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFFEBEE))
                            .padding(12.dp)
                    ) {
                        Text(
                            errorMessage!!,
                            color = Color(0xFFB00020),
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Sign up button
=======
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

>>>>>>> 72daa16 (Modified for better version)
                Button(
                    onClick = {
                        viewModel.signup(
                            name = fullName,
                            email = email,
                            pass = password,
                            confirm = confirmPassword,
                            onSuccess = onSignupSuccess,
                            onError = { errorMessage = it }
                        )
                    },
<<<<<<< HEAD
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NavyBlue
                    )
                ) {
                    Text(
                        "CREATE ACCOUNT",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        letterSpacing = 2.sp,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Already have account
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "Already have an account? ",
                color = Color.Gray,
                fontSize = 14.sp
            )
            TextButton(onClick = onNavigateToLogin) {
                Text(
                    "Sign in",
                    color = NavyBlue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
=======
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("SIGN UP")
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = onNavigateToLogin) {
                    Text("Already have an account? Login")
                }
            }
>>>>>>> 72daa16 (Modified for better version)
        }
    }
}
