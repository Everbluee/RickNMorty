package com.example.ricknmorty.api

import com.example.ricknmorty.models.Character

data class CharacterResponse(
    val results: Set<Character>
)
