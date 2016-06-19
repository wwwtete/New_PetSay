package com.petsay.utile;

import android.util.Log;

import com.petsay.constants.Constants;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/5/23
 * @Description Log日志
 */
public final class PetsayLog {

    private static final String LOG_FORMAT = "%1$s\n%2$s";
    private static volatile boolean writeDebugLogs = Constants.isDebug;
//    private static volatile boolean writeLogs = true;

    public static void writeDebugLogs(boolean writeDebugLogs) {
        PetsayLog.writeDebugLogs = writeDebugLogs;
    }

    public static void d(String message, Object... args) {
        if (writeDebugLogs) {
            log(Log.DEBUG, null, message, args);
        }
    }

    public static void i(String message, Object... args) {
        log(Log.INFO, null, message, args);
    }

    public static void w(String message, Object... args) {
        log(Log.WARN, null, message, args);
    }

    public static void e(Throwable ex) {
        log(Log.ERROR, ex, null);
    }

    public static void e(String message, Object... args) {
        log(Log.ERROR, null, message, args);
    }

    public static void e(Throwable ex, String message, Object... args) {
        log(Log.ERROR, ex, message, args);
    }

    private static void log(int priority, Throwable ex, String message, Object... args) {
        if (!Constants.isDebug) return;
        if (args !=null && args.length > 0) {
            message = String.format(message, args);
        }
        String log;
        if (ex == null) {
            log = message;
        } else {
            String logMessage = message == null ? ex.getMessage() : message;
            String logBody = Log.getStackTraceString(ex);
            log = String.format(LOG_FORMAT, logMessage, logBody);
        }
        Log.println(priority, Constants.TAG, log);
    }

}
