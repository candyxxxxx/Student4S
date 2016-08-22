package shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.transfer;


import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.except.DataHandlerException;

public interface DataTransferInterface
{
	/*
	 * Transferring stored data
	 */
	public void uploadData(final DataUploadCallback[] callback) throws DataHandlerException;
	
}
