package com.dimmer.app.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DimmerApi {

    @GET("movies/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1
    ): MovieListResponse

    @GET("movies/{id}")
    suspend fun getMovieDetails(
        @Path("id") id: Int
    ): MovieDetail

    @GET("movies/search")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): MovieListResponse

    @GET("movies/{id}/similar")
    suspend fun getSimilarMovies(
        @Path("id") id: Int
    ): MovieListResponse
}