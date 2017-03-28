package com.view.developer.customview;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.view.developer.customview.customview.StatusBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        statusBar();
    }

    private void statusBar() {
        final StatusBar statusBar = (StatusBar) findViewById(R.id.statusBarVew);
        Button mBtnAgain = (Button) findViewById(R.id.btn_again);
        mBtnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        statusBar.setStep(1);
                    }
                }, 0);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        statusBar.setStep(2);
                    }
                }, 2000);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        statusBar.setStep(3);
                    }
                }, 4000);
            }
        });
        statusBar.setInfo(new String[]{"烹饪", "收汁中", "完成"});
        statusBar.setPointNumber(3);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                statusBar.setStep(2);
            }
        }, 2000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                statusBar.setStep(3);
            }
        }, 4000);
    }
}
