package com.superglue.toweroffense.util;

public class Constants {
    public static final int DEF_RES_X = 1600;
    public static final int DEF_RES_Y = 900;

    public static final int RES_X = 1280;
    public static final int RES_Y = 720;

    public static float RES_SCALE_X = (float)RES_X / (float)DEF_RES_X;
    public static float RES_SCALE_Y = (float)RES_Y / (float)DEF_RES_Y;

    public static float RES_RATIO = (RES_X * RES_Y) / (DEF_RES_X * DEF_RES_Y);

    public static final float CAMERA_ZOOM_AMT = 1 - RES_SCALE_X;

    public static final float PIXELS_TO_METERS = 100f;

    // pi / 180
    public static final float DEGS_TO_RADS = 0.01745329251f;
}
