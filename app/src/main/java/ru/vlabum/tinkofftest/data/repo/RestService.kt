package ru.vlabum.tinkofftest.data.repo

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import ru.vlabum.tinkofftest.data.entity.DevelopersLife
import ru.vlabum.tinkofftest.data.entity.DevelopersLifeList

interface RestService {

    @GET("/random?json=true")
    suspend fun getRandom(): DevelopersLife

    @GET("/latest/{page}?json=true")
    suspend fun getLatest(@Path("page") page: Int): DevelopersLifeList

    @GET("/hot/{page}?json=true")
    suspend fun getHot(@Path("page") page: Int): DevelopersLifeList

    @GET("/top/{page}?json=true")
    suspend fun getTop(@Path("page") page: Int): DevelopersLifeList
}