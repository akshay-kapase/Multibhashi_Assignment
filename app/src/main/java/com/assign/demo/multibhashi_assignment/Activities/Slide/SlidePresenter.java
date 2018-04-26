package com.assign.demo.multibhashi_assignment.Activities.Slide;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Toast;


import com.assign.demo.multibhashi_assignment.Interface.AsyncResponse;
import com.assign.demo.multibhashi_assignment.Network.FetchAsync;
import com.assign.demo.multibhashi_assignment.Network.FileDownloadAsync;
import com.assign.demo.multibhashi_assignment.Network.NetworkUtils;
import com.assign.demo.multibhashi_assignment.Parser.JsonParser;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Akshay on 22-04-2018.
 */

public class SlidePresenter implements SlideContract.Presenter,AsyncResponse {

    Context ctx;
    List<Slide> list;
    SlideContract.View view;
    FetchAsync async;

    public SlidePresenter(SlideContract.View view) {
        this.view=view;
        ctx = (Context)view;
    }

    @Override
    public void bind() {
        //Initial bind
        ctx = (Context)view;
    }

    @Override
    public void unbind() {
        //UnBind Data
    }

    @Override
    public void FetchRemoteData() {
        //Fetch Data From Server
        if(!NetworkUtils.GetNetworkStatus(ctx)){
            view.NoInternetScreen();
            return;
        }
/*        if(async!=null &&  async.getStatus()!= AsyncTask.Status.RUNNING){*/
            async=new FetchAsync(view);
            async.ctx=ctx;
            async.response=this;
            async.execute();
       // }
    }

    @Override
    public void JsonResponse(JSONObject jsonObject) {
        //Response From Remote
        if(jsonObject!=null){
                list= JsonParser.JsonParse(jsonObject);
                if(list.size()==0)
                    view.FailedScreen();
                else {
                    //Download the First audio File
                    FileDownloadAsync downloadAsync=new FileDownloadAsync(list.get(0).getAudioUrl());
                    downloadAsync.execute();
                    //Initial Position of Json data
                    BindDataWithView(0);
                }
        }
    }

    public void BindDataWithView(int CurrentPosition){

        if(CurrentPosition<list.size() && CurrentPosition!=list.size()-1){
            FileDownloadAsync downloadAsync=new FileDownloadAsync(list.get(CurrentPosition+1).getAudioUrl());
            downloadAsync.execute();
        }

        if(CurrentPosition<list.size()){
            Slide model=list.get(CurrentPosition);
            view.DataScreen(model);
        }
        else
            Toast.makeText(ctx, "Slides Completed", Toast.LENGTH_SHORT).show();
    }

    public void PlayAudio(int CurrentSlide){
        Slide model = list.get(CurrentSlide);
        String AudioName = URLUtil.guessFileName(model.getAudioUrl(), null, null);
        PlayAudioByName(AudioName);
        Log.e("Audio Name",AudioName);
    }
    


    public void PlayAudioByName(String fileName){
        //set up MediaPlayer
        try {
            MediaPlayer mp = new MediaPlayer();
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.setDataSource(ctx, Uri.parse(Environment.getExternalStorageDirectory().getPath()+ "/Multibhashi/"+fileName));
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String GetPronunForCurrentSlide(int position) {
        Slide model = list.get(position-1);
        return model.getPronunciation();
    }

}
