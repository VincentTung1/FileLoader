package com.vincent.fileloader;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.vincent.loadfilelibrary.LoadFileManager;
import com.vincent.loadfilelibrary.engine.x5.callback.BooleanCallback;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView mLv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLv = findViewById(R.id.mLv);

        String[] list = new String[1];
        list[0]="android.permission.WRITE_EXTERNAL_STORAGE";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            MainActivity.this.requestPermissions(list, 105);
        }
        LoadFileManager.get().init(this);



        String rootPath  = Environment.getExternalStorageDirectory().getAbsolutePath();
        File dir = new File(rootPath);
        File[] files = dir.listFiles();
        ArrayList<String> fs =new ArrayList<>();
        for (File file : files) {
            if (file.isFile()) fs.add(file.getAbsolutePath());
        }

        mLv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, (String[])fs.toArray(new String[fs.size()])));
        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String path = fs.get(position);
                File f = new File(path);
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
        });
    }


}
