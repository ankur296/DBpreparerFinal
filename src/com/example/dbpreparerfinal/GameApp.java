package com.example.dbpreparerfinal;

import java.util.ArrayList;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;

public class GameApp extends Application {

	private static GameApp appInstance;
	private ArrayList<ArrayList<String>> movieNames;
	private ProgressDialog progressDialog;
	private static Context context;
	
	public static GameApp getAppInstance(){
		return appInstance;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		GameApp.context = getApplicationContext();
		appInstance = this;
	}
	
	
}
