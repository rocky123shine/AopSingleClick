package com.example.aopsingleclick;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.aopsingleclick.annotation.SingleClickBehavior;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @SingleClickBehavior(300)
    public void login(View view) {
       // SystemClock.sleep(200);
       // System.out.println("登陆。。。。。");
    }
}
