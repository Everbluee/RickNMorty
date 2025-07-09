package com.example.ricknmorty.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ricknmorty.api.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class CharacterViewModel : ViewModel() {
    private val apiService = RetrofitClient.apiService

    private val _data = MutableLiveData<Set<Character>>()
    val data: LiveData<Set<Character>> = _data

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            _loading.value = true

            try {
                val response = apiService.getCharacters().awaitResponse()

                if (response.isSuccessful) {
                    _data.value = response.body()?.results ?: emptySet()
                } else {
                    _data.value = emptySet()
                }
            } catch (e: Exception) {
                Log.e("CharacterViewModel", "Exception occurred: ${e.message}")
                e.printStackTrace()
                _data.value = emptySet()
            }

            _loading.value = false
        }
    }

    fun getCharacterById(characterId: Int): Character? {
        return _data.value?.find { it.id == characterId }
    }
}