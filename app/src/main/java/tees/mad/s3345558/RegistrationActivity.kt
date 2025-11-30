package tees.mad.s3345558

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.firebase.database.FirebaseDatabase


@Composable
fun RegistrationScreen(navController: NavController) {
    var fullname by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var messageText by remember { mutableStateOf("") }


    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var confirmpassword by remember { mutableStateOf("") }

    val context = LocalContext.current.findActivity()

    var passwordStrength by remember { mutableStateOf(0f) }
    var strengthLabel by remember { mutableStateOf("") }

    var predictableWarning by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.lite_green))
    ) {

        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            painter = painterResource(id = R.drawable.ic_securepass),
            contentDescription = "Secure Pass",
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(color = colorResource(id = R.color.lite_green))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(listOf(Color.Gray, Color.Gray)),
                        shape = RoundedCornerShape(16.dp)
                    ),
                value = fullname,
                onValueChange = { fullname = it },
                label = { Text("Enter FullName") }
            )

            Spacer(modifier = Modifier.height(6.dp))


            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(listOf(Color.Gray, Color.Gray)),
                        shape = RoundedCornerShape(16.dp)
                    ),
                value = email,
                onValueChange = { email = it },
                label = { Text("Enter Your Email") }
            )

            Spacer(modifier = Modifier.height(6.dp))

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(listOf(Color.Gray, Color.Gray)),
                        shape = RoundedCornerShape(16.dp)
                    ),
                value = country,
                onValueChange = { country = it },
                label = { Text("Enter Your Country") }
            )

            Spacer(modifier = Modifier.height(6.dp))

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(listOf(Color.Gray, Color.Gray)),
                        shape = RoundedCornerShape(16.dp)
                    ),
                value = password,
                onValueChange = {
                    password = it
                    val result = getPasswordStrength(password)
                    passwordStrength = result.first
                    strengthLabel = result.second

                    predictableWarning = if (isPredictablePassword(password)) {
                        "⚠ Your password is too predictable. Choose something harder."
                    } else ""
                },
                label = { Text("Enter Your Password") }
            )


            Spacer(modifier = Modifier.height(6.dp))

            if (password.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = passwordStrength,
                    color = getStrengthColor(passwordStrength),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .background(Color.LightGray, RoundedCornerShape(4.dp))
                )

                Text(
                    text = strengthLabel,
                    color = getStrengthColor(passwordStrength),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            if (predictableWarning.isNotEmpty()) {
                Text(
                    text = predictableWarning,
                    color = Color.Red,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }



            Spacer(modifier = Modifier.height(6.dp))

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(listOf(Color.Gray, Color.Gray)),
                        shape = RoundedCornerShape(16.dp)
                    ),
                value = confirmpassword,
                onValueChange = { confirmpassword = it },
                label = { Text("Confirm Password") }
            )



            Spacer(modifier = Modifier.height(24.dp))
            if (messageText.isNotEmpty()) {
                Text(
                    text = messageText,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = {

                    if (fullname.isEmpty()) {
                        Toast.makeText(context, "Enter Full Name", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (email.isEmpty()) {
                        Toast.makeText(context, "Enter Full Email", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (country.isEmpty()) {
                        Toast.makeText(context, "Enter Country", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (password.isEmpty()) {
                        Toast.makeText(context, "Enter Password", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val userData = UserData(
                        name = fullname,
                        email = email,
                        country = country,
                        password = password
                    )


                    val db = FirebaseDatabase.getInstance()
                    val ref = db.getReference("AuthUsers")
                    ref.child(userData.email.replace(".", ",")).setValue(userData)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()

                                navController.navigate(NavScreens.Login.route){
                                    popUpTo(NavScreens.Register.route) { inclusive = true }
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "User Registration Failed: ${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(
                                context,
                                "User Registration Failed: ${exception.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }


                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.PureWhite),
                    contentColor = colorResource(
                        id = R.color.text_color
                    )
                )
            ) {
                Text(text = "Sign Up", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "You are an old user ?", fontSize = 14.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Sign In",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.PureWhite),
                    modifier = Modifier.clickable {
                        navController.navigate(NavScreens.Login.route) {
                            popUpTo(NavScreens.Register.route) { inclusive = true }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

        }

    }
}


@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    RegistrationScreen(navController = NavHostController(LocalContext.current))
}

fun getPasswordStrength(password: String): Pair<Float, String> {
    if (password.isEmpty()) return 0f to ""

    val length = password.length >= 8
    val hasUpper = password.any { it.isUpperCase() }
    val hasLower = password.any { it.isLowerCase() }
    val hasDigit = password.any { it.isDigit() }
    val hasSpecial = password.any { !it.isLetterOrDigit() }

    if (!length) {
        return 0.33f to "Weak (Min 8 characters)"
    }

    if (length && ((hasUpper || hasLower) && hasDigit || hasSpecial)) {
        return 0.66f to "Medium"
    }

    if (length && hasUpper && hasLower && hasDigit && hasSpecial && password.length >= 10) {
        return 1f to "Strong"
    }

    return 0.66f to "Medium"
}


fun getStrengthColor(strength: Float): Color {
    return when (strength) {
        0.33f -> Color.Red
        0.66f -> Color(0xFFFFA000) // Amber
        1f -> Color(0xFF2E7D32)    // Green
        else -> Color.Transparent
    }
}


fun isPredictablePassword(password: String): Boolean {
    if (password.isEmpty()) return false

    val commonPasswords = listOf(
        "123456", "12345678", "123456789", "password", "admin", "qwerty",
        "iloveyou", "welcome", "abc123", "monkey", "dragon", "letmein",
        "000000", "111111", "112233", "password1", "qwerty123"
    )

    // 1) Common passwords
    if (commonPasswords.contains(password.lowercase())) return true

    // 2) Repeated characters (e.g., "aaaaaa", "111111")
    if (password.all { it == password[0] }) return true

    // 3) Sequential numbers or letters
    val sequences = listOf(
        "abcdefghijklmnopqrstuvwxyz",
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
        "0123456789"
    )

    val lowered = password.lowercase()

    for (seq in sequences) {
        if (seq.contains(lowered)) return true
        if (seq.reversed().contains(lowered)) return true
    }

    // 4) Keyboard patterns
    val keyboardPatterns = listOf("qwerty", "asdf", "zxcv", "qaz", "wsx")
    if (keyboardPatterns.any { lowered.contains(it) }) return true

    return false
}
