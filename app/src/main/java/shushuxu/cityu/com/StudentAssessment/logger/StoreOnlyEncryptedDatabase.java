package shushuxu.cityu.com.StudentAssessment.logger;

import android.content.Context;

import com.ubhave.sensormanager.ESException;

import shushuxu.cityu.com.StudentAssessment.GlobalApplication;
import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.config.DataStorageConfig;
import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.except.DataHandlerException;
import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.loggertypes.AbstractDataLogger;
import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.loggertypes.AbstractStoreOnlyLogger;

public class StoreOnlyEncryptedDatabase extends AbstractStoreOnlyLogger
{
	private static StoreOnlyEncryptedDatabase instance;
	
	public static AbstractDataLogger getInstance() throws ESException, DataHandlerException
	{
		if (instance == null)
		{
			instance = new StoreOnlyEncryptedDatabase(GlobalApplication.getContext());
		}
		return instance;
	}

	protected StoreOnlyEncryptedDatabase(Context context) throws DataHandlerException, ESException
	{
		super(context, DataStorageConfig.STORAGE_TYPE_DB);
	}

	@Override
	protected String getFileStorageName()
	{
		// Unused for database storage
		return null;
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
		return "password";
	}
}
