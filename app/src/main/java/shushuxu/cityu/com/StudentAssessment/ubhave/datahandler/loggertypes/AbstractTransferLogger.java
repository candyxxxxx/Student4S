package shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.loggertypes;

import android.Manifest;
import android.content.Context;

import com.ubhave.sensormanager.ESException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.config.DataTransferConfig;
import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.except.DataHandlerException;

public abstract class AbstractTransferLogger extends AbstractDataLogger
{
	protected AbstractTransferLogger(final Context context, int storageType) throws DataHandlerException, ESException
	{
		super(context, storageType);
	}
	
	@Override
	protected ArrayList<String> getPermissions(int storageType)
	{
		ArrayList<String> permissions = super.getPermissions(storageType);
		permissions.add(Manifest.permission.INTERNET);
		return permissions;
	}

	@Override
	protected void configureDataStorage() throws DataHandlerException
	{
		super.configureDataStorage();
		dataManager.setConfig(DataTransferConfig.POST_DATA_URL, getDataPostURL());
		dataManager.setConfig(DataTransferConfig.POST_RESPONSE_ON_SUCCESS, getSuccessfulPostResponse());
		HashMap<String, String> params = getPostParameters();
		if (params != null)
		{
			try
			{
				dataManager.setConfig(DataTransferConfig.POST_PARAMETERS, toJSON(params));
			}
			catch (JSONException e)
			{
				throw new DataHandlerException(DataHandlerException.JSON_ERROR);
			}
		}
	}
	
	private JSONObject toJSON(final HashMap<String, String> map) throws JSONException
	{
		JSONObject json = new JSONObject();
		for (String key : map.keySet())
		{
			String value = map.get(key);
			if (value != null)
			{
				json.put(key, value);
			}
		}
		return json;
	}

	protected abstract String getDataPostURL();
	
	protected abstract String getSuccessfulPostResponse();
	
	protected abstract HashMap<String, String> getPostParameters();
}
