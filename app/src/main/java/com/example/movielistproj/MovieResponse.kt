package com.example.movielistproj

import kotlinx.serialization.Serializable

@Serializable
data class MovieResponse (
    val results: List<Movie>
        )