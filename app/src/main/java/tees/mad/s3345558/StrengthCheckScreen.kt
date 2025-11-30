package tees.mad.s3345558

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StrengthCheckScreen(
    onBack: () -> Unit = {}
) {

    var password by remember { mutableStateOf("") }
    var passwordStrength by remember { mutableStateOf(0f) }
    var strengthLabel by remember { mutableStateOf("") }
    var predictableWarning by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Password Strength Check", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Enter a password to analyse its strength:",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ---------------- PASSWORD INPUT ----------------
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = {
                    password = it

                    val result = getPasswordStrength(password)
                    passwordStrength = result.first
                    strengthLabel = result.second

                    predictableWarning = if (isPredictablePassword(password))
                        "⚠ Your password is very predictable. Avoid patterns or common passwords."
                    else ""
                },
                label = { Text("Enter Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ---------------- STRENGTH METER ----------------
            if (password.isNotEmpty()) {

                LinearProgressIndicator(
                    progress = passwordStrength,
                    color = getStrengthColor(passwordStrength),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(Color.LightGray, RoundedCornerShape(4.dp))
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = strengthLabel,
                    color = getStrengthColor(passwordStrength),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                if (predictableWarning.isNotEmpty()) {
                    Text(
                        predictableWarning,
                        color = Color.Red,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Password Requirements", fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(8.dp))

                RequirementRow("At least 8 characters", password.length >= 8)
                RequirementRow("Contains uppercase letter", password.any { it.isUpperCase() })
                RequirementRow("Contains lowercase letter", password.any { it.isLowerCase() })
                RequirementRow("Contains number", password.any { it.isDigit() })
                RequirementRow("Contains symbol", password.any { !it.isLetterOrDigit() })
            }
        }
    }
}

@Composable
fun RequirementRow(text: String, isValid: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = if (isValid) Icons.Default.Check else Icons.Default.Close,
            contentDescription = null,
            tint = if (isValid) Color(0xFF2E7D32) else Color.Red
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, color = if (isValid) Color.Black else Color.DarkGray)
    }
}
