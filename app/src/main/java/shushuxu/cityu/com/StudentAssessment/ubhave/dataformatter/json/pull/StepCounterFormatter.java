/* **************************************************
 Copyright (c) 2015, University of Cambridge
 Neal Lathia, neal.lathia@cl.cam.ac.uk

This library was developed as part of the EPSRC Ubhave (Ubiquitous and
Social Computing for Positive Behaviour Change) Project. For more
information, please visit http://www.emotionsense.org

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 ************************************************** */

package shushuxu.cityu.com.StudentAssessment.ubhave.dataformatter.json.pull;

import android.content.Context;

import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pull.StepCounterData;
import com.ubhave.sensormanager.process.AbstractProcessor;
import com.ubhave.sensormanager.process.pull.StepCounterProcessor;
import com.ubhave.sensormanager.sensors.SensorUtils;

import org.json.JSONException;
import org.json.JSONObject;

import shushuxu.cityu.com.StudentAssessment.ubhave.dataformatter.json.PullSensorJSONFormatter;
import shushuxu.cityu.com.StudentAssessment.ubhave.datahandler.except.DataHandlerException;

public class StepCounterFormatter extends PullSensorJSONFormatter
{
	private final static String NUM_STEPS = "stepCount";
	private final static String LAST_BOOT_MILLIS = "lastBootMillis";
	private final static String LAST_BOOT = "lastBoot";
	
	public StepCounterFormatter(final Context context)
	{
		super(context, SensorUtils.SENSOR_TYPE_STEP_COUNTER);
	}

	@Override
	protected void addSensorSpecificData(JSONObject json, SensorData data) throws JSONException, DataHandlerException
	{
		StepCounterData stepData = (StepCounterData) data;
		long lastBoot = stepData.getLastBoot();
		if (lastBoot == 0)
		{
			throw new DataHandlerException(DataHandlerException.NO_DATA);
		}
		json.put(NUM_STEPS, stepData.getNumSteps());
		json.put(LAST_BOOT_MILLIS, lastBoot);
		json.put(LAST_BOOT, createTimeStamp(lastBoot));
	}

	@Override
	protected void addSensorSpecificConfig(JSONObject json, SensorConfig config) throws JSONException
	{}

	@Override
	public SensorData toSensorData(final String jsonString)
	{
		try
		{
			JSONObject jsonData = super.parseData(jsonString);
			long senseStartTimestamp = super.parseTimeStamp(jsonData);
			SensorConfig sensorConfig = super.getGenericConfig(jsonData);
			float numSteps = jsonData.getInt(NUM_STEPS);
			long lastBoot = jsonData.getLong(LAST_BOOT_MILLIS);
			
			boolean setRawData = true;
			boolean setProcessedData = false;
			
			StepCounterProcessor processor = (StepCounterProcessor) AbstractProcessor.getProcessor(applicationContext, sensorType, setRawData, setProcessedData);
			return processor.process(senseStartTimestamp, numSteps, lastBoot, sensorConfig);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
