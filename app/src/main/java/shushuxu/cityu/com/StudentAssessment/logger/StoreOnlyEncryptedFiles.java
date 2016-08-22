package shushuxu.cityu.com.StudentAssessment.logger;

import android.content.Context;
import android.util.Log;

import com.ubhave.sensormanager.ESException;

import shushuxu.cityu.com.StudentAssessment.GlobalApplication;
import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.config.DataStorageConfig;
import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.except.DataHandlerException;
import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.loggertypes.AbstractDataLogger;
import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.loggertypes.AbstractStoreOnlyLogger;

public class StoreOnlyEncryptedFiles extends AbstractStoreOnlyLogger
{
	private static StoreOnlyEncryptedFiles instance;
	
	public static AbstractDataLogger getInstance() throws ESException, DataHandlerException
	{
		if (instance == null)
		{
			instance = new StoreOnlyEncryptedFiles(GlobalApplication.getContext());
		}
		return instance;
	}

	protected StoreOnlyEncryptedFiles(final Context context) throws DataHandlerException, ESException
	{
		super(context, DataStorageConfig.STORAGE_TYPE_FILES);
	}

	@Override
	protected String getFileStorageName()
	{
		return "Demo-Encrypted-File-Storage";
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
		Log.d("DetectionService", "getEncryptionPassword()");
		// Note: return non-null password to encrypt data
		return "password";
	}
}
