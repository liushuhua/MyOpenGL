package com.example.liushuhua.opengltest.helper;

/**
 * Created by LiuShuHua on 2017/6/27.
 * description：矩阵帮助类
 */

public class MatrixHelper {
    /**
     * 投影矩阵
     *
     * @param m             矩阵
     * @param yFovInDegrees 视角
     * @param aspect        宽高比
     * @param n             近处距焦点的距离
     * @param f             远处处距焦点的距离 必须f>n
     */
    public static void perspectiveM(float[] m, float yFovInDegrees, float aspect, float n, float f) {
        final float angleInRadians = (float) (yFovInDegrees * Math.PI / 180);
        final float a = (float) (1.0 / Math.tan(angleInRadians / 2.0));
        m[0] = a / aspect;
        m[1] = 0f;
        m[2] = 0f;
        m[3] = 0f;

        m[4] = 0f;
        m[5] = a;
        m[6] = 0f;
        m[7] = 0f;

        m[8] = 0f;
        m[9] = 0f;
        m[10] = -((f + n) / (f - n));
        m[11] = -1f;

        m[12] = 0f;
        m[13] = 0f;
        m[14] = -((2f * f * n) / (f - n));
        m[15] = 0f;
    }
}
