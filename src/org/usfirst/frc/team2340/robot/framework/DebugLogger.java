package org.usfirst.frc.team2340.robot.framework;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

class LogFormat extends Formatter {
	private final SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss.SSS a");
	@Override
	public String format(LogRecord record) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(format.format(new Date(record.getMillis())));
		buffer.append(",");
		buffer.append(record.getSourceClassName());
		buffer.append(",");
		buffer.append(record.getSourceMethodName());
		buffer.append(",");
		buffer.append(record.getMessage());
		buffer.append("\n");
		return buffer.toString();
	}
	
	public String getHead(Handler h) {
        return "Date,Object,Event,Message\n";
	}
}

public class DebugLogger {
	private Logger log = null;
	private FileHandler fh = null;
	private final LogFormat logFormat = new LogFormat();
	private final SimpleDateFormat format = new SimpleDateFormat("MMddyyyy_kkmmssSSS");
	
	synchronized public void open(String _filePath, String _fileName, String _extension) {
		//TODO: Only open if it isn't already opened.
		try {
			log = Logger.getLogger(_fileName);
			log.setUseParentHandlers(false); //Disables console output
			fh = new FileHandler(_filePath+_fileName+format.format(Calendar.getInstance().getTime())+_extension);
			fh.setFormatter(logFormat);
			log.addHandler(fh);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	synchronized public void log(String _object, String _event, String _data)
	{
		if (log != null) {
			LogRecord lr = new LogRecord(Level.INFO, _data);
			// Note: Re-purposed the sourceClassName and SourceMethodName.
			lr.setSourceClassName(_object);
			lr.setSourceMethodName(_event);
			log.log(lr);
		}
		else {
			System.out.println(_object+" "+_event+": "+_data);
		}
	}
	
	synchronized public void log(String _object, String _event)
	{
		log(_object, _event, "");
	}
	
	synchronized public void log(String _object, String _event, Double _data, int _precision)
	{
		String data = BigDecimal.valueOf(_data).setScale(_precision, RoundingMode.HALF_UP).toString();
		log(_object, _event, data);
	}
	
	synchronized public void log(String _object, String _event, Float _data, int _precision)
	{
		log(_object, _event, _data.doubleValue(), _precision);
	}
	
	synchronized public void log(String _object, String _event, Number _data)
	{
		log(_object, _event, _data.toString());
	}
	synchronized public void logBreak()
	{
		log("------", "------", "------");
	}
	
	synchronized public void close() {
		System.out.println("Closing Log");
		fh.flush();
		fh.close();
	}
}