package com.example.movielistproj

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie (
    val id: Int,
    val title: String,
    val overview: String,
    @SerialName("vote_average")val voteAverage: Double,
    @SerialName("release_date")val releaseDate: String,
    val popularity: Double,
    @SerialName("vote_count")val voteCount: Int
        )