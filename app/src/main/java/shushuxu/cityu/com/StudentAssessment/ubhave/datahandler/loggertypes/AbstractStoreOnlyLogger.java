package shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.loggertypes;

import android.content.Context;

import com.ubhave.sensormanager.ESException;

import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.config.DataTransferConfig;
import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.except.DataHandlerException;

public abstract class AbstractStoreOnlyLogger extends AbstractDataLogger
{
	protected AbstractStoreOnlyLogger(final Context context, final int storageType) throws DataHandlerException, ESException
	{
		super(context, storageType);
	}

	@Override
	protected void configureDataStorage() throws DataHandlerException
	{
		super.configureDataStorage();
		dataManager.setConfig(DataTransferConfig.DATA_TRANSER_POLICY, DataTransferConfig.STORE_ONLY);
	}
}
