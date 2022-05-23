package com.haolin.liholinhotfix;

import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class MainActivity extends AppCompatActivity {
    TextView titleTv;
    Button showTitleBt;
    Button hotfixBt;
    Button removeHotfixBt;
    Button killSelfBt;

    File apk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleTv = findViewById(R.id.titleTv);
        showTitleBt = findViewById(R.id.showTitleBt);
        hotfixBt = findViewById(R.id.hotfixBt);
        removeHotfixBt = findViewById(R.id.removeHotfixBt);
        killSelfBt = findViewById(R.id.killSelfBt);

        apk = new File(getCacheDir() + "/hotfix.dex");

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                switch (v.getId()) {
                    case R.id.showTitleBt:
                        Title title = new Title();
                        titleTv.setText(title.getTitle());
                        break;
                    case R.id.hotfixBt:
                        Log.e("lyb======",getCacheDir().getAbsolutePath());
                        if (!apk.exists()) {
                            try (Source source = Okio.source(getAssets().open("hotfix.dex"));
                                 BufferedSink sink = Okio.buffer(Okio.sink(apk))) {
                                sink.writeAll(source);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
//                        OkHttpClient client = new OkHttpClient();
//                        final Request request = new Request.Builder()
//                                .url("https://api.hencoder.com/patch/upload/hotfix.dex")
//                                .build();
//                        client.newCall(request)
//                                .enqueue(new Callback() {
//                                    @Override
//                                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                                        v.post(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                Toast.makeText(MainActivity.this, "出错了", Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                                    }
//
//                                    @Override
//                                    public void onResponse(@NotNull Call call, @NotNull Response response) {
//                                        try (BufferedSink sink = Okio.buffer(Okio.sink(apk))) {
//                                            sink.write(response.body().bytes());
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
//                                        v.post(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                Toast.makeText(MainActivity.this, "补丁加载成功", Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                                    }
//                                });
                        break;
                    case R.id.removeHotfixBt:
                        if (apk.exists()) {
                            apk.delete();
                        }
                        break;
                    case R.id.killSelfBt:
                        android.os.Process.killProcess(android.os.Process.myPid());
                        break;
                }
            }
        };

        showTitleBt.setOnClickListener(onClickListener);
        hotfixBt.setOnClickListener(onClickListener);
        removeHotfixBt.setOnClickListener(onClickListener);
        killSelfBt.setOnClickListener(onClickListener);
    }
}