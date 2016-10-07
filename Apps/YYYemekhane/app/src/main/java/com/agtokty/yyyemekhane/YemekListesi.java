package com.agtokty.yyyemekhane;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.agtokty.database.YemekDB;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class YemekListesi extends ListActivity {


    // JSON IDS:
    private static final String TAG_GUN = "gun";
    private static final String TAG_BIR = "bir";
    private static final String TAG_IKI = "iki";
    private static final String TAG_UC = "uc";
    private static final String TAG_DORT = "dort";
    private static String zaman = "";
    private static int OK = 1;

    private ProgressDialog pDialog;
    private TextView tvlisttitle;
    private ArrayList<HashMap<String, String>> mCommentList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yemeklist);
        zaman = getIntent().getExtras().getString("zaman");
        tvlisttitle = (TextView) findViewById(R.id.listtitle);
        if (zaman.equals("ogle")) {
            tvlisttitle.setText("Aylık Öğle Yemek Listesi");

        } else {
            tvlisttitle.setText("Aylık Akşam Yemek Listesi");

        }

        new LoadComments().execute();


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void updateDate() {
        mCommentList = new ArrayList<HashMap<String, String>>();
        try {

            YemekDB ydb = new YemekDB(YemekListesi.this);
            ydb.open();
            Cursor c = ydb.getList();

            if (c.getCount() > 0 && c != null) {

                while (c.moveToNext()) {

                    // gets the content of each tag
                    String gun = c.getString(0);
                    String bir = c.getString(1);
                    String iki = c.getString(2);
                    String uc = c.getString(3);
                    String dort = c.getString(4);
                    String type = c.getString(5);

                    HashMap<String, String> map = new HashMap<String, String>();

                    String[] tok = gun.split("-");
                    int d = Integer.parseInt(tok[0]);
                    int m = Integer.parseInt(tok[1]);
                    int y = 2016;
                    if (tok.length == 3)
                        y = Integer.parseInt(tok[2]);
                    if (tok.length == 2) {
                        Calendar now = Calendar.getInstance();
                        y = now.get(Calendar.YEAR);
                    }

                    //Log.i("tag", type +"ve zaman : "+ zaman);
                    if (compareDate(y, m, d) && type.equals(zaman)) {
                        map.put(TAG_GUN, gun);
                        map.put(TAG_BIR, bir);
                        map.put(TAG_IKI, iki);
                        map.put(TAG_UC, uc);
                        map.put(TAG_DORT, dort);

                        mCommentList.add(map);
                    }
                }

            } else {
                OK = 0;
            }
            ydb.close();
            c.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (mCommentList.isEmpty()) {
            tvlisttitle.setTextColor(getResources().getColor( R.color.red));
            if(zaman.equals("ogle")){
                tvlisttitle.setText("Aylık Öğle Yemek Listesi Boş");
            }
            else{
                tvlisttitle.setText("Aylık Akşam Yemek Listesi Boş");
            }
        }

    }

    private void updateList() {
        ListAdapter adapter = new SimpleAdapter(this, mCommentList,
                R.layout.singleyemek, new String[]{TAG_GUN, TAG_BIR,
                TAG_IKI, TAG_UC, TAG_DORT}, new int[]{R.id.tvgun, R.id.tvbir,
                R.id.tviki, R.id.tvuc, R.id.tvdort});


        setListAdapter(adapter);
        getListView().setClickable(false);
    }

    public class LoadComments extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(YemekListesi.this);
            pDialog.setMessage("Yemek Listesi Yükleniyor...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {
            updateDate();
            return null;

        }


        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            updateList();
            if (OK == 0)
                showalert();

        }
    }

    @SuppressWarnings("deprecation")
    public void showalert() {
        AlertDialog alertDialog = new AlertDialog.Builder(
                YemekListesi.this).create();

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
                        Intent i = new Intent(YemekListesi.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }

    public boolean compareDate(int year, int monthOfYear, int dayOfMonth) {
        @SuppressWarnings("deprecation")
        Date inputDate = new Date(year, monthOfYear, dayOfMonth);
        Long inputTime = inputDate.getTime();
        Calendar calendar = Calendar.getInstance();
        @SuppressWarnings("deprecation")
        Date validDate = new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), (calendar.get(Calendar.DAY_OF_MONTH) + 30));
        Long validTime = validDate.getTime();
        if (validTime > inputTime) {
            //Log.i("tag"," önce");
            return false;

        } else {
            //Log.i("tag"," sonra");
            return true;
        }

    }


}
