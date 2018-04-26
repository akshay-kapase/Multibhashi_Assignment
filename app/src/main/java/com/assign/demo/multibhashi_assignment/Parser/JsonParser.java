package com.assign.demo.multibhashi_assignment.Parser;

import com.assign.demo.multibhashi_assignment.Activities.Slide.Slide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akshay on 22-04-2018.
 */

public class JsonParser {
    public static List<Slide> JsonParse(JSONObject jsonObject){
        List<Slide> list=new ArrayList<>();
        //Try Block
        try {
            JSONArray lessons=jsonObject.getJSONArray("lesson_data");
            for (int i = 0; i < lessons.length(); i++) {
                Slide model=new Slide();
                JSONObject obj=lessons.getJSONObject(i);
                model.setType(obj.getString("type"));
                model.setConceptName(obj.getString("conceptName"));
                model.setPronunciation(obj.getString("pronunciation"));
                model.setTargetScript(obj.getString("targetScript"));
                model.setAudioUrl(obj.getString("audio_url"));
                list.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  list;
    }
}
