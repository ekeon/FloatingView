package com.ekeon.floating;

import android.animation.ObjectAnimator;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Ekeon on 2016. 1. 26..
 */
public class FloatingService extends Service {

  private WindowManager windowManager;
  private View vChatHead;
  private View vClose;
  private WindowManager.LayoutParams params;
  private WindowManager.LayoutParams closeParams;

  private ImageView btnClose;
  private ImageView chathead;

  private int screenHeight;
  private int screenWidth;

  @Override
  public void onCreate() {
    super.onCreate();

    screenWidth = getResources().getDisplayMetrics().widthPixels;
    screenHeight = getResources().getDisplayMetrics().heightPixels;

    windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

    LayoutInflater inflater = LayoutInflater.from(this);

    vChatHead = inflater.inflate(R.layout.chathead_main, null);
    vClose = inflater.inflate(R.layout.chathead_close, null);
    vChatHead.setOnClickListener(null);
    vChatHead.setOnTouchListener(null);

    setParams();

    chathead = (ImageView)vChatHead.findViewById(R.id.chathead);
    btnClose = (ImageView)vClose.findViewById(R.id.btn_close);

    btnClose.setTranslationY(btnClose.getLayoutParams().height);
    btnClose.setOnClickListener(closeButtonListener);
    chathead.setOnTouchListener(floatingTouchListener);
  }

  private void showAndHideCloseButton(View view, boolean isTouch) {
    ViewPropertyAnimator viewPropertyAnimator = view.animate();
    viewPropertyAnimator.cancel();
    viewPropertyAnimator.translationY(isTouch ? - (screenHeight * (0.10f)) : btnClose.getHeight() + 50);
    viewPropertyAnimator.setDuration(200);
    viewPropertyAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    viewPropertyAnimator.start();
  }

  private void setParams() {
    params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT);

    params.gravity = Gravity.TOP | Gravity.LEFT;
    params.x = 0;
    params.y = getResources().getDimensionPixelSize(R.dimen.px30);

    windowManager.addView(vChatHead, params);

    closeParams = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            PixelFormat.TRANSLUCENT);
    windowManager.addView(vClose, closeParams);
  }

  private View.OnTouchListener floatingTouchListener = new View.OnTouchListener() {
    int currentX;
    int currentY;
    float touchX;
    float touchY;
    int positionX;
    int positionY;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
      switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:

          chathead.setTranslationX(positionX);
          chathead.setTranslationY(positionY);

          currentX = (int)chathead.getX();
          currentY = (int) chathead.getY();
          touchX = event.getRawX();
          touchY = event.getRawY();

          params.height = WindowManager.LayoutParams.MATCH_PARENT;
          params.width = WindowManager.LayoutParams.MATCH_PARENT;
          windowManager.updateViewLayout(vChatHead, params);

          showAndHideCloseButton(btnClose, true);
          return true;
        case MotionEvent.ACTION_UP:
          //TODO chathead 눌르고 땐후

          params.height = WindowManager.LayoutParams.WRAP_CONTENT;
          params.width = WindowManager.LayoutParams.WRAP_CONTENT;
          params.x = (positionX);
          params.y = (positionY);
          windowManager.updateViewLayout(vChatHead, params);

          chathead.setTranslationX(0);
          chathead.setTranslationY(0);

          showAndHideCloseButton(btnClose, false);
          return true;
        case MotionEvent.ACTION_MOVE:
          positionX = currentX + (int) (event.getRawX() - touchX);
          positionY = currentY + (int) (event.getRawY() - touchY);
          chathead.setTranslationX(positionX);
          chathead.setTranslationY(positionY);

          Log.d("TAG", "1 : " + btnClose.getTop());
          Log.d("TAG", "2 : " + btnClose.getRight());
          Log.d("TAG", "3 : " + btnClose.getBottom());
          Log.d("TAG", "4 : " + btnClose.getLeft());
          Log.d("TAG", "5 : " + (currentX + (int)(event.getRawX() - touchX)));
          Log.d("TAG", "6 : " + (currentY + (int)(event.getRawY() - touchY)));
          Log.d("TAG", "7 : " + chathead.getTop());
          Log.d("TAG", "8 : " + chathead.getRight());
          Log.d("TAG", "9 : " + chathead.getBottom());
          Log.d("TAG", "10 : " + chathead.getLeft());
          return true;
      }
      return false;
    }
  };

  private View.OnClickListener closeButtonListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      stopService(new Intent(getApplication(), FloatingService.class));
    }
  };

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (vChatHead != null) {
      windowManager.removeViewImmediate(vChatHead);
    }

    if (vClose != null) {
      windowManager.removeViewImmediate(vClose);
    }
  }



  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

}
