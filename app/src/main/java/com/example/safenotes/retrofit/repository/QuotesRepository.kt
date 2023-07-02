package com.example.safenotes.retrofit.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.safenotes.models.quotes.QuotesResponse
import com.example.safenotes.retrofit.QuotesApi
import com.example.safenotes.utils.NetworkResult
import org.json.JSONObject
import javax.inject.Inject

class QuotesRepository @Inject constructor(private val quotesApi: QuotesApi) {

    private val _quotesLiveData = MutableLiveData<NetworkResult<QuotesResponse>>()
    val quotesLiveData: LiveData<NetworkResult<QuotesResponse>>
        get() = _quotesLiveData

    suspend fun getQuote(){
        Log.d("TAG_NETWORK_HIT","hit occur for quotes")
        try {
            _quotesLiveData.postValue(NetworkResult.Loading())
            val response = quotesApi.getQuote()
            if(response.isSuccessful && response.body()!=null){
                _quotesLiveData.postValue(NetworkResult.Success(response.body()!!))
            }else if (response.errorBody() != null) {
                val errObj = JSONObject(response.errorBody()!!.charStream().readText())
                _quotesLiveData.postValue(NetworkResult.Error(errObj.getString("message")))
            } else {
                _quotesLiveData.postValue(NetworkResult.Error("Something went wrong"))
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }


}