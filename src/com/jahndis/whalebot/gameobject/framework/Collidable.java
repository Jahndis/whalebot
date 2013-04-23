package com.jahndis.whalebot.gameobject.framework;

import java.util.Collection;

import android.graphics.Rect;

public interface Collidable {
  
  public void checkForCollisions(Collection<? extends Collidable> others);
  
  public boolean hasCollision(Collidable other);
  
  public Rect getCollisionMask();
  
  public void respondToCollision(Collidable other);
  
  public void respondToNoCollision(Collidable otherClass);

}
