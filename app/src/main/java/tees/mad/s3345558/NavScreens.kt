package tees.mad.s3345558



sealed class NavScreens(val route: String) {
    object Splash : NavScreens("splash_route")
    object Login : NavScreens("login_route")
    object Register : NavScreens("register_route")
    object Profile : NavScreens("profile")

    object Home : NavScreens("home_route")
    object Settings : NavScreens("settings")
    object StrengthChecker : NavScreens("strength_checker")
    object BreachChecker : NavScreens("breacher_checker")

    object History : NavScreens("history")


}