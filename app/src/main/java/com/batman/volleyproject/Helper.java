package com.batman.volleyproject;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by HP on 5/11/2017.
 */

//Helper class to get the api-key
public class Helper {
    private static final String TAG = "Helper";

    public static String getValue (Context context , String name) {
        Resources resources = context.getResources();

        try{
            InputStream rawResource = resources.openRawResource(R.raw.apikey);
            Properties properties = new Properties();
            properties.load(rawResource);
            return properties.getProperty(name);
        }catch(Resources.NotFoundException e){
            Log.e(TAG , "Cannot open file");
        }catch (IOException e) {
            Log.e(TAG , "IOException");
        }

        return null;
    }
}
