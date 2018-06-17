package com.aubergine.furr;

/**
 * Created by pinkesh on 24/5/18.
 */

import android.content.Context;
import android.media.Ringtone;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetector;

import java.io.IOException;
import java.util.List;

/** Custom Image Classifier Demo. */
public class ImageLabelingProcessor extends VisionProcessorBase<List<FirebaseVisionLabel>> {

    private static final String TAG = "ImageLabelingProcessor";

    private final FirebaseVisionLabelDetector detector;
    private Context context;
    private Ringtone r;
    private EntityListener entityListener;

    public ImageLabelingProcessor(Context context, EntityListener entityListener) {
        this.context = context;
        this.entityListener = entityListener;
        detector = FirebaseVision.getInstance().getVisionLabelDetector();
    }

    @Override
    public void stop() {
        try {
            detector.close();
        } catch (IOException e) {
            Log.e(TAG, "Exception thrown while trying to close Text Detector: " + e);
        }
    }

    @Override
    protected Task<List<FirebaseVisionLabel>> detectInImage(FirebaseVisionImage image) {
        return detector.detectInImage(image);
    }

    @Override
    protected void onSuccess(
            @NonNull List<FirebaseVisionLabel> labels,
            @NonNull FrameMetadata frameMetadata,
            @NonNull GraphicOverlay graphicOverlay) {
        graphicOverlay.clear();
        // Print the name from the list....
        for(FirebaseVisionLabel object : labels) {
            entityListener.onEntityListen(object.getLabel(),object.getConfidence());
        }
        //LabelGraphic labelGraphic = new LabelGraphic(graphicOverlay, labels);
        //graphicOverlay.add(labelGraphic);
    }

    @Override
    protected void onFailure(@NonNull Exception e) {
        Log.w(TAG, "Label detection failed." + e);
    }
}
