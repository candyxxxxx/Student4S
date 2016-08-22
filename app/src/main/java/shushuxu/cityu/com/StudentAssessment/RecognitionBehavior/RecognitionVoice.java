package shushuxu.cityu.com.StudentAssessment.RecognitionBehavior;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import shushuxu.cityu.com.StudentAssessment.Entity.SensoredData;
import shushuxu.cityu.com.StudentAssessment.ubhave.datastore.db.UnencryptedDataTables;

/**
 * Created by shushu on 8/3/16.
 */
public class RecognitionVoice extends UnencryptedDataTables {

    private UnencryptedDataTables recogVoiceHelper;

    private String TAG = "RecognitionVoice";

    private String sql = "select data from microphone";

    private final Double silence = 60.00;
    private final Double indoor = 70.00;
    private final Double outdoor = 90.00;


    public RecognitionVoice(Context context) {
        super(context);
        recogVoiceHelper = new UnencryptedDataTables(context);
    }

    public synchronized void getAlldata() throws JSONException {

        SQLiteDatabase db = recogVoiceHelper.getWritableDatabase();

        db.delete("recogvoice", null, null);

        List<SensoredData> micData = new ArrayList<>();

        Cursor cursor = db.rawQuery(sql, null);

        int count = 0;

        while (cursor.moveToNext())
        {
            String jsonString = cursor.getString(cursor.getColumnIndex("data"));

            JSONObject demoJson = new JSONObject(jsonString);
            JSONArray amplitudeList = demoJson.getJSONArray("amplitude");
            JSONArray timeStampList = demoJson.getJSONArray("sensorTimeStamps");

            double avgAmplitude = 0;
            int recogVoice = 0;
            Long timeStamp = timeStampList.getLong(0);
            int aValue ;

            for (int i=0; i<amplitudeList.length(); i++)
            {
                aValue = amplitudeList.getInt(i);
                avgAmplitude += aValue;
                //timeStamp = timeStampList.getLong(i);
            }
            avgAmplitude = avgAmplitude / (double)amplitudeList.length();
            double VoiceDb = 20 * Math.log10(avgAmplitude);
            count++;

            if (VoiceDb <= silence)
            {
                recogVoice = 0;
            }
            else if (VoiceDb > silence && VoiceDb <= indoor)
            {
                recogVoice = 1;
            }
            else if (VoiceDb > indoor)
            {
                recogVoice = 2;
            }

            ContentValues values = new ContentValues();
            values.put("data", recogVoice);
            values.put("timeStampKey", timeStamp);
            values.put("decibel", VoiceDb);
            db.insert("recogvoice", null, values);

        }
    }

//    private PendingIntent getVoiceDetectionPendingIntent() {
//        Intent intent = new Intent(context, DetectedActivitiesIntentService.class);
//
//        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
//        // requestActivityUpdates() and removeActivityUpdates().
//        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//    }

}

