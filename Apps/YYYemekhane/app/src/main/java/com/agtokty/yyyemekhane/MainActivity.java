package com.agtokty.yyyemekhane;


import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.agtokty.Settings;
import com.agtokty.YemekhaneUtil;
import com.agtokty.database.YemekDB;
import com.agtokty.models.YYYemek;
import com.agtokty.models.YYYemekListesi;

public class MainActivity extends Activity implements OnClickListener {

    Button Bogle, Baksam, Bgun;
    private ProgressDialog pDialog;
    //private ArrayList<HashMap<String, String>> mCommentList;

    // An array of all of our comments
    private JSONArray mYemeksogle = null;
    private JSONArray mYemeksaksam = null;
    public static String PACKAGE_NAME;
    // JSON IDS:
    private static final String TAG_YEMEKLER = "yemekler";
    private static final String TAG_GUN = "gun";
    private static final String TAG_BIR = "bir";
    private static final String TAG_IKI = "iki";
    private static final String TAG_UC = "uc";
    private static final String TAG_DORT = "dort";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bogle = (Button) findViewById(R.id.bogle);
        Baksam = (Button) findViewById(R.id.baksam);
        Bgun = (Button) findViewById(R.id.bgun);

        Bogle.setOnClickListener(this);
        Baksam.setOnClickListener(this);
        Bgun.setOnClickListener(this);


        PACKAGE_NAME = getApplicationContext().getPackageName();
        SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstRun = wmbPreference.getBoolean("FIRSTRUN", true);
        if (isFirstRun && isInternetAvailable()) {
            // Code to run once
            SharedPreferences.Editor editor = wmbPreference.edit();
            editor.putBoolean("FIRSTRUN", false);
            editor.commit();
            new LoadYemeks().execute();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update:
                if (isInternetAvailable()) {
                    new LoadYemeks().execute();
                } else
                    Toast.makeText(MainActivity.this, "İnternet Bağlantısı Yok", Toast.LENGTH_SHORT).show();
                break;
            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);

                sendIntent.putExtra(Intent.EXTRA_TEXT, "Yüzüncü Yıl Üniversitesi Yemekhane Uygulaması" +
                        " : https://play.google.com/store/apps/details?id=" + PACKAGE_NAME);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.about:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

                // Setting Dialog Title
                alertDialog.setTitle("  Hakkında  ");

                alertDialog.setIcon(R.drawable.about);

                // Setting Dialog Message
                alertDialog.setMessage(R.string.appabout);

                // Showing Alert Message
                alertDialog.show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bogle:
                YemekDB ydb = new YemekDB(MainActivity.this);
                ydb.open();
                Cursor c = ydb.getList();
                if (c.getCount() > 0 && c != null) {
                    ydb.close();
                    c.close();
                    Intent ogle = new Intent(this, YemekListesi.class);
                    ogle.putExtra("zaman", "ogle");
                    startActivity(ogle);
                } else {
                    ydb.close();
                    c.close();
                    showalertUpdate();
                }

                break;

            case R.id.baksam:
                YemekDB ydb2 = new YemekDB(MainActivity.this);
                ydb2.open();
                Cursor c2 = ydb2.getList();
                if (c2.getCount() > 0 && c2 != null) {
                    ydb2.close();
                    c2.close();
                    Intent ogle = new Intent(this, YemekListesi.class);
                    ogle.putExtra("zaman", "aksam");
                    startActivity(ogle);
                } else {
                    ydb2.close();
                    c2.close();
                    showalertUpdate();
                }
                break;

            case R.id.bgun:
                if (isWeekend()) {
                    showalertWeekend();
                } else {
                    YemekDB ydb3 = new YemekDB(MainActivity.this);
                    ydb3.open();
                    Cursor c3 = ydb3.getList();
                    if (c3.getCount() > 0 && c3 != null) {
                        Intent i = new Intent(MainActivity.this, GununYemekleri.class);
                        ydb3.close();
                        c3.close();
                        startActivity(i);
                    } else {
                        ydb3.close();
                        c3.close();
                        showalertUpdate();
                    }
                }
                break;
            default:
                break;
        }

    }

    public boolean isInternetAvailable() {
        try {
            ConnectivityManager nInfo = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            nInfo.getActiveNetworkInfo().isConnectedOrConnecting();

            Log.i("TAG", "Net avail:"
                    + nInfo.getActiveNetworkInfo().isConnectedOrConnecting());

            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                Log.i("TAG", "Network is available");
                return true;
            } else {
                Log.i("TAG", "Network is not available");
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateJSONdata() {

        JSONParser jParser = new JSONParser();
        YemekDB ydb = new YemekDB(MainActivity.this);
        ydb.open();
        ydb.recreate();
        ydb.close();

        Links links = new Links();


        Settings _settings = null;
        try {
            _settings = jParser.getSettings();
        } catch (Exception exp) {
            //Hata alındı devam et..
        }

        if (_settings != null && _settings.urls != null && !_settings.urls.isEmpty()) {

            for (Settings.ServiceUrl url : _settings.urls) {

                try {
                    YYYemekListesi yemekListesiOglen = jParser.getYemekler(url.bir);
                    YYYemekListesi yemekListesiAksam = jParser.getYemekler(url.iki);

                    ydb.open();
                    if (yemekListesiOglen != null && yemekListesiOglen.yemekler != null && !yemekListesiOglen.yemekler.isEmpty()) {
                        ydb.add(yemekListesiOglen, "oglen");
                    } else {
                        continue;
                    }

                    if (yemekListesiAksam != null && yemekListesiAksam.yemekler != null && !yemekListesiAksam.yemekler.isEmpty()) {
                        ydb.add(yemekListesiAksam, "aksam");
                    } else {
                        continue;
                    }

                } catch (Exception exp) {
                    //hata varsa bir sonraki linklere geç
                    continue;
                }

                //oglen ve aksam listesi güncellendi sorun yok.
                return true;
            }
        } else {
            //ayar dosyasını okuyamadı ise sabit linkler üzerine okuma yap
            YYYemekListesi yemekListesiOglen = jParser.getYemekler(links.serviceLinkOglen);
            YYYemekListesi yemekListesiAksam = jParser.getYemekler(links.serviceLinkAksam);

            if (yemekListesiOglen != null && yemekListesiOglen.yemekler != null && !yemekListesiOglen.yemekler.isEmpty()) {
                ydb.add(yemekListesiOglen, "oglen");
            }

            if (yemekListesiAksam != null && yemekListesiAksam.yemekler != null && !yemekListesiAksam.yemekler.isEmpty()) {
                ydb.add(yemekListesiAksam, "aksam");
            }

            return true;
        }

        return true;
    }

    public class LoadYemeks extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Yemek Listesi Güncelleniyor... \n Lütfen Bekleyiniz.");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {
            updateJSONdata();
            return null;

        }


        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            //updateList();
        }
    }


    public boolean isWeekend() {
        Calendar ci = Calendar.getInstance();

        if (ci.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || ci.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
            return true;
        else
            return false;
    }

    @SuppressWarnings("deprecation")
    public void showalertWeekend() {
        AlertDialog alertDialog = new AlertDialog.Builder(
                MainActivity.this).create();

        // Setting Dialog Title
        alertDialog.setTitle("Uyarı !");

        // Setting Dialog Message
        alertDialog.setMessage("Bugün Hafta Sonu");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.alert);

        // Setting OK Button
        alertDialog.setButton("TAMAM",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }

    @SuppressWarnings("deprecation")
    public void showalertUpdate() {
        AlertDialog alertDialog = new AlertDialog.Builder(
                MainActivity.this).create();

        // Setting Dialog Title
        alertDialog.setTitle("Uyarı !");

        // Setting Dialog Message
        alertDialog.setMessage("Lütfen Yemek Listesini Güncelleyiniz\n\n");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.dbupdate);

        // Setting OK Button
        alertDialog.setButton("TAMAM",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }


}
