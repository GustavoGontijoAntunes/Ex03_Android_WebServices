package br.com.fourvrstudios.aula6_retrofitdemo.Network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

class RetrofitInstance {
    companion object {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://viacep.com.br/ws/") // URL base
            .addConverterFactory(MoshiConverterFactory.create(moshi)) // Converter Factory
            .build()
            .create(ApiService::class.java) // Interface onde estarão os métodos HTTP
    }
}