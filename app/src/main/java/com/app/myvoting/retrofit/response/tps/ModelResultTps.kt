package com.app.myvoting.retrofit.response.tps

import com.google.gson.annotations.SerializedName

class ModelResultTps {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("kode_tps")
    lateinit var kodeTps: String

    @SerializedName("tps")
    lateinit var tps: String

    @SerializedName("created_at")
    lateinit var created_at: String

    @SerializedName("updated_at")
    lateinit var updated_at: String
}