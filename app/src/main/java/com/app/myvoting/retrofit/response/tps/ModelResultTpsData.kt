package com.app.myvoting.retrofit.response.tps

import com.app.myvoting.retrofit.response.kandidat.ModelResult
import com.google.gson.annotations.SerializedName

class ModelResultTpsData {
    @SerializedName("data")
    lateinit var modelResultTps: List<ModelResultTps>
}