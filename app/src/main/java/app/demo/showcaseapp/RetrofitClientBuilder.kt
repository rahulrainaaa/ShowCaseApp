package app.demo.showcaseapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientBuilder {

    private fun getRetrofitClient() = Retrofit.Builder()
        .baseUrl("http://192.168.1.36:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val loginWebService: LoginWebService = getRetrofitClient().create(LoginWebService::class.java)
    val aesCredWebService: AESCredWebService = getRetrofitClient().create(AESCredWebService::class.java)
}