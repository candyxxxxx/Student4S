package shushuxu.cityu.com.StudentAssessment.ubhave.datastore.file;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.except.DataHandlerException;


public class FileStoreReader
{
	protected FileVault vault;

	public FileStoreReader(final FileVault vault)
	{
		this.vault = vault;
	}
	
	public List<JSONObject> readFile(final String directory, final File dataFile) throws DataHandlerException
	{
		List<JSONObject> entries = new ArrayList<JSONObject>();
		try
		{
			InputStream stream = vault.openForReading(dataFile);
			BufferedReader in = new BufferedReader(new InputStreamReader(stream));
			String line;
			while ((line = in.readLine()) != null)
			{
				JSONObject entry = new JSONObject(line);
				entries.add(entry);
			}
			in.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new DataHandlerException(DataHandlerException.IO_EXCEPTION);
		}
		return entries;
	}
}
