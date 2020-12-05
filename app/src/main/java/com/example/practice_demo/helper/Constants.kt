package com.example.practice_demo.helper

class Constants {
    companion object {
        const val MAX_VIDEO_SIZE = 8388608 //MB 8000000... MiB = 8388608
        const val FILE_NAME = "camera_team6_"
        const val REQUEST_VIDEO_CAPTURE = 1
        const val RECORD_REQUEST_CODE = 101
        const val PICK_VIDEO_CODE = 42
        const val DATE_DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss"
    }

    class Api {
        companion object {
            const val BASE_URL = "http://api.mcomputing.eu/mobv/"
            const val MEDIA_URL = "${BASE_URL}uploads/"
            const val KEY = "kS3lX8pX2aM0rM4hA7kE1aU9sP5rL3"
        }
    }
}