package com.example.newsapp.NewsApp.api.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class SourceResponse(

	@field:SerializedName("sources")
	val sources: List<SourcesItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("message")
	val message: List<SourcesItem?>? = null,

	@field:SerializedName("code")
	val code: String? = null
) : Parcelable