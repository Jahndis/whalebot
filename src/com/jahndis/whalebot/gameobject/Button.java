package com.jahndis.whalebot.gameobject;

import com.jahndis.whalebot.framework.Game;
import com.jahndis.whalebot.framework.Graphics;
import com.jahndis.whalebot.framework.Input.TouchEvent;
import com.jahndis.whalebot.gameobject.framework.Paintable;
import com.jahndis.whalebot.gameobject.framework.Touchable;


public abstract class Button extends GameObject implements Paintable, Touchable {
  
  public boolean pressed = false;
  
  public Button(Game game, int x, int y, int width, int height) {
    super(game, x, y, width, height);
  }
  
  @Override
  public void respondToTouchEvent(TouchEvent event) {
    switch (event.type) {
    case TOUCH_DOWN:
      if (inBounds(event)) {
        pressed = true;
        onPress(event);
      }
      break;
    case TOUCH_DRAGGED:
      if (inBounds(event)) {
        pressed = true;
        onDrag(event);
      } else {
        pressed = false;
      }
      break;
    case TOUCH_HOLD:
      if (inBounds(event)) {
        pressed = true;
        onHold(event);
      } else {
        pressed = false;
      }
      break;
    case TOUCH_UP:
      if (inBounds(event)) {
        pressed = false;
        onRelease(event);
      }
      break;
    default:
      throw new RuntimeException("Invalid touch event type '" + event.type + "'");
    }
  }
  
  public abstract void onPress(TouchEvent event);
  
  public abstract void onDrag(TouchEvent event);
  
  public abstract void onHold(TouchEvent event);
  
  public abstract void onRelease(TouchEvent event);
  
  public abstract void paint(Graphics g);
  
  
  /* Private Methods */
  
  private boolean inBounds(TouchEvent event) {
    if (event.x > x && event.x < x + width - 1 && event.y > y && event.y < y + height - 1) {
      return true;
    } else {
      return false;
    }
  }

}
