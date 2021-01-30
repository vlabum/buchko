package ru.vlabum.tinkofftest.data.entity

import java.util.*

data class DevelopersLife (
    val id: Int,
    val description: String,
    val votes: Int,
    val author: String,
    val date: String,
    val gifURL: String,
    val gifSize: Int,
    val previewURL: String,
    val videoURL: String,
    val videoPath: String,
    val videoSize: Int,
    val type: String,
    val width: String,
    val height: String,
    val commentsCount: Int,
    val fileSize: Int,
    val canVote: Boolean
)

data class DevelopersLifeList (
    val result: List<DevelopersLife>
)