package com.jahndis.whalebot.collisions;

import java.util.ArrayList;

import android.graphics.PointF;
import android.graphics.Rect;

import com.jahndis.whalebot.gameobject.framework.Collidable;

public abstract class CollisionManager<T extends Collidable, S extends Collidable> {
  
  private T object;
  private ArrayList<S> others;
  
  public CollisionManager(T object, ArrayList<S> others) {
    this.object = object;
    this.others = others;
  }
  
  public boolean check() {
    for (S other : others) {
      if (other != null) {
        if (Rect.intersects(object.getCollisionMask(), other.getCollisionMask())) {
          onCollision(object, other);
          return true;
        }
      }
    }
    
    onNoCollision(object);
    return false;
  }
  
  public abstract void onCollision(T object, S other);
  
  public abstract void onNoCollision(T object);
  
  public T getObject() {
    return object;
  }
  
  public ArrayList<S> getOthers() {
    return others;
  }
  
  public static PointF getCollisionResolutionVector(Collidable object1, Collidable object2, float direction) {
    // Get overlap (intersection) of collision masks
    Rect intersect = new Rect();
    intersect.setIntersect(object1.getCollisionMask(), object2.getCollisionMask());
    if (intersect.isEmpty()) {
      return new PointF(0, 0);
    }
    
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
