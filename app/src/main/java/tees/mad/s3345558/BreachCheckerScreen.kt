package tees.mad.s3345558


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.math.BigInteger
import java.security.MessageDigest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordBreachCheckScreen(
    onBack: () -> Unit = {}
) {

    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var breachCount by remember { mutableStateOf<Int?>(null) }
    var errorMessage by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Password Breach Check",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
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
                .padding(16.dp)
        ) {

            // ---------------- Premium Header ----------------
            PremiumCardBD {
                Text(
                    "Find out if your password has been exposed in any known data breaches.",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = Color(0xFF1B5E20)
                )
            }

            Spacer(Modifier.height(20.dp))

            // ---------------- Password Input ----------------
            PremiumCardBD {
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        breachCount = null
                        errorMessage = ""
                    },
                    singleLine = true,
                    label = { Text("Enter Password") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = null)
                        }
                    }
                )
            }

            Spacer(Modifier.height(20.dp))

            // ---------------- Check Button ----------------
            Button(
                onClick = {
                    if (password.isEmpty()) {
                        errorMessage = "Password cannot be empty."
                        return@Button
                    }

                    isLoading = true
                    breachCount = null
                    errorMessage = ""

                    scope.launch(Dispatchers.IO) {
                        try {
                            val sha1 = sha1(password).uppercase()
                            val prefix = sha1.substring(0, 5)
                            val suffix = sha1.substring(5)

                            val response = HIBPApi.service.checkPassword(prefix)

                            val match = response.lines().find { it.startsWith(suffix) }

                            breachCount = match?.split(":")?.get(1)?.toIntOrNull() ?: 0
                        } catch (e: Exception) {
                            errorMessage = "Network or API error occurred."
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Check Breach Status", fontSize = 18.sp)
            }

            Spacer(Modifier.height(22.dp))

            // ---------------- Loading ----------------
            if (isLoading) {
                PremiumCardBD {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(28.dp))
                        Spacer(Modifier.width(12.dp))
                        Text("Checking breach database...", fontSize = 16.sp)
                    }
                }
            }

            // ---------------- Error ----------------
            if (errorMessage.isNotEmpty()) {
                PremiumCardBD {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Error, contentDescription = null, tint = Color.Red)
                        Spacer(Modifier.width(8.dp))
                        Text(errorMessage, color = Color.Red, fontSize = 16.sp)
                    }
                }
            }

            // ---------------- Result ----------------
            breachCount?.let { count ->
                Spacer(Modifier.height(12.dp))

                val cardColor =
                    if (count > 0) Color(0xFFD32F2F) else Color(0xFF2E7D32)

                PremiumCardBD {
                    Text(
                        text = if (count > 0) "⚠ Password Found in Breaches!" else "✔ Safe Password",
                        fontWeight = FontWeight.Bold,
                        color = cardColor,
                        fontSize = 20.sp
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = if (count > 0)
                            "Your password appeared $count times in known breaches.\nPlease change it immediately."
                        else
                            "This password was NOT found in any known breaches.\nIt is safe to use.",
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PremiumCardBD(content: @Composable ColumnScope.() -> Unit) {
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
            .padding(20.dp)
    ) {
        content()
    }
}



// ------------------------------------------------------------
// Breach Result UI Component
// ------------------------------------------------------------
@Composable
fun BreachResultCard(title: String, color: Color, message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, color = color, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))
            Text(message, color = Color.Black, fontSize = 15.sp)
        }
    }
}


// ------------------------------------------------------------
// SHA-1 Hash Function (Local Only)
// ------------------------------------------------------------
fun sha1(input: String): String {
    val md = MessageDigest.getInstance("SHA-1")
    val digest = md.digest(input.toByteArray(Charsets.UTF_8))
    val bigInt = BigInteger(1, digest)
    return bigInt.toString(16).padStart(40, '0')
}


// ------------------------------------------------------------
// Retrofit API (HIBP k-Anonymity)
// ------------------------------------------------------------
interface HIBPService {
    @GET("range/{prefix}")
    suspend fun checkPassword(@Path("prefix") prefix: String): String
}

object HIBPApi {
    private const val BASE_URL = "https://api.pwnedpasswords.com/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val service: HIBPService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(HIBPService::class.java)
    }
}
