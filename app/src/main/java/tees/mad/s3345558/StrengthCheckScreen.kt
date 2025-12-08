package tees.mad.s3345558

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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

    var passwordVisible by remember { mutableStateOf(false) }

    // Smooth animations
    val animatedProgress = animateFloatAsState(targetValue = passwordStrength, label = "")
    val animatedColor = animateColorAsState(
        targetValue = getStrengthColor(passwordStrength),
        label = ""
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Strength Checker",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
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
                .padding(padding)
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color(0xFFE8F5E9),
                            Color(0xFFF4FDF6)
                        )
                    )
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // -------------------- Header Card --------------------
            PremiumCard {
                Text(
                    "Create a strong and unpredictable password.",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = Color(0xFF1B5E20)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // -------------------- Password Field --------------------
            PremiumCard {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password,
                    onValueChange = {
                        password = it

                        val result = getPasswordStrength(password)
                        passwordStrength = result.first
                        strengthLabel = result.second

                        predictableWarning = if (isPredictablePassword(password))
                            "⚠ Password is predictable. Avoid common patterns."
                        else ""
                    },
                    label = { Text("Enter Password") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = null
                            )
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // -------------------- Strength Meter --------------------
            if (password.isNotEmpty()) {

                PremiumCard {

                    Text(
                        text = strengthLabel,
                        color = animatedColor.value,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LinearProgressIndicator(
                        progress = animatedProgress.value,
                        color = animatedColor.value,
                        trackColor = Color(0xFFEEEEEE),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )

                    if (predictableWarning.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            predictableWarning,
                            color = Color.Red,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // -------------------- Requirement Checklist --------------------
                PremiumCard {
                    Text(
                        "Password Requirements",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    RequirementRow("At least 8 characters", password.length >= 8)
                    RequirementRow("Uppercase letter", password.any { it.isUpperCase() })
                    RequirementRow("Lowercase letter", password.any { it.isLowerCase() })
                    RequirementRow("Number", password.any { it.isDigit() })
                    RequirementRow("Symbol", password.any { !it.isLetterOrDigit() })
                }
            }
        }
    }
}


// -------------------- Premium Glass Card Component --------------------
@Composable
fun PremiumCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color.Black.copy(alpha = 0.12f),
                spotColor = Color.Black.copy(alpha = 0.12f)
            )
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color.White.copy(alpha = 0.75f),
                        Color.White.copy(alpha = 0.95f)
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.5f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(18.dp)
    ) {
        content()
    }
}


// -------------------- Requirement Row --------------------
@Composable
fun RequirementRow(text: String, isValid: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 6.dp)
    ) {
        Icon(
            imageVector = if (isValid) Icons.Default.Check else Icons.Default.Close,
            contentDescription = null,
            tint = if (isValid) Color(0xFF2E7D32) else Color.Red
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text,
            fontSize = 15.sp,
            color = if (isValid) Color.Black else Color.Gray
        )
    }
}
