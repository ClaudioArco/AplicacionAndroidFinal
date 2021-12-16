package com.example.navigationdrawercompose.ui.theme

import com.example.navigationdrawercompose.retrofitjsonjetpack.UserInfo
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

const val base_user = "http://iesayala.ddns.net/claudio/"

interface UserInterface {
    @GET("movilesjson.php")
    fun userInformation(): Call<UserInfo>
}

object UserInstance {
    val userInterface: UserInterface

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(base_user)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        userInterface = retrofit.create(UserInterface::class.java)
    }


}