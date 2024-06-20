package com.kg.myapplication.data.entity.api

import com.kg.myapplication.data.entity.ShopItems
import retrofit2.Call
import retrofit2.http.GET


interface MainApi {

    @GET("api/v1/product/all")
    fun getOrderItems(): Call<ArrayList<ShopItems>>

}