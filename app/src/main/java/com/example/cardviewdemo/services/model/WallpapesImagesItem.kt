package com.example.cardviewdemo.services.model

data class WallpapesImagesItem(
    val characters: List<Character>,
    val manga_anime: List<MangaAnime>,
    val popular: List<Popular>
)
data class Popular(
    val category: String,
    val slug: String,
    val thumbnail: String,
    val title: String,
    val wallpaper_count: String
)
data class Character(
    val category: String,
    val slug: String,
    val thumbnail: String,
    val title: String,
    val wallpaper_count: String
)
data class MangaAnime(
    val category: String,
    val slug: String,
    val thumbnail: String,
    val title: String,
    val wallpaper_count: String
)