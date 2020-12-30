package io.github.masterj3y.sorna.feature.ad.create

import android.view.View
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import io.github.masterj3y.sorna.R
import io.github.masterj3y.sorna.core.extension.toast
import io.github.masterj3y.sorna.databinding.FragmentCreateNewAdBinding
import java.util.regex.Pattern

const val TITLE_MIN_LEN = 3
const val DESC_MIN_LEN = 10
const val PRICE_MIN = 100

fun FragmentCreateNewAdBinding.isAdDetailsValid(hasImages: Boolean, selectedCategoryId: String?): Boolean {
    val titleText = title.text.toString()
    val descriptionText = description.text.toString()
    val phoneNumberText = phoneNumber.text.toString()
    val priceNumber = getPriceNumber(price)

    var validationState = 0
    var viewToFocus: View? = null

    if (titleText.isBlank() || titleText.length < TITLE_MIN_LEN) {
        titleLayout.error = root.context.getString(R.string.create_new_ad_title_validation_error, TITLE_MIN_LEN)
        if (viewToFocus == null)
            viewToFocus = title
    } else validationState++

    if (descriptionText.isBlank() || descriptionText.length < DESC_MIN_LEN) {
        descriptionLayout.error = root.context.getString(R.string.create_new_ad_description_validation_error, DESC_MIN_LEN)
        if (viewToFocus == null)
            viewToFocus = description
    } else validationState++

    if (isPhoneNumberValid(phoneNumberText).not()) {
        phoneNumberLayout.error = root.context.getString(R.string.create_new_ad_phoneNumber_validation_error)
        if (viewToFocus == null)
            viewToFocus = phoneNumber
    } else validationState++

    if (priceNumber < PRICE_MIN) {
        priceLayout.error = root.context.getString(R.string.create_new_ad_price_validation_error)
        if (viewToFocus == null)
            viewToFocus = price
    } else validationState++

    if (selectedCategoryId.isNullOrBlank()) {
        selectCategoryLayout.error = root.context.getString(R.string.create_new_ad_category_validation_error)
        if (viewToFocus == null)
            viewToFocus = selectCategory
    } else validationState++

    if (!hasImages) {
        root.context.toast(R.string.create_new_ad_images_error)
        if (viewToFocus == null)
            viewToFocus = selectCategory
    } else validationState++

    viewToFocus?.requestFocus()

    return validationState == 6
}

fun FragmentCreateNewAdBinding.clearInput() {
    title.setText("")
    titleLayout.isErrorEnabled = false
    description.setText("")
    descriptionLayout.isErrorEnabled = false
    phoneNumber.setText("")
    phoneNumberLayout.isErrorEnabled = false
    price.setText("")
    priceLayout.isErrorEnabled = false
    selectCategory.setText("")
    selectCategoryLayout.isErrorEnabled = false
    (imagesRecyclerView.adapter as CreateNewAdImageAdapter).clear()
}

fun FragmentCreateNewAdBinding.addTextFieldsOnChangedListener() {
    title.addTextChangedListener { if (it.toString().length > TITLE_MIN_LEN) titleLayout.isErrorEnabled = false }
    description.addTextChangedListener { if (it.toString().length > DESC_MIN_LEN) descriptionLayout.isErrorEnabled = false }
    phoneNumber.addTextChangedListener { if (isPhoneNumberValid(it.toString())) phoneNumberLayout.isErrorEnabled = false }
    price.addTextChangedListener { if (getPriceNumber(price) >= PRICE_MIN) priceLayout.isErrorEnabled = false }
    selectCategory.addTextChangedListener { if (selectCategory.text.toString() != root.context.getString(R.string.select_category)) selectCategoryLayout.isErrorEnabled = false }
}

fun isPhoneNumberValid(phoneNumber: String) = Pattern.matches("09[01239]\\d{8}", phoneNumber)

fun getPriceNumber(editText: EditText): Long = if (editText.text.isNullOrBlank()) 0 else editText.text.toString().toLong()