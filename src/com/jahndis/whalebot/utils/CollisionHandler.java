package com.jahndis.whalebot.utils;

import java.util.Collection;

import android.graphics.PointF;
import android.graphics.Rect;

import com.jahndis.whalebot.gameobject.framework.Collidable;

public final class CollisionHandler {
  
  private CollisionHandler() {
    
  }
  
  public static void checkForCollisions(Collidable object, Collection<? extends Collidable> others) {
    boolean hasCollisionWithAny = false;
    Collidable other = null;
    for (Collidable o : others) {
      if (other == null) {
        other = o;
      }
      if (object.hasCollision(o)) {
        object.respondToCollision(o);
        hasCollisionWithAny = true;
      }
    }
    if (!hasCollisionWithAny) {
      object.respondToNoCollision(other);
    }
  }
  
  public static boolean hasCollision(Collidable object, Collidable other) {
    return Rect.intersects(object.getCollisionMask(), other.getCollisionMask());
  }
  
  public static PointF getCollisionResolutionVector(Collidable object, Collidable other, float direction) {
    // Get overlap (intersection) of collision masks
    Rect intersect = new Rect();
    intersect.setIntersect(object.getCollisionMask(), other.getCollisionMask());
    Rect overlap = new Rect(0, intersect.height(), intersect.width(), 0);
    
    // Get the direction of the vector to move out of collision
    float moveOutX = 0;
    float moveOutY = 0;
    float moveOutDirection = (float) (direction % (Math.PI * 2));
    
    // Variables for line equation y = px + q
    float p = (float) Math.tan(moveOutDirection);
    float q;
    float x;
    float y;
    
    if (moveOutDirection >= 0 && moveOutDirection < Math.PI * 0.5) {
      // Move out direction is up and to the right
      x = overlap.left;
      y = overlap.bottom;
      q = y - (p * x);
      
      // Find the intersect of the move out direction that lies on the overlap rectangle
      if ((p * overlap.right + q) <= overlap.top) {
        // Intersection with right side
        moveOutX = overlap.right;
        moveOutY = -(p * overlap.right + q);
      } else if ((overlap.top - q) / p <= overlap.right) {
        // Intersection with top side
        moveOutX = (overlap.top - q) / p;
        moveOutY = -overlap.top;
      }
      
    } else if (moveOutDirection >= Math.PI * 0.5 && moveOutDirection < Math.PI) {
      // Move out direction is up and to the left
      x = overlap.right;
      y = overlap.bottom;
      q = y - (p * x);
      
      // Find the intersect of the move out direction that lies on the overlap rectangle
      if ((p * overlap.left + q) <= overlap.top) {
        // Intersection with left side
        moveOutX = -(overlap.right - overlap.left);
        moveOutY = -(p * overlap.left + q);
      } else if ((overlap.top - q) / p >= overlap.left) {
        // Intersection with the top side
        moveOutX = -(overlap.right - ((overlap.top - q) / p));
        moveOutY = -overlap.top;
      }
      
    } else if (moveOutDirection >= Math.PI && moveOutDirection < Math.PI * 1.5) {
      // Move out direction is down and to the left
      x = overlap.right;
      y = overlap.top;
      q = y - (p * x);
      
      // Find the intersect of the move out direction that lies on the overlap rectangle
      if ((p * overlap.left + q) >= overlap.bottom) {
        // Intersection with left side
        moveOutX = -(overlap.right - overlap.left);
        moveOutY = overlap.top - (p * overlap.left + q);
      } else if ((overlap.bottom - q) / p >= overlap.left) {
        // Intersection with bottom side
        moveOutX = -(overlap.right - ((overlap.bottom - q) / p));
        moveOutY = overlap.top - overlap.bottom;
      }
      
    } else if (moveOutDirection >= Math.PI * 1.5 && moveOutDirection < Math.PI * 2) {
      // Move out direction is down and to the right
      x = overlap.left;
      y = overlap.top;
      q = y - (p * x);
      
      // Find the intersect of the move out direction that lies on the overlap rectangle
      if ((p * overlap.right + q) >= overlap.bottom) {
        // Intersection with right side
        moveOutX = overlap.right;
        moveOutY = overlap.top - (p * overlap.right + q);
      } else if ((overlap.bottom - q) / p <= overlap.right) {
        // Intersection with bottom side
        moveOutX = (overlap.bottom - q) / p;
        moveOutY = overlap.top - overlap.bottom;
      }
    } 
    
    return new PointF(moveOutX, moveOutY);
  }

}
