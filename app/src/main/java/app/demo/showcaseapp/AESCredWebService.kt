package app.demo.showcaseapp

import retrofit2.http.Body
import retrofit2.http.POST

interface AESCredWebService {

    @POST("aes")
    suspend fun process(@Body request: HttpRequestModel): HttpResponseModel
}