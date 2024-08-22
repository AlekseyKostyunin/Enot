package com.alekseykostyunin.enot.presentation

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.alekseykostyunin.enot.presentation.navigation.StartNavigation
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executor

private const val FILENAME_FORMAT = "yyyy-MM-dd_HH-mm-ss"
private const val DATE_FORMAT = "dd.MM.yyyy HH:mm"

class MainActivity : ComponentActivity() {

    private var imageCapture: ImageCapture? = null
    private lateinit var executor: Executor

    private val name = SimpleDateFormat(FILENAME_FORMAT, Locale.UK)
        .format(System.currentTimeMillis())

    private val date = SimpleDateFormat(DATE_FORMAT, Locale.UK)
        .format(System.currentTimeMillis())

    private val launcher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { map ->
        if (map.values.all { it }) {
            //startCamera()
        } else {
            Toast.makeText(this, "Разрешения не получены!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        executor = ContextCompat.getMainExecutor(this)
        setContent {
            //EnotTheme {}
            StartNavigation(
//                onTakePhoto = { takePhoto() },
//                onStartCamera = { startCamera() },
                 checkPermission()
            )
            checkPermission()
        }

    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        }

        val outputOption = ImageCapture.OutputFileOptions
            .Builder(
                this.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            ).build()

        imageCapture.takePicture(
            outputOption,
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        this@MainActivity,
                        "Photo failed: ${exc.message}", Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    Toast.makeText(
                        this@MainActivity,
                        "Saved: ${output.savedUri}", Toast.LENGTH_SHORT
                    ).show()
                    //viewModel.insertPhoto(output.savedUri.toString(), date)
                    //findNavController().navigate(R.id.action_create_photo_to_list_photo)
                }
            }
        )
    }

    fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider
            .getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
            //preview.setSurfaceProvider(binding.viewFinder.surfaceProvider) // устанавливаем SurfaceView
            imageCapture = ImageCapture.Builder().build()
            cameraProvider.unbindAll() // отключаем камеру
            cameraProvider.bindToLifecycle(
                this,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageCapture
            )
        }, executor)
    }

    fun checkPermission() {
        val isAllGranted = REQUEST_PERMISSIONS.all { permission ->
            ContextCompat.checkSelfPermission(
                this, permission
            ) == PackageManager.PERMISSION_GRANTED
        }
        if (isAllGranted) {
            //startCamera()
            //Toast.makeText(this, "Permission is Granted", Toast.LENGTH_SHORT).show()
        } else {
            launcher.launch(REQUEST_PERMISSIONS)
        }
    }

    companion object {
        private val REQUEST_PERMISSIONS = buildList {
            add(Manifest.permission.CAMERA)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()


    }

}