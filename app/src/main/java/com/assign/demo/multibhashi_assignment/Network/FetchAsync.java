package com.assign.demo.multibhashi_assignment.Network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.assign.demo.multibhashi_assignment.Activities.Slide.SlideContract;
import com.assign.demo.multibhashi_assignment.Interface.AsyncResponse;

import org.json.JSONObject;

/**
 * Created by Mj on 22-04-2018.
 */

public class FetchAsync extends AsyncTask<Void,Void,JSONObject> {


    public Context ctx;
    public AsyncResponse response;
    SlideContract.View view;
    String Url="http://www.akshaycrt2k.com/getLessonData.php";
    String Request="{ \\\"id\\\": \\\"eb5f5da4-25ba-2b47-c71a-d4e2b3272cc4\\\", \\\"name\\\": \\\"assignment_android\\\", \\\"description\\\": \\\"\\\", \\\"order\\\": [ \\\"69ef8b9e-6d9f-efe4-7d33-e1fd75d46f5f\\\" ], \\\"folders\\\": [], \\\"folders_order\\\": [], \\\"timestamp\\\": 1504521492956, \\\"owner\\\": 0, \\\"public\\\": false, \\\"requests\\\": [ { \\\"id\\\": \\\"69ef8b9e-6d9f-efe4-7d33-e1fd75d46f5f\\\", \\\"headers\\\": \\\"\\\", \\\"headerData\\\": [], \\\"url\\\": \\\"http:\\/\\/www.akshaycrt2k.com\\/getLessonData.php\\\", \\\"queryParams\\\": [], \\\"pathVariables\\\": {}, \\\"pathVariableData\\\": [], \\\"preRequestScript\\\": null, \\\"method\\\": \\\"GET\\\", \\\"collectionId\\\": \\\"eb5f5da4-25ba-2b47-c71a-d4e2b3272cc4\\\", \\\"data\\\": null, \\\"dataMode\\\": \\\"params\\\", \\\"name\\\": \\\"Get Lesson Data\\\", \\\"description\\\": \\\"\\\", \\\"descriptionFormat\\\": \\\"html\\\", \\\"time\\\": 1504524010366, \\\"version\\\": 2, \\\"responses\\\": [], \\\"tests\\\": null, \\\"currentHelper\\\": \\\"normal\\\", \\\"helperAttributes\\\": {} } ] }";

    public FetchAsync(SlideContract.View view) {
        this.view=view;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        view.LoadingScreen();
    }

    @Override
    protected JSONObject doInBackground(Void... voids) {
        JSONObject request = new JSONObject();
        JSONObject responseObject = null;
        try {
            Log.e("<----REQUEST---->",request.toString());
            String response = new HttpClientWrapper().doPostRequest(Url,Request);
            Log.e("Response From Net",response);
            responseObject = new JSONObject(response);
            return  responseObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseObject;
    }


    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        response.JsonResponse(jsonObject);
        Log.d("Hello",jsonObject.toString());
    }
}
