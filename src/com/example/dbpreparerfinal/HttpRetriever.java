package com.example.dbpreparerfinal;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpRetriever {

    private DefaultHttpClient httpClient = new DefaultHttpClient();

    public String retrieve(String url){
       
    	HttpGet getRequest = new HttpGet(url);
        
    	try{
    		System.out.println("Search START");
            HttpResponse getResponse = httpClient.execute(getRequest);
            System.out.println("Search END");
            final int statusCode = getResponse.getStatusLine().getStatusCode();

            if(statusCode != HttpStatus.SC_OK)
                Log.w(getClass().getSimpleName(),"Error " + statusCode + " for Url " + url );

            HttpEntity responseEntity = getResponse.getEntity();

            if(responseEntity != null){
                return EntityUtils.toString(responseEntity);
            }
        }
        catch (Exception e){
        	//TODO: handle progress dialog  
            getRequest.abort();
            e.printStackTrace();
            Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
        }
        return null;
    }
}
