package com.example.smartredact.common.constants

object Constants {

    const val KEY_SESSION = "KEY_SESSION"
    const val IS_FIRST_LAUNCHER = "IS_FIRST_LAUNCHER"

    object SharedPreferences {
        const val NAME = "SHARED_PREFERENCES_SMART_REDACT"
    }

    object FaceDetection {

        const val YOLO_MODEL_FILE = "yolo2_face.tflite"
        const val YOLO_INPUT_SIZE = 416
        const val YOLO_BLOCK_SIZE = 32

        const val MINIMUM_CONFIDENCE_YOLO = 0.5
    }
}