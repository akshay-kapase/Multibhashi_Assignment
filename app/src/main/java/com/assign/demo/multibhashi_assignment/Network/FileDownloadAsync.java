package com.assign.demo.multibhashi_assignment.Network;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.widget.ProgressBar;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by mujahid on 17/3/18.
 */

public class FileDownloadAsync extends AsyncTask<Void,Void,Boolean> {

    Context ctx;
    String Filepath;
    ConstraintLayout LoaderPanel;
    ProgressBar progressBar;

    public FileDownloadAsync(String fileUrl) {
        Filepath = fileUrl;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            File dir=new File(Environment.getExternalStorageDirectory(),"Multibhashi");
            if(!dir.exists())
                dir.mkdirs();
            URL u = new URL(Filepath);
            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();

            DataInputStream stream = new DataInputStream(u.openStream());

            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();

            DataOutputStream fos = new DataOutputStream(new FileOutputStream(dir+File.separator+ Filepath.substring(Filepath.lastIndexOf('/') + 1)));
            fos.write(buffer);
            fos.flush();
            fos.close();

        } catch(FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {

    }
}
