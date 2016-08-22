package shushuxu.cityu.com.StudentAssessment.Entity;

/**
 * Created by shushu on 8/4/16.
 */
public class SensoredData {

    private long startTime;
    private long endTime;
    private String activity;

    public SensoredData(long time1, long time2, String act) {
        startTime = time1;
        endTime = time2;
        activity = act;
    }


//    public void setTime(Long time) {
//        mTime = time;
//    }
//


    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public String getActivity() {
        return activity;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    //
//    public void setmAmplitude(int amplitude) {
//        mAmplitude = amplitude;
//    }
//
//    public int getmAmplitude(){
//        return mAmplitude;
//    }
}
