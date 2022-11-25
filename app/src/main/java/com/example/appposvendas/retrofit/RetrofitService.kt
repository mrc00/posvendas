package com.example.appposvendas.retrofit
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


object RetrofitService {
    private val client = OkHttpClient.Builder().build()

    fun getUnsafeOkHttpClient(serviceTimeout: Int): OkHttpClient.Builder? {
        return try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            var builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { hostname: String?, session: SSLSession? -> true }
            val okHttpClient = builder
                .connectTimeout(serviceTimeout.toLong(), TimeUnit.MINUTES)
                .writeTimeout(serviceTimeout.toLong(), TimeUnit.MINUTES)
                .readTimeout(serviceTimeout.toLong(), TimeUnit.MINUTES)
                .build()
            builder = okHttpClient.newBuilder()
            builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://192.168.1.11:30030/")
        //.baseUrl("https://srvsap04h:30030/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .client(getUnsafeOkHttpClient(15)!!.build())
        .build()

    fun<T> buildService(service: Class<T>):T{
        return retrofit.create(service)
    }
}