package com.example.digitalcurrencyappdca.apiManager

import com.example.digitalcurrencyappdca.apiManager.model.CoinsData
import com.example.digitalcurrencyappdca.apiManager.model.NewsData
import ir.dunijet.dunipool.apiManager.model.ChartData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @Headers(API_KEY)
    @GET("v2/news/")
    fun getTopNews(
        @Query("sortOrder") sortOreder: String = "popular"
    ): Call<NewsData>

    @Headers(API_KEY)
    @GET("top/totalvolfull")
    fun getTOPCoins(
        @Query("tsym") to_symbol: String = "USD",
        @Query("limit") limit_data :Int = 10
    ):Call<CoinsData>

    @Headers(API_KEY)
    @GET("{period}")
    fun getCharData(
        @Path("period") period : String ,
        @Query("fsym") fromSymbol : String ,
        @Query("limit") limit : Int ,
        @Query("aggregate") aggregate : Int ,
        @Query("tsym") toSymbol: String = "USD"
    ):Call<ChartData>
}