package com.example.navigationdrawercompose

sealed class NavegacionItem(var route: String, var icon: Int, var title: String)
{
    object Home : NavegacionItem("home", R.drawable.baseline_insert_drive_file_20, "Insertar")
    object Profile : NavegacionItem("profile", R.drawable.baseline_delete_outline_24, "Borrar")
    object Settings : NavegacionItem("settings", R.drawable.baseline_visibility_20, "Visualizar")

}