package ru.vlabum.tinkofftest.data.repo

import retrofit2.Call
import retrofit2.http.GET
import ru.vlabum.tinkofftest.data.entity.DevelopersLife

interface RestService {

    @GET("/random?json=true")
    suspend fun getRandom(): DevelopersLife

}