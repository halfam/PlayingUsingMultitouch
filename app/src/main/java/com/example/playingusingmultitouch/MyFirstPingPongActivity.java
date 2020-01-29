package com.example.playingusingmultitouch;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

public class MyFirstPingPongActivity extends MainMenu {
    public static MyFirstPingPongSurfaceView myFirstPingPongSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myFirstPingPongSurfaceView = new MyFirstPingPongSurfaceView(this);
        setContentView(myFirstPingPongSurfaceView);

    }

}
