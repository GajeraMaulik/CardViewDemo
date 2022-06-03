package com.example.cardviewdemo.services.model

data class UserList(val data:List<User>)
data class User(val id: String?,val name: String?,  val email: String?, val status: String?, val gender: String?)
data class UserResponse(val code: Int?, val meta: Meta?, val data: User?)

data class Pagination(val limit: Int,val page: Int,val pages: Int,val total: Int)

data class Meta(val pagination: Pagination)