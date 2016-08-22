package shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.transfer;

import android.content.Context;

import java.io.File;

import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.except.DataHandlerException;
import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.transfer.async.UploadVault;
import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.transfer.async.UploadVaultInterface;

public class DataTransfer implements DataTransferInterface
{
	public final static String TAG = "DataTransfer";
	private final UploadVaultInterface uploadVault;

	public DataTransfer(final Context context, final String dataPassword)
	{
		this.uploadVault = UploadVault.getInstance(context, dataPassword);
	}

	@Override
	public void uploadData(final DataUploadCallback[] callbacks) throws DataHandlerException
	{
		File directory = uploadVault.getUploadDirectory();
		new FilesPostThread(directory, callbacks).start();
	}
}
