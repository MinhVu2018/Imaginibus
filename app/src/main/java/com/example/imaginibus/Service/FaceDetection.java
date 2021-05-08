package com.example.imaginibus.Service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.imaginibus.Model.ImageModel;
import com.example.imaginibus.Utils.MyApplication;
import com.example.imaginibus.Utils.MyRect;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FaceDetection extends Service {
    private Looper serviceLooper;
    private ServiceHandler serviceHandler;
    FaceDetector detector;
    List<ImageModel> allImage;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            faceDetecting();
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work doesn't disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments", Thread.NORM_PRIORITY);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Start detecting faces", Toast.LENGTH_LONG).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    public void onDestroy() {
        Toast.makeText(this, "Face detection completed", Toast.LENGTH_LONG).show();
        Toast.makeText(this, "Start grouping faces", Toast.LENGTH_LONG).show();
        Intent groupingService = new Intent(FaceDetection.this, FaceGrouping.class);
        startService(groupingService);
    }

    private void faceDetecting() {
        //get all image of the computer
        allImage = ((MyApplication) FaceDetection.this.getApplication()).getListImage();

        //Step 1, configure the face detector
        FaceDetectorOptions faceDetectorOptions = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
                .build();

        //Step 2, get an instance of face detector
        detector = com.google.mlkit.vision.face.FaceDetection.getClient(faceDetectorOptions);

        //Step 3, prepare the input image and process it
        faceTracking();
    }

    private void faceTracking() {
        for (int i=0; i<80; i++) {
            //get the image
            ImageModel imageModel = allImage.get(i);

            //Convert to bitmap
            try {
                InputImage image;
                Uri uri = Uri.fromFile(new File(imageModel.getImageUrl()));
                image = InputImage.fromFilePath(this, uri);
                //process image
                processImage(image, i);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processImage(InputImage image, int pos) {
        Task<List<Face>> result;
        result = detector.process(image)
                .addOnSuccessListener(
                        new OnSuccessListener<List<Face>>() {
                            @Override
                            public void onSuccess(List<Face> faces) {
                                if (faces.size() != 0) {
                                    allImage.get(pos).setContainFace(true);
                                    allImage.get(pos).setRect(new MyRect(faces.get(0).getBoundingBox()));
                                } else {
                                    allImage.get(pos).setContainFace(false);
                                    allImage.get(pos).setRect(null);
                                }
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                            }
                        })
                .addOnCompleteListener(
                        new OnCompleteListener<List<Face>>() {
                            @Override
                            public void onComplete(@NonNull Task<List<Face>> task) {
                                return;
                            }
                        });
    }
}
