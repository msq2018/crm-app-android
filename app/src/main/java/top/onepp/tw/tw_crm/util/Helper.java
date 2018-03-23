package top.onepp.tw.tw_crm.util;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import top.onepp.tw.tw_crm.R;

/**
 * Created by msq on 2018/3/9.
 */

public final class Helper {
    private static final String TAG = "Helper";

    public static String getConfigValue(Context context, String name) {
        Resources resources = context.getResources();
        try {
            InputStream rawResource = resources.openRawResource(R.raw.config);
            Properties properties = new Properties();
            properties.load(rawResource);
            return properties.getProperty(name);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Unable to find the config file: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "Failed to open config file.");
        }

        return null;
    }

    public static String Md5(String string){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(string.getBytes("utf-8"));
            return toHex(bytes);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    private static String toHex(byte[] bytes) {

        final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i=0; i<bytes.length; i++) {
            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }

    public static Map<String, Object> jsonToMap(JSONObject json){
        Map<String, Object> retMap = new HashMap<String, Object>();
        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map<String, Object> toMap(JSONObject object)  {
        Map<String, Object> map = new HashMap<String, Object>();
        try{
            Iterator<String> keysItr = object.keys();
            while(keysItr.hasNext()) {
                String key = keysItr.next();
                Object value = object.get(key);

                if(value instanceof JSONArray) {
                    value = toList((JSONArray) value);
                }
                else if(value instanceof JSONObject) {
                    value = toMap((JSONObject) value);
                }
                map.put(key, value);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static  List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        try{
            for(int i = 0; i < array.length(); i++) {
                Object value = array.get(i);
                if(value instanceof JSONArray) {
                    value = toList((JSONArray) value);
                }

                else if(value instanceof JSONObject) {
                    value = toMap((JSONObject) value);
                }
                list.add(value);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return list;
    }


}
