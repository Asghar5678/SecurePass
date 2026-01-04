package tees.mad.s3345558

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    onLogout: () -> Unit
) {
    val context = LocalContext.current

    val name = UserAccountData.getName(context)
    val email = UserAccountData.getEmail(context)
    val country = UserAccountData.getCountry(context)

    var showChangePasswordDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("My Profile", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Profile Pic",
                            tint = Color(0xFF1B5E20),
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                        )


                    Spacer(Modifier.height(12.dp))

                    Text(name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(email, fontSize = 14.sp, color = Color.Gray)
                    Text(country, fontSize = 14.sp, color = Color.Gray)
                }
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { showChangePasswordDialog = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.Lock, null)
                Spacer(Modifier.width(8.dp))
                Text("Change Password")
            }

            Spacer(Modifier.height(16.dp))

            OutlinedButton(
                onClick = {
                    SecurePrefs.clearLogin(context)
                    onLogout()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
            ) {
                Icon(Icons.Default.Logout, null)
                Spacer(Modifier.width(8.dp))
                Text("Logout")
            }
        }
    }

    if (showChangePasswordDialog) {
        ChangePasswordDialog { showChangePasswordDialog = false }
    }
}


@Composable
fun ChangePasswordDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    val strength = getPasswordStrength(newPassword)
    val animatedStrength = animateFloatAsState(strength.first, label = "")

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                when {
                    oldPassword != SecurePrefs.getPassword(context) ->
                        error = "Old password is incorrect"

                    newPassword.length < 8 ->
                        error = "Password must be at least 8 characters"

                    newPassword != confirmPassword ->
                        error = "Passwords do not match"

                    else -> {
                        SecurePrefs.updatePassword(context, newPassword)
                        Toast.makeText(context, "Password updated", Toast.LENGTH_SHORT).show()
                        onDismiss()
                    }
                }
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text("Change Password", fontWeight = FontWeight.Bold) },
        text = {
            Column {

                OutlinedTextField(
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    label = { Text("Old Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("New Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = animatedStrength.value,
                    color = getStrengthColor(strength.first),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                )

                Text(
                    strength.second,
                    color = getStrengthColor(strength.first),
                    fontSize = 13.sp
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                if (error.isNotEmpty()) {
                    Spacer(Modifier.height(6.dp))
                    Text(error, color = Color.Red, fontSize = 13.sp)
                }
            }
        }
    )
}





object SecurePrefs {

    private const val PREF_NAME = "secure_pass_encrypted"

    private fun prefs(context: Context) =
        EncryptedSharedPreferences.create(
            PREF_NAME,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    fun getName(context: Context) =
        prefs(context).getString("name", "") ?: ""

    fun getEmail(context: Context) =
        prefs(context).getString("email", "") ?: ""

    fun getCountry(context: Context) =
        prefs(context).getString("country", "") ?: ""

    fun getPassword(context: Context) =
        prefs(context).getString("password", "") ?: ""

    fun updatePassword(context: Context, newPassword: String) {
        prefs(context).edit().putString("password", newPassword).apply()
    }

    fun clearLogin(context: Context) {
        prefs(context).edit().clear().apply()
    }
}



