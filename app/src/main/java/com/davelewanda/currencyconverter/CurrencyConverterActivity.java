package com.davelewanda.currencyconverter;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class CurrencyConverterActivity extends AppCompatActivity {

    private EditText usDollarsEditText;
    private ListView currencyListView;
    private final int COUNTRY_COUNT = 4;
    private CurrencyAdapter currencyAdapter;
    private static RequestQueue requestQueue = null;
    private class Currency {
        String country;
        Double currencyAmount;
        public Currency(String country, Double currencyAmount) {
            this.country = country;
            this.currencyAmount = currencyAmount;
        }
    }

    private ArrayList<Currency> currencyArrayList = new ArrayList<>();

    public static class ViewHolder {
        public TextView countryTextView;
        public TextView currencyTextView;
    }

    public void setCurrencies(final int usd) {
        final Double gbp = 0.0, eur = 0.0, jpy = 0.0, brl = 0.0;
        if(usd != 0) {
            String url = "http://api.fixer.io/latest?base=USD";

            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, url, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(this.getClass().toString(), "JSON: " + response.toString());
                            try {
                                JSONObject rates = (JSONObject) response.get("rates");
                                (currencyArrayList.get(0)).currencyAmount = usd * rates.getDouble("GBP");
                                (currencyArrayList.get(1)).currencyAmount = usd * rates.getDouble("EUR");;
                                (currencyArrayList.get(2)).currencyAmount = usd * rates.getDouble("JPY");
                                (currencyArrayList.get(3)).currencyAmount = usd * rates.getDouble("BRL");
                                currencyAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(this.getClass().toString(), "VolleyError: " + error.toString());

                        }
                    });
                requestQueue.add(jsObjRequest);

        } else {
            currencyArrayList.add(new Currency(getString(R.string.GBP), gbp));
            currencyArrayList.add(new Currency(getString(R.string.EUR), eur));
            currencyArrayList.add(new Currency(getString(R.string.JPY), jpy));
            currencyArrayList.add(new Currency(getString(R.string.BRL), brl));
        }
    }

    private class CurrencyAdapter extends BaseAdapter {

        private final Activity activity;
        private final LayoutInflater inflater;

        public CurrencyAdapter(Activity a) {
            this.activity = a;
            this.inflater = (LayoutInflater)activity.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return COUNTRY_COUNT;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewHolder holder;

            if(convertView==null){

                /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
                view = inflater.inflate(R.layout.layout_currency_row, null);

                /****** View Holder Object to contain tabitem.xml file elements ******/

                holder = new ViewHolder();
                holder.countryTextView = (TextView) view.findViewById(R.id.countryTextView);
                holder.currencyTextView=(TextView)view.findViewById(R.id.currencyTextView);

                /************  Set holder with LayoutInflater ************/
                view.setTag(holder);
            }
            else {
                holder = (ViewHolder) view.getTag();
            }

            Currency tempValues = currencyArrayList.get(position);
            holder.countryTextView.setText(tempValues.country);
            holder.currencyTextView.setText(String.format("%.3f", tempValues.currencyAmount));

            return view;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_converter);

        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }

        usDollarsEditText = (EditText) findViewById(R.id.usDollarsEditText);

        usDollarsEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Toast.makeText(CurrencyConverterActivity.this, "update currencies...", Toast.LENGTH_SHORT).show();
                setCurrencies(Integer.parseInt(usDollarsEditText.getText().toString()));
                return true;
            }
        });


        currencyAdapter = new CurrencyAdapter(this);
        currencyListView = (ListView) findViewById(R.id.currencyListView);
        currencyListView.setAdapter(currencyAdapter);

        setCurrencies(0); //initialize to zero

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_currency_converter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
