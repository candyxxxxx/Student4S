package shushuxu.cityu.com.StudentAssessment.ubhave.datahandler;

import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.data.SensorData;

import java.io.IOException;
import java.util.List;

import shushuxu.cityu.com.StudentAssessment.ubhave.dataformatter.DataFormatter;
import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.except.DataHandlerException;
import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.transfer.DataUploadCallback;

public interface ESDataManagerInterface
{
	/*
	 * Updating Data Manager config
	 */
	void setConfig(final String key, final Object value) throws DataHandlerException;
	Object getConfig(final String key)  throws DataHandlerException;
	
	/*
	 * Logging/storing data
	 */
	void logSensorData(final SensorData data) throws DataHandlerException;
    void logSensorData(final SensorData data, DataFormatter formatter) throws DataHandlerException;
	void logExtra(final String tag, final String data) throws DataHandlerException;
	
	/*
	 * Retrieving logged data
	 */
	List<SensorData> getRecentSensorData(int sensorId, long startTimestamp) throws DataHandlerException, ESException, IOException;
	
	/*
	 * Uploading stored data
	 */
	void postAllStoredData(final DataUploadCallback callback) throws DataHandlerException;
	
}
