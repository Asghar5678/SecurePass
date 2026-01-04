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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import tees.mad.s3345558.ui.theme.LiteGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onStrengthCheckClick: () -> Unit = {},
    onBreachCheckClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onProfileClicked: () -> Unit = {}
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var lastStrength by remember {
        mutableStateOf(SecurityPrefs.getLastStrengthStatus(context))
    }
    var lastBreach by remember {
        mutableStateOf(SecurityPrefs.getLastBreachStatus(context))
    }

    LaunchedEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                lastStrength = SecurityPrefs.getLastStrengthStatus(context)
                lastBreach = SecurityPrefs.getLastBreachStatus(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
    }

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
                navigationIcon = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onProfileClicked) {
                            Icon(
                                Icons.Filled.AccountCircle,
                                null,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color(0xFF1B5E20)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = LiteGreen)
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            // ✅ PASS VALUES TO SUMMARY CARD
            SummaryCard(
                lastStrength = lastStrength,
                lastBreach = lastBreach
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Password Tools",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            FeatureCard(
                title = "Password Strength Checker",
                subtitle = "Check strength, predictability & score",
                icon = Icons.Default.Security,
                onClick = onStrengthCheckClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            FeatureCard(
                title = "Password Breach Check",
                subtitle = "Check if your password was leaked (HIBP)",
                icon = Icons.Default.Warning,
                onClick = onBreachCheckClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            FeatureCard(
                title = "History",
                subtitle = "View encrypted password check history",
                icon = Icons.Default.History,
                onClick = onHistoryClick
            )

            Spacer(modifier = Modifier.height(20.dp))

            AboutUsFooter()
        }
    }
}

@Composable
fun SummaryCard(
    lastStrength: String,
    lastBreach: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8F5E9)
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            Text(
                "Your Security Summary",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B5E20)
            )

            Spacer(modifier = Modifier.height(12.dp))

            SummaryRow(
                label = "Last strength check:",
                value = if (lastStrength.isNotEmpty()) lastStrength else "Not checked"
            )

            Spacer(modifier = Modifier.height(6.dp))

            SummaryRow(
                label = "Last breach check:",
                value = if (lastBreach.isNotEmpty()) lastBreach else "Not checked"
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


@Composable
fun AboutUsFooter() {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF1F8F4)
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {

            Text(
                text = "About Secure Pass",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF1B5E20)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Secure Pass is an application designed to help users evaluate password strength and check for data breaches while maintaining complete privacy and security. No passwords are stored or uploaded.",
                fontSize = 14.sp,
                color = Color.DarkGray,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Divider(color = Color(0xFFB2DFDB))

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color(0xFF2E7D32)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Student Number: S3345558",
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = Color(0xFF2E7D32)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "s3345558@live.tees.ac.uk",
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "© Secure Pass • Privacy-First Password Security",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
