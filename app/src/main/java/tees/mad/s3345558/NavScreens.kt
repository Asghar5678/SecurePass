package tees.mad.s3345558



sealed class NavScreens(val route: String) {
    object Splash : NavScreens("splash_route")
    object Login : NavScreens("login_route")
    object Home : NavScreens("home_route")
    object Register : NavScreens("register_route")

}