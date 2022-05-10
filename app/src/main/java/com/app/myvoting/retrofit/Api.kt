package com.app.myvoting.retrofit

import com.app.myvoting.retrofit.response.hasil.ModelResultHasilData
import com.app.myvoting.retrofit.response.kandidat.ModelResultData
import com.app.myvoting.retrofit.response.pemilih.ModelResultPemilihData
import com.app.myvoting.retrofit.response.pemilihan.UserRequest
import com.app.myvoting.retrofit.response.pemilihan.UserResponse
import com.app.myvoting.retrofit.response.tps.ModelResultTpsData
import retrofit2.Call
import retrofit2.http.*


interface Api {
    @GET("api/kandidat")
    fun getKandidat(): Call<ModelResultData>

    @GET("api/hasil")
    fun getHasil(): Call<ModelResultHasilData>

    @GET("api/pemilih")
    fun getPemilih(): Call<ModelResultPemilihData>

    @GET("api/tps")
    fun getTps(): Call<ModelResultTpsData>

    @POST("api/pemilihan")
    fun postPemilihan(
        @Body req: UserRequest?
    ): Call<UserResponse>
}