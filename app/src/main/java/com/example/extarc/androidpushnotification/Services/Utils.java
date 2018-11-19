package com.example.extarc.androidpushnotification.Services;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Lenovo on 01-02-2016.
 */
public class Utils {

    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }

    public static int getIndex(Spinner spinner, String myString){

        int index = 0;
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }
//For IndustryList, FunctionMasterList, GenderList, Qualification
    public static String getText(Context getActivity, String jsonStringConstant, int id){

        String index = "";
        try {
            SharedPreferences prefs = getActivity.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
            String jsonString = prefs.getString(jsonStringConstant, "");

            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject elem = (JSONObject) jsonArray.get(i);
                if (id == elem.getInt("id")) {
                    index=  elem.getString("value");
                    break;
                }
            }
        }catch (Exception ex)
        {
            Log.e("getText in Utils",ex.getMessage());
        }
        return index;
    }

    // Graduation, Specialization ,State,City, District
    public static String getTextTx(Context getActivity, String jsonStringConstant, int id){

        String index = "";
        try {
            SharedPreferences prefs = getActivity.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
            String jsonString = prefs.getString(jsonStringConstant, "");

            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject elem = (JSONObject) jsonArray.get(i);
                if (id == elem.getInt("id")) {
                    index=  elem.getString("tx");
                    break;
                }
            }
        }catch (Exception ex)
        {
            Log.e("getText Tx in Utils",ex.getMessage());
        }
        return index;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if(activity != null && activity.getCurrentFocus() != null){
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static boolean isEmptyORNull(String s){
        if(TextUtils.isEmpty(s)){
            return true;
        } else if(s.equalsIgnoreCase("null")){
            return true;
        }
        return false;
    }
    public static String replaceEmailWithAsterik(String str){
        if(str != null){
           // int index = str.lastIndexOf("@");
            str= str.replaceAll("(?<=.).(?=[^@]*?.@)", "*");
        }
        return str;
    }

    public static String shorten(String longUrl) {

//        Urlshortener.Builder builder = new Urlshortener.Builder(AndroidHttp.newCompatibleTransport(), AndroidJsonFactory.getDefaultInstance(), null);
//
//        Urlshortener urlshortener = builder.setApplicationName("com.extarc.tmi")
//                .build();
//        Url url = new Url();
//         url.setLongUrl(longUrl);
//        try {
//            Urlshortener.Url.Insert insert=urlshortener.url().insert(url);
//            insert.setKey("AIzaSyBYf3aS5MDHA8G-8fMOKbiOj6FLEx8gObc"); // BrowserKey
//            url = insert.execute();
//            return url.getId();
//        } catch (IOException e) {
//            Log.e("Excpetion while getting shortenUrl", e.getMessage());
//            return "";
//        }
        return"";
    }

}
