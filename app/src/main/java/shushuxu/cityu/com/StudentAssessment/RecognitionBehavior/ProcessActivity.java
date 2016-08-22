package shushuxu.cityu.com.StudentAssessment.RecognitionBehavior;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import shushuxu.cityu.com.StudentAssessment.Entity.SensoredData;
import shushuxu.cityu.com.StudentAssessment.GlobalApplication;
import shushuxu.cityu.com.StudentAssessment.ubhave.datastore.db.UnencryptedDataTables;

/**
 * Created by shushu on 8/6/16.
 */
public class ProcessActivity extends UnencryptedDataTables {

    private UnencryptedDataTables recogActivityHelper;

    private final String TAG = "ProcessActivity";

    private long total_sleep = 0;
    private long total_study = 0;
    private long total_sports = 0;
    private long total_social = 0;

    private ArrayList<Long> cross_sleep;
    private ArrayList<Long> cross_study;
    private ArrayList<Long> cross_social;

    public ProcessActivity(Context context) {
        super(context);
        recogActivityHelper = new UnencryptedDataTables(context);
    }

    //活动换成时间段

    public synchronized ArrayList<SensoredData> processAct() {

        ArrayList<SensoredData> actList = new ArrayList<>();
        SQLiteDatabase database = recogActivityHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery("select * from recogactivity", null);

        long startTime;
        long endTime;
        String activity;
        boolean flag_act = false;


        cursor.moveToFirst();
        startTime = cursor.getLong(cursor.getColumnIndex("timeStampKey"));
        activity = cursor.getString(cursor.getColumnIndex("data"));
//        Log.d(TAG, "first act" + activity + startTime);
        int row_num = 1;

        while (cursor.moveToNext()) {
            String tem_activity = cursor.getString(cursor.getColumnIndex("data"));

            if (activity.equals(tem_activity)) {
                row_num++;
                continue;
            } else {
                endTime = cursor.getLong(cursor.getColumnIndex("timeStampKey"));
//                Log.d(TAG,"second act" + tem_activity + endTime);

                if (row_num >= 9){
                    SensoredData act = new SensoredData(startTime, endTime, activity);
                    actList.add(act);
//                    Log.d(TAG,"record act" + "start " + startTime + "end " +endTime);
                }
                activity = tem_activity;
                startTime = endTime;
                row_num = 1;
                flag_act = true;
            }
//            Log.d(TAG,"next act" + activity + startTime);
        }

        if(!flag_act) {
            actList.add(new SensoredData(startTime, System.currentTimeMillis(), activity));
        }
//        for (SensoredData sensoredData : actList)
//        {
//            Log.d(TAG, "Activity is " + sensoredData.getActivity() + "Starttime is " + sensoredData.getStartTime() + "Endtime is " + sensoredData.getEndTime());
//        }

        return actList;
//        merge(list);
//        Log.d(TAG, list.size() + "" + list.get(0).getActivity() + list.get(0).getEndTime());
    }

    //voice换成时间段
    public synchronized ArrayList<SensoredData> processVoice()
    {
        ArrayList<SensoredData> voiceList = new ArrayList<>();
        SQLiteDatabase database = recogActivityHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery("select * from recogvoice", null);

        long startTime;
        long endTime;
        String data;

        cursor.moveToFirst();
        startTime = cursor.getLong(cursor.getColumnIndex("timeStampKey"));
        data = cursor.getString(cursor.getColumnIndex("data"));
        Log.d(TAG, "first voice " + data + startTime);
        boolean flag_voice = false;

        while (cursor.moveToNext())
        {
            String tem_data = cursor.getString(cursor.getColumnIndex("data"));

            if (data.equals(tem_data)) {
                continue;
            } else {
                endTime = cursor.getLong(cursor.getColumnIndex("timeStampKey"));
                Log.d(TAG,"second voice " + tem_data + endTime);

                SensoredData voice = new SensoredData(startTime, endTime, data);
                voiceList.add(voice);
                Log.d(TAG,"record voice" + data + "start " + startTime + "end " +endTime);

                data = tem_data;
                startTime = endTime;
                flag_voice = true;
            }
        }
        if(!flag_voice){
            voiceList.add(new SensoredData(startTime, System.currentTimeMillis(), data));
        }
        Log.d(TAG, "voiceList is " + voiceList.size());
        return voiceList;
    }

    //screen换成时间段
    private int flag_screen;

    public synchronized ArrayList<Long> processScreen() throws JSONException {
        ArrayList<Long> screenList = new ArrayList<>();

        SQLiteDatabase db = recogActivityHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from screen", null);

        cursor.moveToFirst();
        String jsonString = cursor.getString(cursor.getColumnIndex("data"));
        JSONObject demoJson = new JSONObject(jsonString);
        String status = demoJson.getString("status");
        Long time = demoJson.getLong("senseStartTimeMillis");
        screenList.add(time);

        if (status.equals("SCREEN_ON"))
            flag_screen = 1;
        else
            flag_screen = 0;

        while (cursor.moveToNext()) {
            String tem_jsonString = cursor.getString(cursor.getColumnIndex("data"));

            JSONObject tem_demoJson = new JSONObject(tem_jsonString);
            Long tem_time = tem_demoJson.getLong("senseStartTimeMillis");

            screenList.add(tem_time);
        }
        return screenList;
    }


    //位置－－－－－－－－－－－－－－－－－－－－－－－－－－
    private int flag_location;
    public synchronized ArrayList<Long> processLocation() {
        ArrayList<Long> locationList = new ArrayList<>();

        SQLiteDatabase db = recogActivityHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from recoglocation", null);
        long startTime;
        long endTime;
        int data;

        cursor.moveToFirst();
        startTime = cursor.getLong(cursor.getColumnIndex("timeStampKey"));
        locationList.add(startTime);
        data = cursor.getInt(cursor.getColumnIndex("data"));
        if (data == 0)
            flag_location = 0;
        else
            flag_location = 1;

        while (cursor.moveToNext())
        {
            int tem_data = cursor.getInt(cursor.getColumnIndex("data"));

            if (data == tem_data) {
                //row_num++;
                continue;
            } else {
                endTime = cursor.getLong(cursor.getColumnIndex("timeStampKey"));
//                Log.d(TAG,"second voice " + tem_data + endTime);

                locationList.add(endTime);
//                Log.d(TAG,"record voice" + data + "start " + startTime + "end " +endTime);

                data = tem_data;
                startTime = endTime;
            }
//            Log.d(TAG,"next voice" + data + startTime);
        }
        for (int i = 0; i < locationList.size(); i++ )
        {
            Log.d(TAG, "Data is " + locationList.get(i));
        }
        return locationList;
    }


    //判断所有
    public synchronized long[] recogFinal(final ArrayList<SensoredData> actlist, final ArrayList<SensoredData> voicelist,
                                          final ArrayList<Long> screenlist, final ArrayList<Long> locationlist)
    {
        total_sleep = 0;
        total_social = 0;
        total_sports = 0;
        total_study = 0;

        final long[] data = new long[4];

        data[0] = calStudy(actlist, voicelist, locationlist);
        data[1] = calSports(actlist);
        data[2] = calSleep(actlist, voicelist, screenlist);
        data[3] = calSocial(voicelist,locationlist);

        return data;
    }

    public long calStudy(ArrayList<SensoredData> actlist, final ArrayList<SensoredData> voicelist, ArrayList<Long> locationlist)
    {
        cross_study = new ArrayList<>();
        for(SensoredData act : actlist)
        {
            if (act.getActivity().equals("Still"))
            {
                Log.d(TAG, "here-----------------");
                for(SensoredData voice : voicelist) {
                    if (voice.getActivity().equals("1"))
                        crossAVTime(act.getStartTime(), act.getEndTime(), voice.getStartTime(), voice.getEndTime(), cross_study);
                }
            }
        }

        for (int i = flag_location + 1; i < locationlist.size() - 1; i += 2)
        {
            for (int j = 0; j < cross_study.size() - 1; j += 2)
            {
                total_study += crossAVTime(locationlist.get(i), locationlist.get(i + 1), cross_study.get(j), cross_study.get(j + 1), new ArrayList<Long>());
            }

        }

        return total_study;
    }

    public long calSleep(ArrayList<SensoredData> actlist, ArrayList<SensoredData> voicelist, ArrayList<Long> screenlist) {

        cross_sleep = new ArrayList<>();
        if( screenlist.size() <= 3)
        {
            Toast.makeText(GlobalApplication.getContext(), "No data!", Toast.LENGTH_SHORT).show();
            return 0;
        }

        for (int i = flag_screen; i < screenlist.size() - 1; i += 2)
        {
            long off_time = screenlist.get(i + 1) - screenlist.get(i);
            if (off_time >= 5 * 1000 * 60 * 60)
            {
                for (SensoredData sensoredData : actlist)
                {
                    if (sensoredData.getActivity().equals("Still"))
                        crossAVTime(screenlist.get(i), screenlist.get(i + 1), sensoredData.getStartTime(), sensoredData.getEndTime(), cross_sleep);
                }
            }
        }

        for (int i = 0; i < cross_sleep.size() - 1; i += 2)
        {
            for (SensoredData sensoredData : voicelist)
            {
                if (sensoredData.getActivity().equals("0")) {
                    total_sleep += crossAVTime(cross_sleep.get(i), cross_sleep.get(i + 1), sensoredData.getStartTime(), sensoredData.getEndTime(), new ArrayList<Long>());
                }
            }
        }
        return total_sleep;

    }


    public long calSports(ArrayList<SensoredData> actlist)
    {
        for (SensoredData sensoredData : actlist)
        {
            if (sensoredData.getActivity().equals("Walking"))
            {
                long minutes = (sensoredData.getEndTime() - sensoredData.getStartTime()) / (1000 * 60);
                total_sports += minutes;
            }
        }
        return total_sports;
    }

    public long calSocial(ArrayList<SensoredData> voicelist, ArrayList<Long> locationlist) {

        cross_social = new ArrayList<>();
        if (locationlist.size() == 1 && flag_location == 0) {

            for (int i = 0; i < voicelist.size(); i++)
            {
                if (voicelist.get(i).getActivity().equals("2")) {
                    long period = (voicelist.get(i).getEndTime() - voicelist.get(i).getStartTime()) / (1000 * 60);
                    total_social += period;
                }
            }
        }
        else if (locationlist.size() == 2) {
            if (flag_location == 1) {
                long location_time = locationlist.get(1);
                Log.d(TAG, "voice" + voicelist.size());
                for (int i = 0; i < voicelist.size(); i++)
                {
                    if (voicelist.get(i).getActivity().equals("2") && voicelist.get(i).getStartTime() > location_time) {
                        long period = (voicelist.get(i).getEndTime() - voicelist.get(i).getStartTime()) / (1000 * 60);
                        total_social += period;
                        Log.d(TAG, "size 2.1 " + period);
                    }
                    else if (voicelist.get(i).getActivity().equals("2") && voicelist.get(i).getEndTime() > location_time && voicelist.get(i).getStartTime() < location_time) {
                        long period = (voicelist.get(i).getEndTime() - location_time) / (1000 * 60);
                        total_social += period;
                        Log.d(TAG, "size 2.2 " + period);
                    }
                }
            }
            else {
                long location_time = locationlist.get(1);
                Log.d(TAG, "voice" + voicelist.size());
                for (int i = 0; i < voicelist.size(); i++)
                {
                    if (voicelist.get(i).getActivity().equals("2") && voicelist.get(i).getEndTime() < location_time) {
                        long period = (voicelist.get(i).getEndTime() - voicelist.get(i).getStartTime()) / (1000 * 60);
                        total_social += period;
                        Log.d(TAG, "size 2.3 " + period);
                    }
                    else if (voicelist.get(i).getActivity().equals("2") && voicelist.get(i).getEndTime() > location_time && voicelist.get(i).getStartTime() < location_time) {
                        long period = (location_time - voicelist.get(i).getStartTime()) / (1000 * 60);
                        total_social += period;
                        Log.d(TAG, "size 2.4 " + period);
                    }
                }

            }
        }
        else
        {
            for (int i = flag_location; i < locationlist.size() - 1; i += 2)
            {

                for (SensoredData sensoredData : voicelist)
                {
                    if (sensoredData.getActivity().equals("2"))
                        total_social += crossAVTime(locationlist.get(i), locationlist.get(i + 1), sensoredData.getStartTime(), sensoredData.getEndTime(), cross_social);
                }

            }
        }

        Log.d(TAG, "total is " + total_social );
        return total_social;
    }

    public synchronized long crossAVTime(long time_start1, long time_end1, long time_start2, long time_end2, ArrayList<Long> cross)
    {
        long cross_time = 0;

        if (time_start1 < time_end2 && time_end1 > time_start2)
        {
            long[] time = {time_start1, time_end1, time_start2, time_end2};
            Arrays.sort(time);
//            Log.d(TAG, "time1 " + time[1]);
//            Log.d(TAG, "time2 " + time[2]);
            long minutes = (time[2] - time[1]) / (1000 * 60);
            cross.add(time[1]);
            cross.add(time[2]);
            cross_time += minutes;
        }
        return cross_time;
    }

    //待开发

    public synchronized void recordData(final ArrayList<SensoredData> list)
    {
        SQLiteDatabase database = recogActivityHelper.getWritableDatabase();
        for (SensoredData data : list)
        {
            ContentValues values = new ContentValues();
            values.put("data", data.getActivity());
            values.put("startTimeStamp", data.getStartTime());
            values.put("endTimeStamp", data.getEndTime());
            //database.insert("processactivity", null, values);
        }
    }

    public synchronized void removeData(String table_name) {

        SQLiteDatabase database = recogActivityHelper.getWritableDatabase();
        database.delete(table_name, null, null);
    }
}
