package com.example.hirokazu.rssreader;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by Hirokazu on 2016/08/13.
 */
public class RegisterService extends BaseService {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        RssSyncTask rssSyncTask = new RssSyncTask();
        rssSyncTask.execute(Utils.RSS_URL);

        return START_STICKY;
    }
}

class RssSyncTask extends AsyncTask<String,String,Void>{

    @Override
    protected Void doInBackground(String... strings) {
        String result = null;
        //リクエストオブジェクトを作成
        Request request = new Request.Builder().url(strings[0]).get().build();

        //クライアントオブジェクトを作成
        OkHttpClient client = new OkHttpClient();

        //リクエストして結果を受け取る
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setInput(new StringReader(result));

            int eventType;
            eventType = xmlPullParser.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){
                if(eventType == XmlPullParser.START_DOCUMENT) {
                    Log.d("XmlPullParserSample", "Start document");
                } else if(eventType == XmlPullParser.END_DOCUMENT) {
                    Log.d("XmlPullParserSample", "End document");
                } else if(eventType == XmlPullParser.START_TAG) {
                    Log.d("XmlPullParserSample", "Start tag "+xmlPullParser.getName());
                } else if(eventType == XmlPullParser.END_TAG) {
                    Log.d("XmlPullParserSample", "End tag "+xmlPullParser.getName());
                } else if(eventType == XmlPullParser.TEXT) {
                    Log.d("XmlPullParserSample", "Text "+xmlPullParser.getText());
                }
                eventType = xmlPullParser.next();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }
}
