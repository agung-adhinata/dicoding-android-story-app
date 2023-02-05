package com.nekkiichi.storyapp.ui.view.addStory

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.nekkiichi.storyapp.data.ResponseStatus
import com.nekkiichi.storyapp.data.remote.response.BasicResponse
import com.nekkiichi.storyapp.databinding.ActivityAddStoryBinding
import com.nekkiichi.storyapp.utils.createCustomTempFile
import com.nekkiichi.storyapp.utils.uriToFile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class AddStoryActivity : AppCompatActivity() {
    class AddStoryActivity : AppCompatActivity() {
        private lateinit var binding: ActivityAddStoryBinding
        private val viewModel: AddStoryViewModel by viewModels()
        private lateinit var currentPhotoPath: String
        private var getFile: File? = null
        private val launcherIntentCamera = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                val myfile = File(currentPhotoPath)
                val result = BitmapFactory.decodeFile(myfile.path)
                getFile = myfile
                binding.ivPhoto.setImageBitmap(result)
            }
        }
        private val launcherIntentGallery =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val selectedImg: Uri = it.data?.data as Uri
                    val myFile = uriToFile(selectedImg, this@AddStoryActivity)
                    getFile = myFile
                    binding.ivPhoto.setImageURI(selectedImg)
                }
            }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            //setup binding
            binding = ActivityAddStoryBinding.inflate(layoutInflater)
            setContentView(binding.root)
            //setup permission
            if (!allPermissionsGranted()) {
                ActivityCompat.requestPermissions(
                    this,
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            }
            title = "Add new Story"
            // setup lifecycle
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        viewModel.status.collect {
                            collectUploadStatus(it)
                        }
                    }
                }
            }

            // setup binding
            binding.btnOpenCamera.setOnClickListener {
                openCamera()
            }
            binding.btnOpenGallery.setOnClickListener {
                openGallery()
            }
            binding.btnAdd.setOnClickListener {
                uploadImage()
            }
        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == REQUEST_CODE_PERMISSIONS) {
                if (!allPermissionsGranted()) {
                    Toast.makeText(this, "Permission Failed", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

        private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
        }

        private fun openCamera() {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.resolveActivity(packageManager)

            createCustomTempFile(application).also {
                val photoUri = FileProvider.getUriForFile(
                    this@AddStoryActivity,
                    "com.nekkiichi.storyapp",
                    it
                )
                currentPhotoPath = it.absolutePath
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                launcherIntentCamera.launch(intent)
            }
        }

        private fun openGallery() {
            val intent = Intent()
            intent.action = ACTION_GET_CONTENT
            intent.type = "image/*"
            val chooser = Intent.createChooser(intent, "Choose a Picture")
            launcherIntentGallery.launch(chooser)
        }

        private fun uploadImage() {
            if (getFile == null) {
                Toast.makeText(this, "Image is empty", Toast.LENGTH_SHORT).show()
            } else if (binding.edAddDescription.text.toString().isEmpty()) {
                Toast.makeText(this, "remember to add description", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.uploadImage(getFile as File, binding.edAddDescription.text.toString())
            }
        }

        private fun collectUploadStatus(status: ResponseStatus<BasicResponse>) {
            when (status) {
                is ResponseStatus.Loading -> showLoading(true)
                is ResponseStatus.Error -> {
                    showLoading(false)
                    Toast.makeText(this, "error: ${status.error}", Toast.LENGTH_SHORT).show()
                }

                is ResponseStatus.Success -> {
                    showLoading(false)
                    Toast.makeText(this, "upload successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }

                else -> showLoading(false)
            }
        }

        private fun showLoading(boolean: Boolean) {
            binding.btnOpenCamera.isEnabled = !boolean
            binding.btnOpenGallery.isEnabled = !boolean
            binding.btnAdd.isEnabled = !boolean
        }

        companion object {
            private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
            private const val REQUEST_CODE_PERMISSIONS = 10
        }
    }
}