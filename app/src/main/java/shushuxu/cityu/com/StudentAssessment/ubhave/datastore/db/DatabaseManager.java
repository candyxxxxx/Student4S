package shushuxu.cityu.com.StudentAssessment.ubhave.datastore.db;

import android.content.Context;
import android.util.Log;

import com.ubhave.sensormanager.ESException;

import net.sqlcipher.database.SQLiteDatabase;

import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.ESDataManager;
import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.config.DataStorageConfig;
import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.except.DataHandlerException;
import shushuxu.cityu.com.StudentAssessment.ubhave.datastore.DataStorageInterface;

public class DatabaseManager extends ESDataManager
{	
	public DatabaseManager(final Context context, final String dataPassword) throws ESException, DataHandlerException
	{
		super(context, dataPassword);
		SQLiteDatabase.loadLibs(context);
	}
	
	@Override
	protected DataStorageInterface getStorage(final String dataPassword) throws DataHandlerException
	{
		Log.d(DatabaseStorage.TAG, "Creating database storage in manager");
		return new DatabaseStorage(context, dataPassword);
	}
	
	@Override
	protected final int getStorageType()
	{
		return DataStorageConfig.STORAGE_TYPE_DB;
	}
}
