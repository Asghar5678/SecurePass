package tees.mad.s3345558

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onStrengthCheckClick: () -> Unit = {},
    onBreachCheckClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Secure Pass",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color(0xFF1B5E20)
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
        ) {

            // ---------------- HEADER SUMMARY CARD ----------------
            SummaryCard()

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Password Tools",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ---------------- STRENGTH CHECKER CARD ----------------
            FeatureCard(
                title = "Password Strength Checker",
                subtitle = "Check strength, predictability & score",
                icon = Icons.Default.Security,
                onClick = onStrengthCheckClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ---------------- BREACH CHECK CARD ----------------
            FeatureCard(
                title = "Password Breach Check",
                subtitle = "Check if your password was leaked (HIBP)",
                icon = Icons.Default.Warning,
                onClick = onBreachCheckClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ---------------- HISTORY CARD ----------------
            FeatureCard(
                title = "History",
                subtitle = "View encrypted password check history",
                icon = Icons.Default.History,
                onClick = onHistoryClick
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Secure. Private. Offline First.",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.Gray,
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic
            )
        }
    }
}

@Composable
fun SummaryCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8F5E9)
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                "Your Security Summary",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B5E20)
            )

            Spacer(modifier = Modifier.height(12.dp))

            SummaryRow(label = "Last strength check:", value = "Strong")
            Spacer(modifier = Modifier.height(6.dp))
            SummaryRow(label = "Last breach check:", value = "Safe")
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 14.sp)
        Text(value, fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))
    }
}

@Composable
fun FeatureCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(36.dp),
                tint = Color(0xFF2E7D32)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(subtitle, color = Color.Gray, fontSize = 13.sp)
            }
        }
    }
}
