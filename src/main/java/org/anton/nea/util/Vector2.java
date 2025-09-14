package org.anton.nea.util;

public class Vector2 {
    public Vector2(int x, int y){
        this.x = x;
        this.y = y;
    }
    public int x;
    public int y;

    public Vector2 add(Vector2 other){
        return new Vector2(this.x + other.x, this.y + other.y);
    }
    public Vector2 subtract(Vector2 other){
        return new Vector2(this.x - other.x, this.y - other.y);
    }
    public Vector2 multiply(int scalar){
        return new Vector2(this.x * scalar, this.y * scalar);
    }
    public Vector2 divide(int scalar){
        return new Vector2(this.x / scalar, this.y / scalar);
    }
    public int magnitude(){
        return (int)Math.sqrt(x * x + y * y);
    }

}
