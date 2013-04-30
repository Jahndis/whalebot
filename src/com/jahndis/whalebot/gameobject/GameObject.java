package com.jahndis.whalebot.gameobject;

import com.jahndis.whalebot.framework.Game;

import android.graphics.Point;

public abstract class GameObject {
  
  protected Game game;
  public int x;
  public int y;
  public int width;
  public int height;
  
  public GameObject(Game game, int x, int y, int width, int height) {
    this.game = game;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }
  
  public void setPosition(int x, int y) {
    this.x = x;
    this.y = y;
  }
  
  public void setPosition(Point position) {
    this.x = position.x;
    this.y = position.y;
  }
  
  public void move(float x, float y) {
    this.x += x;
    this.y += y;
  }
  
  public Point getPosition() {
    return new Point(this.x, this.y);
  }
  
  public void setSize(int width, int height) {
    this.width = width;
    this.height = height;
  }

}
