package com.agtokty.yyyemekhane;


import java.util.Calendar;

import com.agtokty.database.YemekDB;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class GununYemekleri extends Activity{
	
	TextView O1,O2,O3,O4,A1,A2,A3,A4,Tvtarih;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gununyemekleri);
		
		Log.i("tag", "günün yemekleri");
		
		Calendar ci = Calendar.getInstance(); 
		int day_id = ci.get(Calendar.DAY_OF_WEEK);
		String day_name="";
		switch (day_id) {
		case Calendar.MONDAY:
			day_name="Pazartesi";
		    break;
		case Calendar.TUESDAY:
			day_name="Salı";
		    break;
		case Calendar.WEDNESDAY:
			day_name="Çarşamba";
		    break;
		case Calendar.THURSDAY:
			day_name="Perşembe";
		    break;
		case Calendar.FRIDAY:
			day_name="Cuma";
		    break;
		case Calendar.SATURDAY:
			day_name="Cumartesi";
			break;
		case Calendar.SUNDAY:
			day_name="Pazar";
		    break;
		}
		
		
		
		int g = ci.get(Calendar.DAY_OF_MONTH);
		String str=g+"";
		if(g<10)
			 str = "0"+g;
		
		String CiDateTime = "" + str+  "-" + 
			    (ci.get(Calendar.MONTH) + 1) + "-" +
			    ci.get(Calendar.YEAR)+""  ;
		//Log.i("tag", CiDateTime);
		
		O1 = (TextView)findViewById(R.id.tv1);
		O2 = (TextView)findViewById(R.id.tv2);
		O3 = (TextView)findViewById(R.id.tv3);
		O4 = (TextView)findViewById(R.id.tv4);
		A1 = (TextView)findViewById(R.id.tv5);
		A2 = (TextView)findViewById(R.id.tv6);
		A3 = (TextView)findViewById(R.id.tv7);
		A4 = (TextView)findViewById(R.id.tv8);
		Tvtarih= (TextView)findViewById(R.id.tvtarih);
		Tvtarih.setText(CiDateTime+" "+day_name);
		
		YemekDB ydb = new YemekDB(GununYemekleri.this);
		ydb.open();
		Cursor c = ydb.getList();
		
		
		int af = 0 ,of =0 ;
		
		while(c.moveToNext()){
			Log.i("tag", "database : "+c.getString(0)+"tel : "+CiDateTime);
			//Log.i("tag","yemek " + c.getString(1) +"\n");
			if(CiDateTime.equals(c.getString(0)) && c.getString(5).equals("ogle") && of ==0){
				O1.setText(c.getString(1));
				O2.setText(c.getString(2));
				O3.setText(c.getString(3));
				O4.setText(c.getString(4));
				of =1;
				Log.i("tag","ogle oldu");
				
			}
			else if(CiDateTime.equals(c.getString(0)) && c.getString(5).equals("aksam") && af ==00){
				A1.setText(c.getString(1));
				A2.setText(c.getString(2));
				A3.setText(c.getString(3));
				A4.setText(c.getString(4));
				af=1;
				Log.i("tag","aksam oldu");
			}
		}
		ydb.close();
		c.close();
		
		
		
		
	}
}
