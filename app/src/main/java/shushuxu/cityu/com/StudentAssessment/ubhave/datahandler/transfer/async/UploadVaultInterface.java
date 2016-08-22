package shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.transfer.async;


import org.json.JSONObject;

import java.io.File;
import java.util.List;

import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.except.DataHandlerException;

public interface UploadVaultInterface
{
	void writeData(final String dataName, final List<JSONObject> data) throws Exception;
	
	boolean isUploadDirectory(final File directory) throws DataHandlerException;
	
	File getUploadDirectory() throws DataHandlerException;
}
