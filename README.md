# LogFiles
Persistent log files management.

[Download JAR](https://github.com/FMR7/LogFiles/raw/master/dist/LogFiles.jar)

[Download source](https://github.com/FMR7/LogFiles/archive/master.zip)

###import com.persistence.LogFiles;
_____________________________________________________________________

####Call the class.
LogFiles lf = new LogFiles();
_____________________________________________________________________

####Create a new log file.
#####Requires two strings, filename prefix and the log msg. 
lf.createLog("Debug", "Log example.\nDebugging info...");
######File will look like this: Debug_yyyyMMdd_HHmmss.log
_____________________________________________________________________

####Deleting old logs.
#####Requires a Date to remove older logs. And a string, filename prefix, to filter.
######Get current datetime.
Calendar cal = Calendar.getInstance();
######Set datetime to one week before.
cal.add(Calendar.DAY_OF_MONTH, -7);
######Parse calendar to date.
Date d = cal.getTime();
######Remove logs older than Date d and with the filename prefix "Debug".
lf.deleteOldLogs("Debug",d);
