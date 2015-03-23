package com.nothing.pojo;

import java.util.Random;

/**
 * Created by zyan.zhang on 2015/3/23.
 */
public class Position {
    public static final int ImageX = 290;
    public static final int ImageY = 150;
    public static final int MaxX = 4;
    public static final int MaxY = 2;
    public static Random random = new Random();
    int x;
    int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getCoordinateX() {
        return (int) (Position.ImageX / Position.MaxX * (x - random.nextFloat()));
    }

    public int getCoordinateY() {
        return (int) (Position.ImageY / Position.MaxY * (y - random.nextFloat()));
    }
}

