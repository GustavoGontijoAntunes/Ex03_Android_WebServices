package br.com.fourvrstudios.aula6_retrofitdemo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fourvrstudios.aula6_retrofitdemo.Network.Cep
import br.com.fourvrstudios.aula6_retrofitdemo.Network.Photo
import br.com.fourvrstudios.aula6_retrofitdemo.Network.RetrofitInstance
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

enum class RestApiStatus { LOADING, ERROR, DONE }

class RetrofitViewModel : ViewModel() {

    private val _status = MutableLiveData<RestApiStatus>()
    val status : LiveData<RestApiStatus>
        get() = _status

    private val _response = MutableLiveData<List<Photo>?>()
    val response: LiveData<List<Photo>?>
        get() = _response

    private val _reqResponse = MutableLiveData<Photo?>()
    val reqResponse: LiveData<Photo?>
        get() = _reqResponse

    private val _cepReqResponse = MutableLiveData<Cep?>()
    val cepReqResponse: LiveData<Cep?>
        get() = _cepReqResponse

    var cep = MutableLiveData<String?>()

    init {

    }

    public fun getByCep() : Job =
        viewModelScope.launch {
            try {
                _status.value = RestApiStatus.LOADING
                _cepReqResponse.value = RetrofitInstance.retrofit.getCep(cep.value.toString()).body()
                _status.value = RestApiStatus.DONE
            } catch (e: Exception){
                Log.i("MYTAG", "Exception Genérica")
                _cepReqResponse.value = null
            }
        }

    private fun getById() : Job =
        viewModelScope.launch {
            try {
                _status.value = RestApiStatus.LOADING
                _reqResponse.value = RetrofitInstance.retrofit.getByIdPath(1).body()
                _status.value = RestApiStatus.DONE
            } catch (e: Exception){
                Log.i("MYTAG", "Exception Genérica")
                _reqResponse.value = null
            }
        }

    private fun getPhotosByAlbum() : Job =
        viewModelScope.launch {
            try{
                _status.value = RestApiStatus.LOADING
                _response.value = RetrofitInstance.retrofit.getPhotosByAlbumId(7).body()
                _status.value = RestApiStatus.DONE
            } catch (e: Exception){
                Log.i("MYTAG", "Exception Genérica")
                _response.value = null
            }
        }

    private fun getAllPhotos() : Job =
        viewModelScope.launch {
            try{
                _status.value = RestApiStatus.LOADING
                _response.value = RetrofitInstance.retrofit.getAll().body()
                _status.value = RestApiStatus.DONE
            } catch (e: IOException) {
                _status.value = RestApiStatus.ERROR
                Log.i("MYTAG", "IOException - Sem internet, URL incorreta")
                _response.value = null
            } catch (e: HttpException) {
                _status.value = RestApiStatus.ERROR
                Log.i("MYTAG", "HttpException - Status Codes não iniciados com 2xx")
                _response.value = null
            } catch (e: Exception) {
                _status.value = RestApiStatus.ERROR
                Log.i("MYTAG", "Exception Genérica")
                _response.value = null
            }
        }

    private fun getRestApiResponse() {
        RetrofitInstance.retrofit.getAllCall().enqueue(object : Callback<List<Photo>>{
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                if (response.isSuccessful){
                    _response.value = response.body()

                    Log.i(
                        "RETROFIT_CALL",
                        "Código de resposta: ${
                            response.code().toString()
                        } - Registros retornados: ${response.body()?.size}"
                    )
                }
            }

            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                Log.i(
                    "RETROFIT_CALL",
                    "Falha: " + t.message
                )
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        //getData().cancel() - As corrotinas em viewModelScope já são canceladas automaticamente.
    }
}