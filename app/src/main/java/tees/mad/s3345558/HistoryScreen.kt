package tees.mad.s3345558

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController
) {
    val context = LocalContext.current
    var history by remember { mutableStateOf(HistoryPrefs.getHistory(context)) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("History", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                },
                actions = {
                    if (history.isNotEmpty()) {
                        IconButton(onClick = {
                            HistoryPrefs.clearHistory(context)
                            history = emptyList()
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Clear History")
                        }
                    }
                }
            )
        }
    ) { padding ->

        if (history.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No history yet.\nCheck a password to see results here.",
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                itemsIndexed(history.reversed()) { index, item ->
                    HistoryCard(
                        item = item,
                        onDelete = {
                            HistoryPrefs.deleteItem(context, item)
                            history = HistoryPrefs.getHistory(context)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryCard(
    item: HistoryItem,
    onDelete: () -> Unit
) {

    val date = SimpleDateFormat(
        "dd MMM yyyy • hh:mm a",
        Locale.getDefault()
    ).format(Date(item.timestamp))

    val strengthColor = when (item.strength) {
        "Strong" -> Color(0xFF2E7D32)
        "Medium" -> Color(0xFFFFA000)
        else -> Color.Red
    }

    val breachColor = if (item.breachStatus == "Safe")
        Color(0xFF2E7D32)
    else
        Color(0xFFD32F2F)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    Icons.Default.VpnKey,
                    contentDescription = null,
                    tint = Color(0xFF1B5E20)
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = if (item.label.isNotEmpty()) item.label else "Unlabeled Password",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }
            }

            Spacer(Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Schedule,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(date, fontSize = 13.sp, color = Color.Gray)
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Security,
                        contentDescription = null,
                        tint = strengthColor
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        item.strength,
                        fontWeight = FontWeight.Bold,
                        color = strengthColor
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = breachColor
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        item.breachStatus,
                        fontWeight = FontWeight.Bold,
                        color = breachColor
                    )
                }
            }
        }
    }
}


data class HistoryItem(
    val label: String,
    val timestamp: Long,
    val strength: String,
    val breachStatus: String
)


object HistoryPrefs {

    private const val PREF_NAME = "secure_pass_history"

    private fun prefs(context: Context) =
        EncryptedSharedPreferences.create(
            PREF_NAME,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    fun saveHistory(
        context: Context,
        label: String,
        strength: String,
        breachStatus: String
    ) {
        val history = getHistory(context).toMutableList()

        history.add(
            HistoryItem(
                label = label,
                timestamp = System.currentTimeMillis(),
                strength = strength,
                breachStatus = breachStatus
            )
        )

        prefs(context).edit()
            .putString("history", Gson().toJson(history))
            .apply()
    }

    fun getHistory(context: Context): List<HistoryItem> {
        val json = prefs(context).getString("history", null) ?: return emptyList()
        val type = object : TypeToken<List<HistoryItem>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun deleteItem(context: Context, item: HistoryItem) {
        val updated = getHistory(context)
            .filterNot { it.timestamp == item.timestamp }

        prefs(context).edit()
            .putString("history", Gson().toJson(updated))
            .apply()
    }

    fun clearHistory(context: Context) {
        prefs(context).edit().remove("history").apply()
    }
}
