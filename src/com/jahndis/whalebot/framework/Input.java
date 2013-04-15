package com.jahndis.whalebot.framework;

import java.util.List;

public interface Input {

  public static class TouchEvent {
    
    public enum TouchEventType {
      TOUCH_DOWN, TOUCH_UP, TOUCH_DRAGGED, TOUCH_HOLD
    }
    
    public TouchEventType type;
    public int x, y;
    public int pointer;
  }
  
  public boolean isTouchDown(int pointer);
  
  public int getTouchX(int pointer);
  
  public int getTouchY(int pointer);
  
  public List<TouchEvent> getTouchEvents();
  
}
