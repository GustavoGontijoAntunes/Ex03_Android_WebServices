package br.com.fourvrstudios.aula6_retrofitdemo.Network

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // Este método irá concatenar photos com a base url, resultando em https://jsonplaceholder.typicode.com/photos
    @GET("photos")
    fun getAllCall() : Call<List<Photo>>

    @GET("photos")
    suspend fun getAll() : Response<List<Photo>>

    @GET("photos")
    suspend fun getPhotosByAlbumId(@Query("albumId") idAlbum : Int) : Response<List<Photo>>
    // https://jsonplaceholder.typicode.com/photos?albumId=idalbum

    @GET("photos/{id}")
    suspend fun getByIdPath(@Path("id") meuId: Int) : Response<Photo>

    @GET("{cep}/json")
    suspend fun getCep(@Path("cep") meuCep: String) : Response<Cep>
}