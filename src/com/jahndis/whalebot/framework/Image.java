package com.jahndis.whalebot.framework;

import com.jahndis.whalebot.framework.Graphics.ImageFormat;

public interface Image {
  
  public int getWidth();
  
  public int getHeight();
  
  public ImageFormat getFormat();
  
  public void dispose();

}
