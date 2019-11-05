package com.example.smartredact.common.facerecoginition;

public interface config {
    int INPUT_SIZE = 416;

    String MODEL_FILE = "yolo2_face.tflite";
    String LABEL_FILE = "tiny-yolo-face-labels.txt";
    String LOGGING_TAG = "----------YOLO--------";
}
