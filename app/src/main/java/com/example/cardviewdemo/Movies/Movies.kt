package com.example.cardviewdemo.Movies

data class Movies(
    val actors: List<Actor>,
    val aka: List<String>,
    val countries: List<String>,
    val creator_names: List<Any>,
    val description: String,
    val director_names: List<String>,
    val genres: List<String>,
    val id: String,
    val image: String,
    val imdb_date: String,
    val imdb_type: String,
    val languages: List<String>,
    val popularity: Int,
    val rating: Double,
    val rating_count: Int,
    val release_year: Int,
    val runtime: String,
    val title: String,
    val version: Int
)