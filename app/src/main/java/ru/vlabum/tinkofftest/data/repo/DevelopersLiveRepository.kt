package ru.vlabum.tinkofftest.data.repo

import ru.vlabum.tinkofftest.data.entity.DevelopersLife
import java.lang.Thread.sleep

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

object DevelopersLiveRepository {
    private val network = NetworkManager.api
    val devLifeList = ArrayList<DevelopersLife>()
        get() = field


    suspend fun getNext() {
        val content = network.getRandom().apply { sleep(1000) }
        devLifeList.add(content)
    }

    fun getPrev(index: Int): DevelopersLife? {
        if (devLifeList.size > index) {
            return devLifeList[index]
        }
        return null
    }


}