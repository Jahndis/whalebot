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
import android.graphics.PointF;
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
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    // Set window to have no title bar and be full screen
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    
    // Determine dimensions and scaling of frame buffer
    Point frameBufferDimensions = getFrameBufferDimensions();
    PointF frameBufferScale = getFrameBufferScale(frameBufferDimensions);
    
    // Create the frame buffer
    Bitmap frameBuffer = Bitmap.createBitmap(frameBufferDimensions.x, frameBufferDimensions.y, Config.RGB_565);
    
    // Create all of the components for the game
    renderView = new AndroidFastRenderView(this, frameBuffer);
    graphics = new AndroidGraphics(getAssets(), frameBuffer);
    fileIO = new AndroidFileIO(this);
    audio = new AndroidAudio(this);
    input = new AndroidInput(this, renderView, frameBufferScale.x, frameBufferScale.y);
    screen = getInitScreen();
    setContentView(renderView);
    
    // Keep the screen on while the application has focus
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
  }
  
  @Override
  public void onResume() {
    super.onResume();
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    screen.resume();
    renderView.resume();
  }
  
  @Override
  public void onPause() {
    super.onPause();
    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    renderView.pause();
    screen.pause();
    
    if (isFinishing()) {
      screen.dispose();
    }
  }
  
  @Override
  public void onBackPressed() {
    getCurrentScreen().backButton();
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
  
  @Override
  public void exit() {
    finish();
  }
  
  
  /* Private Methods */
  
  private Point getFrameBufferDimensions() {
    Point frameBufferDimensions = new Point();
    
    boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    
    frameBufferDimensions.x = isPortrait ? 800: 1280;
    frameBufferDimensions.y = isPortrait ? 1200: 800;

    return frameBufferDimensions;
  }
  
  @SuppressLint("NewApi")
  @SuppressWarnings("deprecation")
  private PointF getFrameBufferScale(Point frameBufferDimensions) {
    PointF frameBufferScale = new PointF();
    
    if (VERSION.SDK_INT < 13) {
      frameBufferScale.x = (float) frameBufferDimensions.x / getWindowManager().getDefaultDisplay().getWidth();
      frameBufferScale.y = (float) frameBufferDimensions.y / getWindowManager().getDefaultDisplay().getHeight();
    } else {
      Point size = new Point();
      getWindowManager().getDefaultDisplay().getSize(size);
      frameBufferScale.x = (float) frameBufferDimensions.x / size.x;
      frameBufferScale.y = (float) frameBufferDimensions.y / size.y;
    }
    
    return frameBufferScale;
  }

}
