package shushuxu.cityu.com.StudentAssessment;

import android.util.Log;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.loggertypes.AbstractDataLogger;

/**
 * Created by shushu on 7/15/16.
 */
public class SenseOnceThread extends Thread implements SensorDataListener {
    private final int sensorType;
    private final ESSensorManager sensorManager;
    private final AbstractDataLogger logger;

    private boolean stop;
    private int subscriptionId;

    public SenseOnceThread(final ESSensorManager sensorManager, AbstractDataLogger logger, int sensorType)
    {
        this.sensorManager = sensorManager;
        this.logger = logger;
        this.sensorType = sensorType;

        this.stop = false;
    }

    //---------------------
    public void stopSensing()
    {
        try
        {
            stop = true;
            sensorManager.unsubscribeFromSensorData(subscriptionId);
        }
        catch (ESException e)
        {
            Log.d("Service Error", e.getLocalizedMessage());
        }
    }


//    @Override
//    public void run()
//    {
//        try
//        {
//            SensorData data = sensorManager.getDataFromSensor(sensorType);
//            if (data != null)
//            {
//                logger.logSensorData(data);
//                Log.d("Test", "Finished sensing: "+ SensorUtils.getSensorName(sensorType));
//            }
//            else
//            {
//                Log.d("Test", "Finished sensing: null data");
//            }
//
//        }
//        catch (ESException e)
//        {
//            Log.d("Service Error", e.getLocalizedMessage());
//        }
//    }

    @Override
    public void run()
    {
        try
        {
            Log.d("here-----------------", "");
            subscriptionId = sensorManager.subscribeToSensorData(sensorType, this);
            while (!stop)
            {
                sleep(500);
            }
        }
        catch (Exception e)
        {
            Log.d("Service Error", e.getLocalizedMessage());
        }
    }

    @Override
    public void onDataSensed(SensorData data) {

        try
        {
            Log.d("Test", "Received from: " + SensorUtils.getSensorName(sensorType));
            logger.logSensorData(data);
        }
        catch (Exception e)
        {
            Log.d("Service Error", e.getLocalizedMessage());
        }
    }

    @Override
    public void onCrossingLowBatteryThreshold(boolean isBelowThreshold) {

    }
}

