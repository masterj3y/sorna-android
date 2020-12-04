package io.github.masterj3y.sorna.feature.ad.create

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import io.github.masterj3y.sorna.R
import io.github.masterj3y.sorna.core.platform.BaseFragment
import io.github.masterj3y.sorna.databinding.FragmentCreateNewAdBinding
import io.github.masterj3y.sorna.feature.categories.Category
import io.github.masterj3y.sorna.feature.ad.create.CreateNewAdImageAdapter.OnItemClickedListener
import io.github.masterj3y.sorna.feature.dialog.action.ActionDialogItem
import io.github.masterj3y.sorna.feature.dialog.action.actionDialog
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.io.File

@AndroidEntryPoint
class CreateNewAdFragment :
        BaseFragment<FragmentCreateNewAdBinding>(R.layout.fragment_create_new_ad), OnItemClickedListener {

    private val model: CreateNewAdViewModel by viewModels()

    private val categories = mutableListOf<Category>()

    private val imageAdapter = CreateNewAdImageAdapter(this)
    private val selectedImages = arrayListOf<Image>()

    private lateinit var selectedCategoryId: String

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == STORAGE_PERMISSION_RC) {
            if (isStoragePermissionGranted())
                openImagePicker()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandleResult(requestCode, resultCode, data, IMAGE_PICKER_RC)) {
            selectedImages.clear()
            selectedImages.addAll(ImagePicker.getImages(data))
            imageAdapter.submitList(selectedImages.toUriList())
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = model
            imagePickerAdapter = imageAdapter
            imagesRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
            selectCategory.setOnClickListener { selectCategory() }
            postAd.setOnClickListener { postAd() }
            addTextFieldsOnChangedListener()
        }

    }

    @ExperimentalCoroutinesApi
    private fun subscribeObservers() = with(model) {
        cachedCategories.removeObservers(viewLifecycleOwner)
        cachedCategories.observe(viewLifecycleOwner) {
            categories.clear()
            categories.addAll(it)
        }

        isAdPosted.removeObservers(viewLifecycleOwner)
        isAdPosted.observe(viewLifecycleOwner) {
            if (it)
                Toast.makeText(requireContext(), R.string.create_new_ad_successful, Toast.LENGTH_SHORT).show()
        }

        isFailed.removeObservers(viewLifecycleOwner)
        isFailed.observe(viewLifecycleOwner) {
            if (it)
                Toast.makeText(requireContext(), R.string.create_new_ad_failed, Toast.LENGTH_SHORT).show()

        }
    }

    @ExperimentalCoroutinesApi
    private fun postAd() = with(binding) {
        val categoryId =
                if (::selectedCategoryId.isInitialized)
                    selectedCategoryId
                else null

        if (isAdDetailsValid(categoryId)) {
            val titleText = title.text.toString()
            val descriptionText = description.text.toString()
            val phoneNumberText = phoneNumber.text.toString()
            val priceNumber = price.text.toString().toLong()
            model.postAd(titleText, descriptionText, phoneNumberText,priceNumber,
                    selectedCategoryId, selectedImages.mapToFileList())
        }
    }

    override fun onImageClicked(position: Int) {
        selectImage()
    }

    override fun onRemoveImageClicked(position: Int) {
        selectedImages.removeAt(position)
    }

    private fun selectCategory() {
        actionDialog {
            title(R.string.select_category)
            items = categories.map {
                ActionDialogItem(
                        text = it.title,
                        onClick = {
                            Toast.makeText(requireContext(), it.id, Toast.LENGTH_SHORT).show()
                            binding.selectCategory.text = it.title
                            selectedCategoryId = it.id
                            dismiss()
                        })
            }
        }.show()
    }

    private fun selectImage() {
        if (isPermissionGranted())
            openImagePicker()
    }

    private fun openImagePicker() {
        ImagePicker.with(this@CreateNewAdFragment)
                .setFolderMode(true)
                .setFolderTitle(getString(R.string.create_new_ad_folder_title))
                .setRootDirectoryName(Config.ROOT_DIR_DCIM)
                .setDirectoryName(getString(R.string.create_new_ad_directory_name))
                .setMultipleMode(true)
                .setShowNumberIndicator(true)
                .setMaxSize(AD_MAX_IMAGES_COUNT)
                .setLimitMessage(getString(R.string.create_new_ad_images_limit, AD_MAX_IMAGES_COUNT))
                .setSelectedImages(selectedImages)
                .setRequestCode(IMAGE_PICKER_RC)
                .start()
    }

    private fun isPermissionGranted(): Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (!isStoragePermissionGranted()) {
            showPermissionAlertDialog()
            false
        } else
            true
    } else
        true


    private fun isStoragePermissionGranted() =
            ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED

    private fun showPermissionAlertDialog() {
        AlertDialog.Builder(requireContext())
                .setTitle(R.string.storage_permission_title)
                .setMessage(R.string.storage_permission_message)
                .setPositiveButton(R.string.storage_permission_grant) { _, _ ->
                    requestPermissions(
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            STORAGE_PERMISSION_RC
                    )
                }
                .setNegativeButton(R.string.storage_permission_deny) { _, _ -> }
                .show()
    }

    private fun ArrayList<Image>.toUriList(): List<Uri> = map { it.uri }

    private fun ArrayList<Image>.mapToFileList(): List<File> = map {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            File(requireNotNull(it.uri.path))
        else
            File(it.path)
    }

    companion object {

        const val AD_MAX_IMAGES_COUNT = 5
        private const val STORAGE_PERMISSION_RC = 142
        private const val IMAGE_PICKER_RC = 102
    }
}