package com.agtokty.yyyemekhane;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.agtokty.Settings;
import com.agtokty.models.*;
import com.agtokty.models.YYYemekListesi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    // constructor
    public JSONParser() {

    }

    private String getJSONStringFromUrl(String urlString) {

        String jsonString = null;
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            jsonString = getStringFromInputStream(in);

        } catch (Exception exp) {

        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        return  jsonString;
    }

    public JSONObject getJSONFromUrl(String url) {

        String jsonString = null;
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    con.getInputStream(), "iso-8859-9"), 8);
            while ((jsonString = reader.readLine()) != null) {
                sb.append(jsonString + "\n");
            }
            jsonString = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;
    }

    public Settings getSettings() {

        Settings[] _settings = null;

        String jsonString = getJSONStringFromUrl(Links.globalSettings);

        Gson gson = new GsonBuilder().create();

        _settings = gson.fromJson(jsonString, Settings[].class);

        for (int i = 0; i < _settings.length; i++) {
            if (_settings[i].appName.equals(Links.appName))
                return _settings[i];
        }

        return null;
    }

    public YYYemekListesi getYemekler(String yemekUrl) {

        YYYemekListesi _yemekListesi = null;

        String jsonString = getJSONStringFromUrl(yemekUrl);

        Gson gson = new GsonBuilder().create();

        _yemekListesi = gson.fromJson(jsonString, YYYemekListesi.class);

        return _yemekListesi;
    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }
}