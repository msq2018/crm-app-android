package top.onepp.tw.tw_crm.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by msq on 2018/2/1.
 */

public class Json {
    private static final Json ourInstance = new Json();

    public static Json init() {
        return ourInstance;
    }

    private Json() {}

    private JSONObject jsonObject;

    public JSONObject parse(String string){
        this.createJSONObject(string);
        return  this.jsonObject;
    }

    /**
     *
     * @param jsonObject
     * @param name
     * @return
     */
    public Object get(JSONObject jsonObject , String name){
        Object result = null;
        try {
           result = jsonObject.get(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


    public boolean getBoolean(JSONObject jsonObject, String name) {
        Boolean result = false;
        try {
           result = jsonObject.getBoolean(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  result;
    }

    public String getString(JSONObject jsonObject,String name){
        String result = null;
        try {
            result = jsonObject.getString(name);
        } catch (JSONException e) {
            Log.e("TEST TAG",e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public int getInt(JSONObject jsonObject,String name){
        try {
            return jsonObject.getInt(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public JSONObject getJSONObject(JSONObject jsonObject, String name){
        try {
            return jsonObject.getJSONObject(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray getJsonArray(JSONObject jsonObject , String name){
        try {
          return jsonObject.getJSONArray(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }




    private void createJSONObject(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            this.jsonObject = jsonObject;
        } catch (JSONException e) {
            Log.e("JSONObject",e.getMessage());
            e.printStackTrace();
        }
    }
}
