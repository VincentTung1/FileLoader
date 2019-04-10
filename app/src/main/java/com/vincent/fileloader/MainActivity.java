package com.vincent.fileloader;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import com.vincent.loadfilelibrary.LoadFileManager;
import com.vincent.loadfilelibrary.engine.x5.callback.BooleanCallback;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String[] list = new String[1];
        list[0]="android.permission.WRITE_EXTERNAL_STORAGE";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            MainActivity.this.requestPermissions(list, 105);
        }
        LoadFileManager.get().init(this);

        String path  = Environment.getExternalStorageDirectory() + File.separator  + "test.pdf";
        final File f = new File(path);
        LoadFileManager.get().isFileCanRead(f, new BooleanCallback() {
            @Override
            public void onSuccess(boolean isOK) {

                if (isOK) LoadFileManager.get().loadFile(f);
            }

            @Override
            public void onError(Throwable e) {

            }
        });

    }
}
