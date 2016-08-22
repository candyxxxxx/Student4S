package shushuxu.cityu.com.StudentAssessment.ubhave.datastore.db;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.DetectedActivity;
import com.ubhave.sensormanager.data.SensorData;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import shushuxu.cityu.com.StudentAssessment.RecognitionBehavior.Constants;
import shushuxu.cityu.com.StudentAssessment.ubhave.dataformatter.json.JSONFormatter;
import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.config.DataHandlerConfig;

import static shushuxu.cityu.com.StudentAssessment.RecognitionBehavior.Constants.getActivityString;

public class UnencryptedDataTables extends SQLiteOpenHelper implements DataTablesInterface
{
	private final static Object lock = new Object();
	private final static int dbVersion = 3;
	private static final String DATABASE_NAME = "sensor_datastore";

	private final HashMap<String, UnencryptedDataTable> dataTableMap;

	// -----------------------------Broadcast
	protected ActivityDetectionBroadcastReceiver mBroadcastReceiver;

	private String recogActivityQuery = "CREATE TABLE IF NOT EXISTS recogactivity ("
			+ "data TEXT NOT NULL, "
			+ "timeStampKey INTEGER NOT NULL "
			+ ");";

	private String recogVoiceQuery = "CREATE TABLE IF NOT EXISTS recogvoice ("
			+ "data INTEGER NOT NULL, "
			+ "decibel INTEGER, "
			+ "timeStampKey INTEGER NOT NULL "
			+ ");";

	public static final String recogLocationQuery = "CREATE TABLE IF NOT EXISTS recoglocation ("
			+ "data TEXT NOT NULL, "
			+ "timeStampKey INTEGER NOT NULL "
			+ ");";


	public UnencryptedDataTables(final Context context)
	{
		super(context, DATABASE_NAME, null, dbVersion);
		Log.d(DatabaseStorage.TAG, "UnencryptedDataTables constructor: "+DATABASE_NAME);
		this.dataTableMap = new HashMap<String, UnencryptedDataTable>();

		//----------------------------Broadcast
		// Get a receiver for broadcasts from ActivityDetectionIntentService.
		mBroadcastReceiver = new ActivityDetectionBroadcastReceiver();
		LocalBroadcastManager.getInstance(context).registerReceiver(mBroadcastReceiver,
				new IntentFilter(Constants.BROADCAST_ACTION));
	}

	@Override
	public void onCreate(final SQLiteDatabase db)
	{
		Log.d(DatabaseStorage.TAG, "Database onCreate()");
		for (UnencryptedDataTable table : dataTableMap.values())
		{
			if (DataHandlerConfig.shouldLog())
			{
				Log.d(DatabaseStorage.TAG, "Creating table in onCreate(): "+table.getName()+".");
			}
			Log.d("create---------------", table.getName());
			table.createTable(db);
		}
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}


	@Override
	public void onOpen(SQLiteDatabase db) {
		//super.onOpen(db);
		for (UnencryptedDataTable table : dataTableMap.values())
		{
			if (DataHandlerConfig.shouldLog())
			{
				Log.d(DatabaseStorage.TAG, "notdropppppppppppppp: "+table.getName()+".");

			}
			Log.d("onOpen---------------", table.getName());
			db.execSQL(table.getCreateTableQuery());
			db.execSQL(recogActivityQuery);
			db.execSQL(recogVoiceQuery);
			db.execSQL(recogLocationQuery);
		}

	}

	public Set<String> getTableNames()
	{
		return dataTableMap.keySet();
	}

	protected UnencryptedDataTable getTable(final String tableName)
	{
		UnencryptedDataTable table = dataTableMap.get(tableName);
		if (table == null)
		{
			if (DataHandlerConfig.shouldLog())
			{
				Log.d(DatabaseStorage.TAG, "Adding: "+tableName+" to table map.");
			}
			table = new UnencryptedDataTable(tableName);
			dataTableMap.put(tableName, table);
		}
		return table;
	}

	@Override
	public synchronized void writeData(final String tableName, final String data)
	{
		if (DataHandlerConfig.shouldLog())
		{
			Log.d(DatabaseStorage.TAG, "Writing to table: "+tableName+".");
		}
		
		UnencryptedDataTable table = getTable(tableName);
		SQLiteDatabase database = getWritableDatabase();
		database.beginTransaction();
		try
		{
			table.add(database, System.currentTimeMillis(), data);
			database.setTransactionSuccessful();
		}
		catch (Exception e)
		{
			Log.d(DatabaseStorage.TAG, ""+e.getLocalizedMessage());
			e.printStackTrace();
		}
		finally
		{
			database.endTransaction();
//			database.close();
//			close();
		}
	}

	@Override
	public List<SensorData> getRecentSensorData(final String tableName, final JSONFormatter formatter, final long timeLimit)
	{
		synchronized (lock)
		{
			List<SensorData> data = null;
			UnencryptedDataTable table = getTable(tableName);
			SQLiteDatabase database = getReadableDatabase();
			database.beginTransaction();
			try
			{
				data = table.getRecentData(database, formatter, timeLimit);
				database.setTransactionSuccessful();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				database.endTransaction();
				database.close();
			}
			return data;
		}
	}

	@Override
	public List<JSONObject> getUnsyncedData(final String tableName, final long maxAge)
	{
		if (DataHandlerConfig.shouldLog())
		{
			Log.d(DatabaseStorage.TAG, "Get unsynced data from: "+tableName+".");
		}
		synchronized (lock)
		{
			List<JSONObject> data = null;
			UnencryptedDataTable table = getTable(tableName);
			SQLiteDatabase database = getReadableDatabase();
			database.beginTransaction();
			try
			{
				data = table.getUnsyncedData(database, maxAge);
				database.setTransactionSuccessful();
				Log.d(DatabaseStorage.TAG, "Retrieved "+data.size()+" entries.");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				database.endTransaction();
				database.close();
			}	
			return data;
		}
	}

	@Override
	public void setSynced(final String tableName, final long syncTime)
	{
		synchronized (lock)
		{
			UnencryptedDataTable table = getTable(tableName);
			SQLiteDatabase database = getWritableDatabase();
			database.beginTransaction();
			try
			{
				table.setSynced(database, syncTime);
				database.setTransactionSuccessful();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				database.endTransaction();
				database.close();
			}
		}
	}

	/**
	 * Receiver for intents sent by DetectedActivitiesIntentService via a sendBroadcast().
	 * Receives a list of one or more DetectedActivity objects associated with the current state of
	 * the device.
	 */
	public class ActivityDetectionBroadcastReceiver extends BroadcastReceiver {
		protected static final String TAG = "activity-detection----";

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG,"-------------");
			ArrayList<DetectedActivity> updatedActivities =
					intent.getParcelableArrayListExtra(Constants.ACTIVITY_EXTRA);
			ArrayList<String> recogActivity = new ArrayList<>();
			for(int i = 0; i < updatedActivities.size(); i++){
				recogActivity.add(i, getActivityString(context, updatedActivities.get(i).getType()));
				Log.d(TAG, recogActivity.get(i));
			}
			insertData("recogactivity", recogActivity);
		}
	}


	public synchronized void insertData(final String tableName, final ArrayList<String> activity)
	{

			UnencryptedDataTable table = getTable(tableName);
			SQLiteDatabase database = getWritableDatabase();
			//database.beginTransaction();
		Log.d("RECOG------", "");
			if(tableName.equals("recogactivity"))
			{
				try
				{

					for(String actString: activity){
						//table.add(database, System.currentTimeMillis(), actString);
						ContentValues values = new ContentValues();
						values.put("data", actString);
						values.put("timeStampKey", System.currentTimeMillis());
						database.insert(tableName, null, values);
						//database.setTransactionSuccessful();
					}

				}
				catch (Exception e)
				{
					Log.d(DatabaseStorage.TAG, ""+e.getLocalizedMessage());
					e.printStackTrace();
				}
				finally
				{
					//database.endTransaction();
					database.close();
					close();
				}
			}
//			else if (tableName.equals("recogvoice"))
//			{
//				Log.d("thereeeee-----------"," ");
//
//			}

	}


}
