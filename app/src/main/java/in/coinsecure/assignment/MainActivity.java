package in.coinsecure.assignment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.coinsecure.utils.Utilities;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private final String API_URL="https://api.coinsecure.in/v0/noauth/allasks";
    private TableLayout mainTable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        if(Utilities.isNetworkAvailable(this))
            hitApi();
        else
        {
            Utilities.showToast(this,"Not connected to internet.");
            JSONArray array=Utilities.getPreference(this);
            if(array!=null)
                try {
                    setData(array);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utilities.showToast(this, "Error in parsing!");

                }
        }

    }

    void init()
    {
        mainTable=(TableLayout)findViewById(R.id.main_table_layout);
    }

    void hitApi()
    {
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Fetching");
        progressDialog.setCancelable(false);

        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.GET,API_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data=null;
                    data = jsonObject.getJSONArray("result").getJSONObject(0).getJSONArray("allasks").getJSONArray(0);
                    setData(data);
                }
                catch (JSONException e)
                {
                    Utilities.showToast(MainActivity.this,"Error in response parsing!");
                }

                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("debug",error.toString());
                JSONArray array=Utilities.getPreference(MainActivity.this);
                if(array!=null)
                    try {
                        setData(array);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utilities.showToast(MainActivity.this, "Error in parsing!");

                    }
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
    }

    void setData(JSONArray data) throws JSONException{

        TableRow tempRow;
        JSONObject tempJson;
        TextView tempTextView;

        for(int i=0;i<data.length();i++)
        {
            tempRow=new TableRow(this);

            tempRow.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            tempJson = data.getJSONObject(i);

            tempTextView=new TextView(this);
            tempTextView.setText(tempJson.getString("rate"));
            tempRow.addView(tempTextView);

            tempTextView=new TextView(this);
            tempTextView.setText(tempJson.getString("sum"));
            tempRow.addView(tempTextView);

            tempTextView=new TextView(this);
            tempTextView.setText(tempJson.getString("sumFiat"));
            tempRow.addView(tempTextView);

            tempTextView=new TextView(this);
            tempTextView.setText(tempJson.getString("volume"));
            tempRow.addView(tempTextView);

            mainTable.addView(tempRow);
            final View vline1 = new View(this);
            vline1.setLayoutParams(new
                    TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));
            vline1.setBackgroundColor(Color.WHITE);
            mainTable.addView(vline1);
        }

        Utilities.savePreference(this,data);

    }
}
