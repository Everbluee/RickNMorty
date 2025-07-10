package com.example.ricknmorty

object AppRoutes {
    const val CHARACTER_LIST_ROUTE = "characterList"
    const val CHARACTER_DETAIL_ROUTE = "characterDetail"
    const val CHARACTER_ID_ARG = "characterId"
    const val CHARACTER_DETAIL_WITH_ARG = "$CHARACTER_DETAIL_ROUTE/{$CHARACTER_ID_ARG}"
}