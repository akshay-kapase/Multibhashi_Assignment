package com.assign.demo.multibhashi_assignment.Activities.Slide;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.assign.demo.multibhashi_assignment.R;

import java.util.ArrayList;
import java.util.Locale;

public class SlidesActivity extends AppCompatActivity implements SlideContract.View{

    SlidePresenter presenter;
    View view_DataScreen_learn, view_DataScreen_question, view_No_Internet, view_Failed;
    TextView tvConcept_learn, tvPronunciation_learn;
    ImageView ivPlay_learn;
    Button btnNext_learn;

    TextView tvConcept_question, tvPronunciation_question;
    ImageView ivSpeak_question;
    Button btnNext_question;

    //Global variables - Just for demo
    int CurrentSlide = 0;
    boolean isAnswerCorrect=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slides);
        presenter = new SlidePresenter(this);
        view_DataScreen_learn = getLayoutInflater().inflate(R.layout.view_data_learn, null);
        view_DataScreen_question = getLayoutInflater().inflate(R.layout.view_data_question, null);
        view_No_Internet = getLayoutInflater().inflate(R.layout.view_no_internet, null);
        view_Failed = getLayoutInflater().inflate(R.layout.view_failed, null);

        //Type Learn
        tvConcept_learn = view_DataScreen_learn.findViewById(R.id.tvConcept);
        tvPronunciation_learn = view_DataScreen_learn.findViewById(R.id.tvPronunciation);
        ivPlay_learn = view_DataScreen_learn.findViewById(R.id.ivPlay);
        btnNext_learn = view_DataScreen_learn.findViewById(R.id.btnNext);

        //Type Question
        tvConcept_question = view_DataScreen_question.findViewById(R.id.tvConcept);
        tvPronunciation_question = view_DataScreen_question.findViewById(R.id.tvPronunciation);
        ivSpeak_question = view_DataScreen_question.findViewById(R.id.ivSpeak);
        btnNext_question = view_DataScreen_question.findViewById(R.id.btnNext);

        //Buttons
        ivPlay_learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.PlayAudio(CurrentSlide);
            }
        });

        ivSpeak_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                  Toast.makeText(Slides.this, "Yes", Toast.LENGTH_SHORT).show();
                startVoiceInput();
            }
        });

        btnNext_learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.BindDataWithView(CurrentSlide);
            }
        });

        btnNext_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isAnswerCorrect)
                    presenter.BindDataWithView(CurrentSlide);
                else
                    Toast.makeText(SlidesActivity.this, "First pronounce correctly to move forword", Toast.LENGTH_SHORT).show();
            }
        });

        view_Failed.findViewById(R.id.ivReload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.FetchRemoteData();
            }
        });

        view_No_Internet.findViewById(R.id.ivReload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.FetchRemoteData();
            }
        });

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    100);
        } else
            presenter.FetchRemoteData();

    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak & Wait");
        try {
            startActivityForResult(intent, 150);

        } catch (ActivityNotFoundException a) {
            Log.d("Error",a.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 150: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String pronun = presenter.GetPronunForCurrentSlide(CurrentSlide);
                    double matchPerc = GetMatchPrecentage(pronun, result.get(0));
                    if(matchPerc>=60) {
                        isAnswerCorrect = true;
                        Toast.makeText(this, "Your Pronunciation is correct with "+ matchPerc +"%", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(this, "Pronunciation does not match with "+matchPerc+"% \n Try Again...", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    public double GetMatchPrecentage(final String pronun,final String record) {
        int count = 0;
        char[] pronunA= pronun.toCharArray();
        char[] recordA= record.toCharArray();
        for (int i = 0; i < pronunA.length; i++) {
            for (int j = 0; j < recordA.length; j++ ) {
                if (pronunA[i] == recordA[j]) {
                    count++;
                }
            }
        }
        double per = 0.0;
        if (count <= pronunA.length) per = (count * 100) / recordA.length;
        else per = 100;
        return per;
    }



    @Override
    public void LoadingScreen() {
        setContentView(R.layout.view_loading);
    }

    @Override
    public void DataScreen(Slide slide) {
        if(slide.getType().equals("learn"))
            SetLearnData(slide);
        else
            SetQuestionData(slide);
        CurrentSlide+=1;
    }

    private void SetLearnData(Slide slide){
        setContentView(view_DataScreen_learn);
        tvConcept_learn.setText(slide.getConceptName());
        tvPronunciation_learn.setText(slide.getPronunciation());
    }

    private void SetQuestionData(Slide slide){
        isAnswerCorrect=false;
        setContentView(view_DataScreen_question);
        tvConcept_question.setText(slide.getConceptName());
        tvPronunciation_question.setText(slide.getPronunciation());
    }

    @Override
    public void FailedScreen() {
        setContentView(view_Failed);
    }

    @Override
    public void NoInternetScreen() {
        setContentView(view_No_Internet);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unbind();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==100){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                presenter.FetchRemoteData();
            }
            else
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
