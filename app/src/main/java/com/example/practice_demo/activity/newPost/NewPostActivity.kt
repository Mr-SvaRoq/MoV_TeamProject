package com.example.practice_demo.activity.newPost

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.MediaController
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.practice_demo.R
import com.google.android.exoplayer2.SimpleExoPlayer
import kohii.v1.core.Common
import kohii.v1.exoplayer.Kohii
import kotlinx.android.synthetic.main.activity_new_post.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.File


private const val FILE_NAME = "camera_team6_"
private const val REQUEST_VIDEO_CAPTURE = 1
private const  val RECORD_REQUEST_CODE = 101
private const  val PICK_VIDEO_CODE = 42
//private var photoUri: Uri? = null

//location of videoFIle
private lateinit var videoFile: File

class NewPostActivity : AppCompatActivity() {
    private var kohii: Kohii? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)
        setupPermissions()
        setOnClickListenerMakeVideo()
        setOnClickListenerUploadVideo()
        kohii = Kohii[this]
        kohii?.register(this)?.addBucket(videoExoFrame)
    }

    private fun setupPermissions() {
        val permissionExternalStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        val permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)

        if (permissionExternalStorage != PackageManager.PERMISSION_GRANTED || permissionCamera != PackageManager.PERMISSION_GRANTED) {
            Log.i("Permission: ", "Permission to access external storage or camera is denied -> making request")
            makeRequest()
        }
    }

    private fun setOnClickListenerMakeVideo() {
        btnMakeVideo.setOnClickListener {
            val takeVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)

            videoFile = getVideoFile(FILE_NAME)
            val fileProvider = FileProvider.getUriForFile(this, "com.example.practice_demo.activity.fileprovider", videoFile)
            //Add extended data to the intent.
            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            //check if any camera in this device
            if (takeVideoIntent.resolveActivity(this.packageManager) != null) {
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)
            } else {
                Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setOnClickListenerUploadVideo() {
        btnUploadVideo.setOnClickListener{
            Log.i("Upload video: ", "function called")
            val videoPickerIntent = Intent(Intent.ACTION_GET_CONTENT)
            videoPickerIntent.type = "video/mp4"
            if (videoPickerIntent.resolveActivity(this.packageManager) != null) {
                startActivityForResult(videoPickerIntent, PICK_VIDEO_CODE)
            }
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA), RECORD_REQUEST_CODE)
    }

    private fun getVideoFile(fileName: String): File {
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".mp4", storageDirectory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //check if successfully take video
        if (resultCode == Activity.RESULT_OK) {
            var videoToView: Uri? = null
            when(requestCode) {
                REQUEST_VIDEO_CAPTURE -> {videoToView = Uri.parse(videoFile.absolutePath)}
                PICK_VIDEO_CODE -> {videoToView = data?.data}
            }
            if (videoToView != null) {
//                kohii?.setUp("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")?.bind(videoExoPlayer)
                kohii?.setUp(videoToView)?.bind(videoExoPlayer)
//                {
//                    preload = true
//                    repeatMode = Common.REPEAT_MODE_ONE
//                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult( requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RECORD_REQUEST_CODE -> {
                var accessToNewPost: Boolean = false;
                grantResults.forEachIndexed {index, element ->
                    if (grantResults.isEmpty() || element != PackageManager.PERMISSION_GRANTED) {
                        Log.i(permissions[index], "Permission has been denied by user")
                    } else {
                        Log.i(permissions[index], "Permission has been granted by user")
                        accessToNewPost = true
                    }

                    when (index) {
                        0 -> {btnUploadVideo.isEnabled = element == 0}
                        1 -> {btnMakeVideo.isEnabled = element == 0}
                    }
                }

                if (!accessToNewPost)
                    finish()
            }
        }
    }
}