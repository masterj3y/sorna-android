package io.github.masterj3y.sorna.feature.ad.create

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.github.masterj3y.sorna.core.platform.BaseViewModel
import io.github.masterj3y.sorna.feature.ad.AdsRepository
import io.github.masterj3y.sorna.feature.categories.CategoriesRepository
import io.github.masterj3y.sorna.feature.categories.Category
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class CreateNewAdViewModel @ViewModelInject constructor(
    private val adsRepository: AdsRepository,
    private val categoryRepository: CategoriesRepository
) : BaseViewModel() {

    @ExperimentalCoroutinesApi
    val cachedCategories: LiveData<List<Category>> = launchOnViewModelScope {
        categoryRepository.getCachedCategories()
    }

    val isPosting = MutableLiveData(false)
    val isAdPosted = MutableLiveData(false)
    val isFailed = MutableLiveData(false)

    @ExperimentalCoroutinesApi
    fun postAd(
            title: String,
            description: String,
            phoneNumber: String,
            price: Long,
            categoryId: String,
            picsUriList: List<File>
    ) {
        val titleBody = requestBody(title)
        val descriptionBody = requestBody(description)
        val phoneNumberBody = requestBody(phoneNumber)
        val priceBody = requestBody(price.toString())
        val categoryIdBody = requestBody(categoryId)
        val picsBody: List<MultipartBody.Part> = picsUriList
                .mapToMultipartBodyList()

        performPostAd(titleBody, descriptionBody, phoneNumberBody, priceBody, categoryIdBody, *picsBody.toTypedArray())
    }

    @ExperimentalCoroutinesApi
    private fun performPostAd(title: RequestBody,
                              description: RequestBody,
                              phoneNumber: RequestBody,
                              price: RequestBody,
                              categoryId: RequestBody,
                              vararg pics: MultipartBody.Part) =
            viewModelScope.launch {
                posting()
                posted(false)
                failed(false)
                adsRepository.postAd(
                        onSuccess = {
                            isAdPosted.value = true
                            posting(false)
                            posted()
                        },
                        onError = {
                            posting(false)
                            posted(false)
                            failed(true)
                        },
                        title, description, phoneNumber, price, categoryId, *pics)
            }


    private fun posting(isLoading: Boolean = true) {
        this.isPosting.value = isLoading
    }

    private fun posted(isPosted: Boolean = true) {
        this.isAdPosted.value = isPosted
    }

    private fun failed(isFailed: Boolean = true) {
        this.isFailed.value = isFailed
    }

    private fun requestBody(string: String): RequestBody = string.toRequestBody("text/plain".toMediaType())

    private fun List<File>.mapToMultipartBodyList(): List<MultipartBody.Part> = map { file ->
        val requestBody = file.asRequestBody("image/jpeg".toMediaType())
        MultipartBody.Part.createFormData("pic", file.absolutePath, requestBody)
    }
}