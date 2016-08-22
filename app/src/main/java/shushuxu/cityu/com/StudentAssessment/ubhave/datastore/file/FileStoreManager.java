package shushuxu.cityu.com.StudentAssessment.ubhave.datastore.file;

import android.content.Context;

import com.ubhave.sensormanager.ESException;

import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.ESDataManager;
import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.config.DataStorageConfig;
import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.except.DataHandlerException;
import shushuxu.cityu.com.StudentAssessment.ubhave.datastore.DataStorageInterface;

public class FileStoreManager extends ESDataManager
{	
	public FileStoreManager(final Context context, final String dataPassword) throws ESException, DataHandlerException
	{
		super(context, dataPassword);
	}
	
	@Override
	protected DataStorageInterface getStorage(final String dataPassword)
	{
		return new FileStorage(context, dataPassword, fileTransferLock);
	}
	
	@Override
	protected final int getStorageType()
	{
		return DataStorageConfig.STORAGE_TYPE_FILES;
	}
}
