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
public class SubscribeThread extends Thread implements SensorDataListener
{
    private final int sensorType;
    private final ESSensorManager sensorManager;
    private final AbstractDataLogger logger;

    private int subscriptionId;
    private boolean stop;

    public SubscribeThread(final ESSensorManager sensorManager, AbstractDataLogger logger, int sensorType)
    {
        this.sensorType = sensorType;
        this.sensorManager = sensorManager;
        this.logger = logger;
        this.stop = false;
    }

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
    public void onDataSensed(SensorData data)
    {
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
    public void onCrossingLowBatteryThreshold(boolean isBelowThreshold)
    {
        // TODO: what happens when there is low battery?
    }
}

