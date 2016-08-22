package shushuxu.cityu.com.StudentAssessment.RecognitionBehavior;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

import static shushuxu.cityu.com.StudentAssessment.RecognitionBehavior.Constants.getActivityString;

/**
 * Created by shushu on 7/31/16.
 */
public class DetectedActivitiesIntentService extends IntentService{

    protected static final String TAG = "DetectedActivitiesIS";

    /**
     * This constructor is required, and calls the super IntentService(String)
     * constructor with the name for a worker thread.
     */

    public DetectedActivitiesIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Handles incoming intents.
     * @param intent The Intent is provided (inside a PendingIntent) when requestActivityUpdates()
     *               is called.
     */

    @Override
    protected void onHandleIntent(Intent intent) {

        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

        DetectedActivity probActivity = result.getMostProbableActivity();

        //
        ArrayList<DetectedActivity> detectedActivity = new ArrayList<>();
        detectedActivity.add(probActivity);

        // ---------------Broadcast
        Intent localIntent = new Intent(Constants.BROADCAST_ACTION);

        // Log each activity.
        Log.i(TAG, "activity detected");
        for (int j = 0; j < detectedActivity.size(); j++) {
            DetectedActivity da = detectedActivity.get(j);
            Log.i(TAG, getActivityString(getApplicationContext(),
                    probActivity.getType()) + " " + probActivity.getConfidence() + "%");
        }

        //String activityString = Constants.getActivityString(getApplicationContext(), probActivity.getType());

        // Broadcast the list of detected activities.
        localIntent.putExtra(Constants.ACTIVITY_EXTRA, detectedActivity);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

    }

}
