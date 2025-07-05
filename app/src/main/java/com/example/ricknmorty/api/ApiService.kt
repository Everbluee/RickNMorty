package com.example.ricknmorty.api

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("character")
    fun getCharacters(): Call<CharacterResponse>
}