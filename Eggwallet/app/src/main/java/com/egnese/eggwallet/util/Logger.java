package com.egnese.eggwallet.util;

import android.content.Context;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by adityaagrawal on 24/12/15.
 */
public class Logger {
    public static void appendLog(Context context, String text) {
        File folder = context.getExternalFilesDir("log");
        File logFile = new File(folder.toString() + "/log.log");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
