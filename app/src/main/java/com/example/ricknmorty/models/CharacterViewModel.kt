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

    private val _characterList = MutableLiveData<Set<Character>>()
    val characterList: LiveData<Set<Character>> = _characterList

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _selectedCharacter = MutableLiveData<Character?>()
    val selectedCharacter: MutableLiveData<Character?> = _selectedCharacter

    private val _loadingCharacter = MutableLiveData<Boolean>()
    val loadingCharacter: LiveData<Boolean> = _loadingCharacter

    private val _characterListError = MutableLiveData(false)
    val characterListError: LiveData<Boolean> = _characterListError

    private val _characterDetailError = MutableLiveData(false)
    val characterDetailError: LiveData<Boolean> = _characterDetailError

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            _loading.value = true
            _characterListError.value = false

            try {
                val response = apiService.getCharacters().awaitResponse()

                if (response.isSuccessful) {
                    _characterList.value = response.body()?.results ?: emptySet()
                } else {
                    _characterList.value = emptySet()
                    _characterListError.value = true
                }
            } catch (e: Exception) {
                Log.e("CharacterViewModel", "Exception occurred: ${e.message}")
                e.printStackTrace()
                _characterList.value = emptySet()
                _characterListError.value = true
            } finally {
                _loading.value = false
            }
        }
    }

    fun getCharacterById(characterId: Int) {
        viewModelScope.launch {
            _loadingCharacter.value = true
            _selectedCharacter.value = null
            _characterDetailError.value = false

            try {
                val response = apiService.getCharacter(characterId).awaitResponse()

                if (response.isSuccessful) {
                    _selectedCharacter.value = response.body()
                } else {
                    _characterDetailError.value = true
                }
            } catch (e: Exception) {
                Log.e("CharacterViewModel", "Exception occurred: ${e.message}")
                e.printStackTrace()
                _characterDetailError.value = true
            } finally {
                _loadingCharacter.value = false
            }
        }
    }
}