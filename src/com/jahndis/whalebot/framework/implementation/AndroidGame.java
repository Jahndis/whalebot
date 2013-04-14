package com.jahndis.whalebot.framework.implementation;

import com.jahndis.whalebot.framework.Audio;
import com.jahndis.whalebot.framework.FileIO;
import com.jahndis.whalebot.framework.Game;
import com.jahndis.whalebot.framework.Graphics;
import com.jahndis.whalebot.framework.Input;
import com.jahndis.whalebot.framework.Screen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Point;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public abstract class AndroidGame extends Activity implements Game {
  
  AndroidFastRenderView renderView;
  Graphics graphics;
  Audio audio;
  Input input;
  FileIO fileIO;
  Screen screen;
//  WakeLock wakeLock;
  
  @SuppressWarnings("deprecation")
  @SuppressLint("NewApi")
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    
    boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    int frameBufferWidth = isPortrait ? 800: 1280;
    int frameBufferHeight = isPortrait ? 1200: 800;
    Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth, frameBufferHeight, Config.RGB_565);
    
    float scaleX;
    float scaleY;
    if (VERSION.SDK_INT < 13) {
      scaleX = (float) frameBufferWidth / getWindowManager().getDefaultDisplay().getWidth();
      scaleY = (float) frameBufferWidth / getWindowManager().getDefaultDisplay().getHeight();
    } else {
      Point size = new Point();
      getWindowManager().getDefaultDisplay().getSize(size);
      scaleX = (float) frameBufferWidth / size.x;
      scaleY = (float) frameBufferWidth / size.y;
    }
    
    renderView = new AndroidFastRenderView(this, frameBuffer);
    graphics = new AndroidGraphics(getAssets(), frameBuffer);
    fileIO = new AndroidFileIO(this);
    audio = new AndroidAudio(this);
    input = new AndroidInput(this, renderView, scaleX, scaleY);
    screen = getInitScreen();
    setContentView(renderView);
    
//    PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
//    wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "MyGame");
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
  }
  
  @Override
  public void onResume() {
    super.onResume();
//    wakeLock.acquire();
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    screen.resume();
    renderView.resume();
  }
  
  @Override
  public void onPause() {
    super.onPause();
//    wakeLock.release();
    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    renderView.pause();
    screen.pause();
    
    if (isFinishing()) {
      screen.dispose();
    }
  }
  
  @Override
  public Input getInput() {
    return input;
  }
  
  @Override
  public FileIO getFileIO() {
    return fileIO;
  }
  
  @Override
  public Graphics getGraphics() {
    return graphics;
  }
  
  @Override
  public Audio getAudio() {
    return audio;
  }
  
  @Override
  public void setScreen(Screen screen) {
    if (screen == null) {
      throw new IllegalArgumentException("Screen must not be null");
    }
    
    this.screen.pause();
    this.screen.dispose();
    screen.resume();
    screen.update(0);
    this.screen = screen;
  }
  
  @Override
  public Screen getCurrentScreen() {
    return screen;
  }

}
