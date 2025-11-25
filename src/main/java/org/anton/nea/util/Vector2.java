package org.anton.nea.util;

public class Vector2 {
    public Vector2(double x, double y){
        this.x = x;
        this.y = y;
    }
    public double x;
    public double y;

    public Vector2 add(Vector2 other){
        return new Vector2(this.x + other.x, this.y + other.y);
    }
    public Vector2 subtract(Vector2 other){
        return new Vector2(this.x - other.x, this.y - other.y);
    }
    public Vector2 multiply(double scalar){
        return new Vector2(this.x * scalar, this.y * scalar);
    }
    public Vector2 divide(double scalar){
        return new Vector2(this.x / scalar, this.y / scalar);
    }
    public Vector2 normalize(){ return new Vector2(this.x / magnitude(), this.y / magnitude());}

    public double magnitude(){
        return Math.sqrt(x * x + y * y);
    }
    public boolean isNotZero() { return this.x != 0 || this.y != 0; }
    public boolean isZero() { return this.x == 0 && this.y == 0; }
    public boolean isEqual(Vector2 other){return this.x == other.x && this.y == other.y; }


    public static final Vector2 UP = new Vector2(0, -1);
    public static final Vector2 DOWN = new Vector2(0, 1);
    public static final Vector2 LEFT = new Vector2(-1, 0);
    public static final Vector2 RIGHT = new Vector2(1, 0);

}
