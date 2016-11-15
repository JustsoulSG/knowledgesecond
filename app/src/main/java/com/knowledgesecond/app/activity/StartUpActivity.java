package com.knowledgesecond.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.knowledgesecond.app.R;
import com.knowledgesecond.app.util.Constant;
import com.knowledgesecond.app.util.HttpUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.Header;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by zsg95 on 2016/11/14.
 */

public class StartUpActivity extends Activity{
    private ImageView startUp;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.start_up_layout);
        startUp=(ImageView) findViewById(R.id.start_up);
        initImage();
    }

    private void initImage(){
        File dir=getFilesDir();
        final  File imageFile=new File(dir,"start.jpg");
        if (imageFile.exists()){
            startUp.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
        }else{
            startUp.setImageResource(R.mipmap.start);
        }
        final ScaleAnimation scaleAni=new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAni.setFillAfter(true);
        scaleAni.setDuration(3000);
        scaleAni.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if (HttpUtils.isNetworkConnected(StartUpActivity.this)){
                    HttpUtils.get(Constant.START, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            try{
                                JSONObject jsonObject = new JSONObject(new String(bytes));
                                String url = jsonObject.getString("img");
                                HttpUtils.getImage(url, new BinaryHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                        saveImage(imageFile, bytes);
                                        startActivity();

                                    }

                                    @Override
                                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                                        startActivity();
                                    }
                                });
                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                            startActivity();
                        }
                    });
                }else{
                    Toast.makeText(StartUpActivity.this,"没有网络连接!",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        startUp.startAnimation(scaleAni);
    }

    private void startActivity(){

        Intent intent = new Intent(StartUpActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    public void saveImage(File file, byte[] bytes){
        try{
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.flush();
            fos.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
