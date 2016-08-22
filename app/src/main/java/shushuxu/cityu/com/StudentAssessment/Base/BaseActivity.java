package shushuxu.cityu.com.StudentAssessment.Base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by shushu on 7/7/16.
 */
public class BaseActivity extends SupportActivity {

//    private AlarmManager alarm = null;
//    private Calendar calendar = Calendar.getInstance();//Calendar是一类可以将时间转化成绝对时间
//    //毫秒数的一个类
//    private final int HourOfDay = 8;//定时更新的小时
//
//    private final int TIME_INTERVAL=1000*60*60*24;//set the interval of the alarm repeating

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

//        //the function of setAlarm and send broad cast
//        this.setAlarm();
    }

//    private void setAlarm(){
//        Intent intent=new Intent(this,SecondTabFragment.class);
//        //create the Intent between activity and broadcast
//
//        this.alarm=(AlarmManager)super.getSystemService(Context.ALARM_SERVICE);
//        //get the alarm service
//        this.calendar.set(Calendar.HOUR_OF_DAY,this.HourOfDay);
//        //set the hour of the calendar to the value that we want
//        this.calendar.set(Calendar.MINUTE, 0);
//        //set the minute of the calendar to 0
//        this.calendar.set(Calendar.SECOND, 0);
//        //set the minute of the calendar to 0
//        this.calendar.set(Calendar.MILLISECOND,0);
//        //set the millsecond of the calendar to 0
//
//        intent.setAction("org.long.action.setalarm");//define the action of intent
//        PendingIntent sender=PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        //define the PendingIntent
//        this.alarm.setRepeating(AlarmManager.RTC_WAKEUP, this.calendar.getTimeInMillis(),
//                this.TIME_INTERVAL,sender);//set the properties of alarm
//        //send broatcast at the same time
//    }

}
