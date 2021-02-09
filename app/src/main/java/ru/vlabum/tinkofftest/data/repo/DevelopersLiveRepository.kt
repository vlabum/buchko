package ru.vlabum.tinkofftest.data.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.vlabum.tinkofftest.data.entity.DevelopersLife
import ru.vlabum.tinkofftest.data.entity.DevelopersLifeList
import java.lang.Thread.sleep

sealed class ResultDevLife<out R> {
    data class Success<out T>(val data: T) : ResultDevLife<T>()
    data class Error(val exception: Exception) : ResultDevLife<Nothing>()
}

object DevelopersLiveRepository {
    private val network = NetworkManager.api
    val devLifeList = ArrayList<DevelopersLife>()

    suspend fun getNext(): ResultDevLife<DevelopersLife> {
        return withContext(Dispatchers.IO) {
            try {
                val content = network.getRandom()//.apply { sleep(1000) }
                devLifeList.add(content)
                ResultDevLife.Success(content)
            } catch (e: Exception) {
                ResultDevLife.Error(Exception("Some Error"))
            }
        }
    }


    suspend fun getHot(page: Int): ResultDevLife<List<DevelopersLife>> {
        return withContext(Dispatchers.IO) {
            try {
                val content = network.getHot(page)//.apply { sleep(1000) }
                devLifeList.addAll(content.result)
                ResultDevLife.Success(content.result)
            } catch (e: Exception) {
                ResultDevLife.Error(e)
            }
        }
    }

    suspend fun getLatest(page: Int): ResultDevLife<List<DevelopersLife>> {
        return withContext(Dispatchers.IO) {
            try {
                val content = network.getLatest(page)//.apply { sleep(1000) }
                devLifeList.addAll(content.result)
                ResultDevLife.Success(content.result)
            } catch (e: Exception) {
                ResultDevLife.Error(e)
            }
        }
    }

    suspend fun getTop(page: Int): ResultDevLife<List<DevelopersLife>> {
        return withContext(Dispatchers.IO) {
            try {
                val content = network.getTop(page)//.apply { sleep(1000) }
                devLifeList.addAll(content.result)
                ResultDevLife.Success(content.result)
            } catch (e: Exception) {
                ResultDevLife.Error(e)
            }
        }
    }

}