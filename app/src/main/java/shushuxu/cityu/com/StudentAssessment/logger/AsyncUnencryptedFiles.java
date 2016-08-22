package shushuxu.cityu.com.StudentAssessment.logger;

import android.content.Context;

import com.ubhave.sensormanager.ESException;

import java.util.HashMap;

import shushuxu.cityu.com.StudentAssessment.GlobalApplication;
import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.config.DataStorageConfig;
import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.except.DataHandlerException;
import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.loggertypes.AbstractAsyncTransferLogger;
import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.loggertypes.AbstractDataLogger;

public class AsyncUnencryptedFiles extends AbstractAsyncTransferLogger
{
	private static AsyncUnencryptedFiles instance;

	public static AbstractDataLogger getInstance() throws ESException, DataHandlerException
	{
		if (instance == null)
		{
			instance = new AsyncUnencryptedFiles(GlobalApplication.getContext());
		}
		return instance;
	}

	protected AsyncUnencryptedFiles(final Context context) throws DataHandlerException, ESException
	{
		super(context, DataStorageConfig.STORAGE_TYPE_FILES);
	}

	@Override
	protected String getDataPostURL()
	{
		return RemoteServerDetails.FILE_POST_URL;
	}

	@Override
	protected String getPostKey()
	{
		return RemoteServerDetails.FILE_KEY;
	}

	@Override
	protected String getSuccessfulPostResponse()
	{
		return RemoteServerDetails.RESPONSE_ON_SUCCESS;
	}

	@Override
	protected HashMap<String, String> getPostParameters()
	{
		// Note: any additional parameters (e.g., API key-value) that your URL
		// requires
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(RemoteServerDetails.API_KEY_KEY, RemoteServerDetails.API_KEY_VALUE);
		return params;
	}

	@Override
	protected long getDataLifeMillis()
	{
		// Note: all files older than a minute will be uploaded
		return 1000L * 30;
	}

	@Override
	protected long getTransferAlarmLengthMillis()
	{
		// Note: transfer alarm will fire every 10 minutes
		return 1000L * 60 * 1;
	}

	@Override
	protected String getFileStorageName()
	{
		return "Demo-Unencrypted-Async-Storage";
	}

	@Override
	protected String getUniqueUserId()
	{
		// Note: this should not be a static string
		return "test-user-id";
	}

	@Override
	protected String getDeviceId()
	{
		// Note: this should not be a static string
		return "test-device-id";
	}

	@Override
	protected boolean shouldPrintLogMessages()
	{
		// Note: return false to turn off Log.d messages
		return true;
	}

	@Override
	protected String getEncryptionPassword()
	{
		// Note: return non-null password to encrypt data
		return null;
	}
	
	@Override
	protected long getWaitForWiFiMillis()
	{
		// Note: wait for a Wi-Fi connection for a maximum of 4 hours
		return 1000L * 60 * 60 * 4;
	}
}
