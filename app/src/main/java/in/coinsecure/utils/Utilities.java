package in.coinsecure.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;

/**
 * Created by user on 8/12/15.
 */
public class Utilities
{
    public static final String TABLE_DATA="tableData";

    public static void showToast(Context context,String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void savePreference(Context context,JSONArray jsonArray)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(TABLE_DATA, jsonArray.toString());
        editor.commit();
    }

    public static JSONArray getPreference(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            JSONArray jsonArray = new JSONArray(prefs.getString(TABLE_DATA, null));
            Log.d("debug",jsonArray.toString());
            return jsonArray;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
