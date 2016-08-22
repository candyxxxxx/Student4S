package shushuxu.cityu.com.StudentAssessment;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.sensors.SensorUtils;

import shushuxu.cityu.com.StudentAssessment.logger.StoreOnlyUnencryptedDatabase;
import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.loggertypes.AbstractDataLogger;

public class DetectionService extends Service {

    private final static String LOG_TAG = "DetectionService";

    private AbstractDataLogger logger;
    private ESSensorManager sensorManager;

    private SubscribeThread[] subscribeThreads;
    private SenseOnceThread[] pullThreads;

    // TODO: add push sensors you want to sense from here
    private final int[] pushSensors = { SensorUtils.SENSOR_TYPE_BATTERY, SensorUtils.SENSOR_TYPE_SCREEN};

    // TODO: add pull sensors you want to sense once from here
    private final int[] pullSensors = { SensorUtils.SENSOR_TYPE_WIFI, SensorUtils.SENSOR_TYPE_MICROPHONE};

    @Override
    public void onCreate()
    {
        super.onCreate();

        try
        {
            // TODO: change this line of code to change the type of data logger
            // Note: you shouldn't have more than one logger!
//			logger = AsyncEncryptedDatabase.getInstance();
//          logger = AsyncWiFiOnlyEncryptedDatabase.getInstance();
//			logger = AsyncEncryptedFiles.getInstance();
//			logger = AsyncUnencryptedDatabase.getInstance();
//			logger = AsyncUnencryptedFiles.getInstance();
//			logger = StoreOnlyEncryptedDatabase.getInstance();
//			logger = StoreOnlyEncryptedFiles.getInstance();
            logger = StoreOnlyUnencryptedDatabase.getInstance();
//			logger = StoreOnlyUnencryptedFiles.getInstance();
            sensorManager = ESSensorManager.getSensorManager(this);

            // Example of starting some sensing in onCreate()
            // Collect a single sample from the listed pull sensors
            pullThreads = new SenseOnceThread[pullSensors.length];
            for (int i = 0; i < pullSensors.length; i++)
            {
                pullThreads[i] = new SenseOnceThread(sensorManager, logger, pullSensors[i]);
                pullThreads[i].start();
            }

            subscribeThreads = new SubscribeThread[pushSensors.length];
            for (int i = 0; i < pushSensors.length; i++)
            {
                subscribeThreads[i] = new SubscribeThread(sensorManager, logger, pushSensors[i]);
                subscribeThreads[i].start();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this, "" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
           // Log.d(LOG_TAG, e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        // TODO Auto-generated method stub
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification noti = new Notification.Builder(this)
                .setContentTitle("Title")
                .setContentText("Message")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(12346, noti);
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        for (SubscribeThread thread : subscribeThreads)
        {
            thread.stopSensing();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}


