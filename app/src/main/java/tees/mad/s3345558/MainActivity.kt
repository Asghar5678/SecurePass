package tees.mad.s3345558

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppNavGraph()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyAppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavScreens.Splash.route
    ) {
        composable(NavScreens.Splash.route) {
            SecurePassCheck(navController = navController)
        }

        composable(NavScreens.Login.route) {
            LoginScreen(navController = navController)
        }

        composable(NavScreens.Register.route) {
            RegistrationScreen(navController = navController)
        }

        composable(NavScreens.Home.route) {
            HomeScreen(
                onStrengthCheckClick = {
                    navController.navigate(NavScreens.StrengthChecker.route)
                },
                onBreachCheckClick = {
                    navController.navigate(NavScreens.BreachChecker.route)
                }

            )
        }


        composable(NavScreens.BreachChecker.route) {
            PasswordBreachCheckScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavScreens.StrengthChecker.route) {
            StrengthCheckScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}


@Composable
fun SecurePassCheck(navController: NavController) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        delay(3000)

        if (UserPrefs.checkLoginStatus(context)) {
            navController.navigate(NavScreens.Home.route) {
                popUpTo(NavScreens.Splash.route) {
                    inclusive = true
                }
            }
        } else {
            navController.navigate(NavScreens.Login.route) {
                popUpTo(NavScreens.Splash.route) {
                    inclusive = true
                }
            }
        }

    }

    SplashScreen()
}

@Composable
fun SplashScreen() {

//    val context = LocalContext.current as Activity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.lite_green)),
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(100.dp))

            Text(
                text = "Secure Pass App",
                color = colorResource(id = R.color.white),
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 4.dp)
            )


            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .background(
                        color = colorResource(id = R.color.white),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = colorResource(id = R.color.white),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(16.dp),

                )
            {
                Image(
                    modifier = Modifier.size(200.dp, 200.dp),
                    painter = painterResource(id = R.drawable.ic_securepass),
                    contentDescription = "Secure Pass",
                )
            }
            Spacer(modifier = Modifier.height(12.dp))


            Text(
                text = "By",
                color = colorResource(id = R.color.white),
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "Bilal",
                color = colorResource(id = R.color.white),
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 4.dp)
            )


        }
    }

}


@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}