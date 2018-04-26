package com.assign.demo.multibhashi_assignment.Activities.Slide;

/**
 * Created by Mj on 22-04-2018.
 */

public interface SlideContract {

    interface View{
        void LoadingScreen();
        void DataScreen(Slide model);
        void FailedScreen();
        void NoInternetScreen();
    }

    interface Presenter{
        void bind();
        void unbind();
        void FetchRemoteData();
    }
}
