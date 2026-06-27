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
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToSignup: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

<<<<<<< HEAD
    val NavyBlue = Color(0xFF000080)
    val LightBg = Color(0xFFF0F4FF)

    // ✅ Entire screen scrollable — fixes "Don't have an account" cut off
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

        // Logo
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

        // White card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(24.dp)
        ) {
            Column {
                Text(
                    "Welcome back",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NavyBlue
                )
                Text(
                    "Sign in to continue your journey",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

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
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

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
                    placeholder = { Text("••••••••", color = Color.Gray) },
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

                // Sign in button
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
                    text = "REPFORGE",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Enter the future of training",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 24.dp)
                )

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
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

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
                        viewModel.login(
                            email = email,
                            pass = password,
                            onSuccess = onLoginSuccess,
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
                        "SIGN IN",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        letterSpacing = 2.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = { },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        "Forgot Password?",
                        color = NavyBlue.copy(alpha = 0.7f),
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ✅ This is now always visible — not cut off
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "Don't have an account? ",
                color = Color.Gray,
                fontSize = 14.sp
            )
            TextButton(onClick = onNavigateToSignup) {
                Text(
                    "Create one",
                    color = NavyBlue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
=======
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("LOGIN")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onNavigateToSignup) {
                        Text("Create Account")
                    }
                    TextButton(onClick = { /* TODO: Forgot Password */ }) {
                        Text("Forgot Password?")
                    }
                }
>>>>>>> 72daa16 (Modified for better version)
            }
        }
    }
}
