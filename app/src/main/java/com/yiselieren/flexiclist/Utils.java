package com.yiselieren.flexiclist;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {
    public static void Debug(String msg)
    {
        //Log.d(MainActivity.GlobalContext.getResources().getString(R.string.app_tag), msg);
    }

    public static void importInternal(Context cntx, int res, File dir, String oname)
    {
        BufferedReader br = null;
        boolean abort = false;

        // Input
        try {
            InputStream raw = cntx.getResources().openRawResource(res);
            br = new BufferedReader(new InputStreamReader(raw, "UTF8"));
        } catch (IOException e) {
            abort = true;
        }

        // Output
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(new File(dir, oname)));
        } catch (IOException e) {
            abort = true;
        }

        // Copy
        if (!abort) {
            try {
                String readLine;
                while ((readLine = br.readLine()) != null) {
                    readLine += "\n";
                    bw.write(readLine, 0, readLine.length());
                }
            } catch (IOException e) {
            }
        }

        try {
            if (br != null)
                br.close();
            if (bw != null)
                bw.close();
        } catch (IOException e) {
        }
    }

    public static String getTitle(File dir, String fname, String name)
    {
        String rc = name;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File(dir, fname)));
        } catch (FileNotFoundException e) {
            return rc;
        }

        final String prefix = "*****";
        final String suffix = "*****";
        String readLine;
        String current = null;
        try {
            while ((readLine = br.readLine()) != null) {
                // Line processing
                if (readLine.trim().isEmpty())
                    continue;
                String line = readLine.trim();
                if (line.startsWith(prefix) && line.endsWith(suffix)) {
                    rc = line.substring(prefix.length(), line.length() - suffix.length());
                    break;
                }
            }
        } catch (IOException e) {
            return rc;
        }
        try {
            br.close();
        } catch (IOException e) {
            return rc;
        }

        return rc.trim();
    }
}
