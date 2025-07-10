package com.example.ricknmorty.api

import com.example.ricknmorty.models.Character
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("character")
    fun getCharacters(): Call<CharacterResponse>

    @GET("character/{id}")
    fun getCharacter(@Path("id") id: Int): Call<Character>
}