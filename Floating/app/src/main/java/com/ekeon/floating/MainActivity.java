package com.ekeon.floating;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.facebook.drawee.backends.pipeline.Fresco;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

//  @Bind(R.id.btn_start) Button btnStrat;
//  @Bind(R.id.btn_start) Button btnStop;

  @SuppressWarnings("unused")
  @OnClick(R.id.btn_start)
  void onServiceStart() {
    startService(new Intent(getApplication(), FloatingService.class));
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.btn_stop)
  void onServiceStop() {
    stopService(new Intent(getApplication(), FloatingService.class));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Fresco.initialize(this);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
  }

}
