package com.ucas.utils;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

/**
 * Compare student's position with base position (Accurate to meter)
 *
 * If student-base position > standardMeters(300m) and return "invalid".
 * If student-base position <= standardMeters(300m) and return "valid".
 */
public class PositionComp {
//    public static void main(String[] args){
//        //GetPos(basePos, getPos);
//        // 40.409461,116.679735 教一
//        GetPos(40.409461 ,116.679735,40.410580,116.680416);
//    }

    public static String GetPos(double latitude1, double longitude1, double latitude2, double longitude2){

        double standardMeters = 300;    // The position is valid within 300 meters

        // GlobalCoordinates(double latitude, double longitude)
        GlobalCoordinates basePos = new GlobalCoordinates(latitude1, longitude1);
        GlobalCoordinates getPos = new GlobalCoordinates(latitude2, longitude2);

        GeodeticCurve geodeticCurve = new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.WGS84, basePos, getPos);

        // Distance between basePos and getPos
        double meters = geodeticCurve.getEllipsoidalDistance();

        System.out.println("WGS84 coordinate system calculation result："+ meters + "m");

        if(meters > standardMeters){
            return "Position invalid!";
        } else {
            return "sign success!";
        }

    }

}
