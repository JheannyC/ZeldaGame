package com.trigger.entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {

    protected double x, y, width, height;

    private BufferedImage sprite;

    public Entity(int x, int y, int width, int height, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
    }

    public int getX() {
        return (int) x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return (int) y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return (int) width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return (int) height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void render (Graphics g) {
        g.drawImage(sprite, this.getX(), this.getY(), null);
    }

    public void tick() {

    }
}
