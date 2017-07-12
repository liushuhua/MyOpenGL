package com.example.liushuhua.opengltest.utils;

/**
 * Created by LiuShuHua on 2017/6/29.
 * description：几何数据处理
 */

public class Geometry {
    /**
     * 点
     */
    public static class Point {

        public final float x, y, z;

        public Point(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        /**
         * 平移
         *
         * @param distance 位移
         * @return 位移后的点
         */
        public Point translateY(float distance) {
            return new Point(x, y + distance, z);
        }
    }

    /**
     * 圆
     */
    public static class Circle {

        public final Point center;
        public final float radius;

        public Circle(Point center, float radius) {
            this.center = center;
            this.radius = radius;

        }

        /**
         * 缩放
         *
         * @param scale 缩放比例
         * @return 缩放后的圆
         */
        public Circle scale(float scale) {
            return new Circle(center, radius * scale);
        }
    }

    /**
     * 圆柱体
     */
    public static class Cylinder {
        public Point center;
        public float radius, height;

        public Cylinder(Point center, float radius, float height) {
            this.center = center;
            this.radius = radius;
            this.height = height;
        }

    }
}
