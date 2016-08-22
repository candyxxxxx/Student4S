package shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.loggertypes;

import android.Manifest;
import android.content.Context;

import com.ubhave.sensormanager.ESException;

import java.util.ArrayList;

import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.config.DataStorageConfig;
import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.config.DataTransferConfig;
import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.except.DataHandlerException;
import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.transfer.DataUploadCallback;

public abstract class AbstractAsyncTransferLogger extends AbstractTransferLogger
{
	protected AbstractAsyncTransferLogger(final Context context, final int storageType) throws DataHandlerException, ESException
	{
		super(context, storageType);
	}
	
	@Override
	protected ArrayList<String> getPermissions(final int storageType)
	{
		ArrayList<String> permissions = super.getPermissions(storageType);
		permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
		return permissions;
	}

	@Override
	protected void configureDataStorage() throws DataHandlerException
	{
		super.configureDataStorage();
		dataManager.setConfig(DataTransferConfig.DATA_TRANSER_POLICY, DataTransferConfig.TRANSFER_PERIODICALLY);
		dataManager.setConfig(DataStorageConfig.DATA_LIFE_MILLIS, getDataLifeMillis());
		dataManager.setConfig(DataTransferConfig.TRANSFER_ALARM_INTERVAL, getTransferAlarmLengthMillis());
		dataManager.setConfig(DataTransferConfig.POST_KEY, getPostKey());
		dataManager.setConfig(DataTransferConfig.WAIT_FOR_WIFI_INTERVAL_MILLIS, getWaitForWiFiMillis());
	}
	
	protected abstract String getPostKey();

	protected abstract long getDataLifeMillis();

	protected abstract long getTransferAlarmLengthMillis();
	
	protected abstract long getWaitForWiFiMillis();
	
	public void flush(final DataUploadCallback callback) throws DataHandlerException
	{
		dataManager.postAllStoredData(callback);
	}
}
