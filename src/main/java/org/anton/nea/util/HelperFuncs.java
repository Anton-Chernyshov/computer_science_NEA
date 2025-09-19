package org.anton.nea.util;

public class HelperFuncs {
    /**
     * gives the rotational matrix for theta degrees anticlockwise from the horizontal
     * @param theta IN RADIANS, MUST BE IN FUCKING RADIANS OR I SWEAR TO GOD
     * @return new instance of matrix for thetaDegrees
     */
    public static double[][] getRotationalMatrix(double theta){
        return new double[][]{{Math.cos(theta), -Math.sin(theta)}, {Math.sin(theta), Math.cos(theta)}};
    }

}
