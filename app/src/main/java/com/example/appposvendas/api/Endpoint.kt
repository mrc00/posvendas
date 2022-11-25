package com.example.appposvendas.api



import com.example.appposvendas.requests.LoginRequest
import com.example.appposvendas.requests.ServicePatch
import com.example.appposvendas.response.LoginResponse
import com.example.appposvendas.response.ServicesValue
import com.example.appposvendas.response.UserValue
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface Endpoint {
    @POST("b1s/v1/Login")
    @Headers("Content-Type:application/json; charset=utf-8")
    fun login(@Body user: LoginRequest): Call<LoginResponse>

    @GET("b1s/v1/Users?\$select=UserCode,UserName,eMail,U_EASY_TOKENPAS,U_EASY_BWUWB")
    @Headers("Content-Type:application/json; charset=utf-8")
    fun getUsers(@Header("Cookie") token: String,
                 @Query("\$filter") userCode: String): Call<UserValue>

    @GET("b1s/v1/ServiceCalls")
    @Headers("Prefer:odata.maxpagesize=1000")
    fun getService(@Header("Cookie")token: String,
                   @Query("\$filter")filter: String): Call<ServicesValue>

    @PATCH("b1s/v1/ServiceCalls({id})")
    @Headers("Content-Type:application/json; charset=utf-8")
    fun atualizaStatus(@Body service: ServicePatch,
                       @Header("Cookie") token: String,
                       @Path("id") id: Int):Call<JsonObject>


}