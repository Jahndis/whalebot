package com.jahndis.whalebot.framework.implementation;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;

import com.jahndis.whalebot.framework.Graphics;
import com.jahndis.whalebot.framework.Image;


public class AndroidGraphics implements Graphics {
  
  AssetManager assets;
  Bitmap frameBuffer;
  Canvas canvas;
  Paint paint;
  Rect srcRect = new Rect();
  Rect dstRect = new Rect();
  
  public AndroidGraphics(AssetManager assets, Bitmap frameBuffer) {
    this.assets = assets;
    this.frameBuffer = frameBuffer;
    this.canvas = new Canvas(frameBuffer);
    this.paint = new Paint();
  }
  
  @Override
  public Image newImage(String filename, ImageFormat format) {
    Bitmap bitmap = createBitmap(filename, format);
    format = getImageFormatFromBitmapConfig(bitmap.getConfig());
    return new AndroidImage(bitmap, format);
  }
  
  @Override
  public void clearScreen(int color) {
    canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8, (color & 0xff));
  }
  
  @Override
  public void drawLine(int x, int y, int x2, int y2, int color) {
    paint.setColor(color);
    canvas.drawLine(x, y, x2, y2, paint);
  }
  
  @Override
  public void drawRect(int x, int y, int width, int height, int color) {
    paint.setColor(color);
    paint.setStyle(Style.FILL);
    canvas.drawRect(x,  y, x + width, y + height, paint);
  }
  
  @Override
  public void drawCircle(int cx, int cy, int radius, int color) {
    paint.setColor(color);
    paint.setStyle(Style.FILL);
    canvas.drawCircle(cx, cy, radius, paint);
  }
  
  @Override
  public void drawOval(int x, int y, int width, int height, int color) {
    paint.setColor(color);
    paint.setStyle(Style.FILL);
    canvas.drawOval(new RectF(x, y, x + width, y + height), paint);
  }
  
  @Override
  public void drawARGB(int a, int r, int g, int b) {
    paint.setStyle(Style.FILL);
    canvas.drawARGB(a, r, g, b);
  }
  
  @Override
  public void drawString(String text, int x, int y, Paint paint) {
    canvas.drawText(text, x, y, paint);
  }
  
  @Override
  public void drawImage(Image image, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight) {
    srcRect.left = srcX;
    srcRect.top = srcY;
    srcRect.right = srcX + srcWidth;
    srcRect.left = srcY + srcHeight;
    
    dstRect.left = x;
    dstRect.top = y;
    dstRect.right = x + srcWidth;
    dstRect.left = y + srcHeight;
    
    canvas.drawBitmap(((AndroidImage) image).bitmap, srcRect, dstRect, null);
  }
  
  @Override
  public void drawImage(Image image, int x, int y) {
    canvas.drawBitmap(((AndroidImage) image).bitmap, x, y, null);
  }
  
  public void drawScaledImage(Image image, int x, int y, int width, int height, int srcX, int srcY, int srcWidth, int srcHeight) {
    srcRect.left = srcX;
    srcRect.top = srcY;
    srcRect.right = srcX + srcWidth;
    srcRect.left = srcY + srcHeight;
    
    dstRect.left = x;
    dstRect.top = y;
    dstRect.right = x + width;
    dstRect.left = y + height;
    
    canvas.drawBitmap(((AndroidImage) image).bitmap, srcRect, dstRect, null);
  }
  
  @Override
  public int getWidth() {
    return frameBuffer.getWidth();
  }
  
  @Override
  public int getHeight() {
    return frameBuffer.getHeight();
  }
  
  
  /* Private Methods */
  
  private Bitmap createBitmap(String fileName, ImageFormat format) {
    Bitmap bitmap = null;
    
    Bitmap.Config config = getBitmapConfigFromImageFormat(format);
    
    Options options = new Options();
    options.inPreferredConfig = config;
    
    InputStream in = null;
    try {
      in = assets.open(fileName);
      bitmap = BitmapFactory.decodeStream(in, null, options);
      if (bitmap == null) {
        throw new RuntimeException("Couldn't load bitmap from asset '" + fileName + "'");
      }
    } catch (IOException e) {
      throw new RuntimeException("Couldn't load bitmap from asset '" + fileName + "'");
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
          
        }
      }
    }
    
    return bitmap;
  }
  
  private Bitmap.Config getBitmapConfigFromImageFormat(ImageFormat format) {
    Bitmap.Config config = null;
    
    switch (format) {
    case RGB565:
      config = Bitmap.Config.RGB_565;
      break;
    case ARGB4444:
      config = Bitmap.Config.ARGB_4444;
      break;
    case ARGB8888:
      config = Bitmap.Config.ARGB_8888;
      break;
    case ALPHA8:
      config = Bitmap.Config.ALPHA_8;
      break;
    default:
      throw new RuntimeException("Unsupported image format '" + format + "'");
    }
    
    return config;
  }
  
  private ImageFormat getImageFormatFromBitmapConfig(Config config) {
    ImageFormat format = null;
    
    switch (config) {
    case RGB_565:
      format = ImageFormat.RGB565;
      break;
    case ARGB_4444:
      format = ImageFormat.ARGB4444;
      break;
    case ARGB_8888:
      format = ImageFormat.ARGB8888;
      break;
    case ALPHA_8:
      format = ImageFormat.ALPHA8;
      break;
    default:
      throw new RuntimeException("Unsupported image format '" + config + "'");
    }
    
    return format;
  }

}
