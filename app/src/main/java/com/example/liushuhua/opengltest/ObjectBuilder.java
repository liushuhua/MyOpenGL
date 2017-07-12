package com.example.liushuhua.opengltest;

import com.example.liushuhua.opengltest.utils.Geometry;

import java.util.ArrayList;
import java.util.List;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glDrawArrays;

/**
 * Created by LiuShuHua on 2017/6/29.
 * description：
 */

public class ObjectBuilder {
    private static final int FLOATS_PER_VERTEX = 3;
    private final float[] vertexData;
    private final List<DrawCommand> drawList = new ArrayList<>();
    private int offset;

    private ObjectBuilder(int sizeInVertices) {
        vertexData = new float[FLOATS_PER_VERTEX * sizeInVertices];
    }

    private static int sizeOfCircleInVertices(int pointCount) {
        return 1 + (pointCount + 1);
    }

    private static int sizeOfOpenCylinderInVertices(int pointCount) {
        return 2 * (pointCount + 1);
    }

    /**
     * 生成冰球
     *
     * @param puck        三角形带
     * @param pointCounts 点的个数
     * @return 冰球
     */
    public static GenerateData createPuck(Geometry.Cylinder puck, int pointCounts) {
        int size = sizeOfCircleInVertices(pointCounts) + sizeOfOpenCylinderInVertices(pointCounts);
        ObjectBuilder builder = new ObjectBuilder(size);
        //冰球上面的圆，三角扇
        Geometry.Circle puckTop = new Geometry.Circle(puck.center.translateY(puck.height / 2f), puck.radius);
        builder.appendCircle(puckTop, pointCounts);
        builder.appendOpenCylinder(puck, pointCounts);
        return builder.builder();
    }

    /**
     * 返回生成的数据
     *
     * @return 数据
     */
    private GenerateData builder() {
        return new GenerateData(vertexData, drawList);
    }

    /**
     * 生成三角带
     *
     * @param puck
     * @param pointCounts
     */
    private void appendOpenCylinder(Geometry.Cylinder puck, int pointCounts) {
        final int startVertex = offset / FLOATS_PER_VERTEX;
        final int numVertex = sizeOfOpenCylinderInVertices(pointCounts);
        final float yStart = puck.center.y - (puck.height / 2f);
        final float yEnd = puck.center.y + (puck.height / 2f);
        for (int i = 0; i <= pointCounts; i++) {
            float angleInRadius = ((float) i / (float) pointCounts) * ((float) Math.PI * 2f);
            float xPosition = puck.center.x + puck.radius * (float) Math.cos(angleInRadius);
            float zPosition = puck.center.z + puck.radius * (float) Math.sin(angleInRadius);
            vertexData[offset++] = xPosition;
            vertexData[offset++] = yStart;
            vertexData[offset++] = zPosition;
            vertexData[offset++] = xPosition;
            vertexData[offset++] = yEnd;
            vertexData[offset++] = zPosition;
            drawList.add(new DrawCommand() {
                @Override
                public void draw() {
                    glDrawArrays(GL_TRIANGLE_STRIP, startVertex, numVertex);
                }
            });
        }
    }

    /**
     * 生成圆，三角扇
     *
     * @param circle
     * @param pointCounts
     */
    private void appendCircle(Geometry.Circle circle, int pointCounts) {
        final int startVertex = offset / FLOATS_PER_VERTEX;
        final int numVertex = sizeOfCircleInVertices(pointCounts);
        //center point of fan
        vertexData[offset++] = circle.center.x;
        vertexData[offset++] = circle.center.y;
        vertexData[offset++] = circle.center.z;
        //Fan around center point is used because we want to generate the point at the starting
        //<=angle twice to complete the fan
        for (int i = 0; i <= pointCounts; i++) {
            //和圆心的夹角对应的弧长
            float angleInRadius = ((float) i / (float) pointCounts) * ((float) Math.PI * 2f);
            vertexData[offset++] = circle.center.x + circle.radius * (float) (Math.cos(angleInRadius));
            vertexData[offset++] = circle.center.y;
            vertexData[offset++] = circle.center.z + circle.radius * (float) (Math.sin(angleInRadius));
            drawList.add(new DrawCommand() {
                @Override
                public void draw() {
                    glDrawArrays(GL_TRIANGLE_FAN, startVertex, numVertex);
                }
            });
        }
    }

    public static GenerateData createMallet(Geometry.Point center, float radius, float height, int numPoints) {
        int size = sizeOfCircleInVertices(numPoints) * 2 + sizeOfOpenCylinderInVertices(numPoints) * 2;
        ObjectBuilder builder = new ObjectBuilder(size);
        float baseHeight = height * 0.25f;
        Geometry.Circle baseCircle = new Geometry.Circle(center.translateY(-baseHeight), radius);
        Geometry.Cylinder baseCylinder = new Geometry.Cylinder(baseCircle.center.translateY(-baseHeight / 2f), radius, baseHeight);
        builder.appendCircle(baseCircle, numPoints);
        builder.appendOpenCylinder(baseCylinder, numPoints);
        float handlerHeight = height * 0.75f;
        float handlerRadius = height / 3f;
        Geometry.Circle handlerCircle = new Geometry.Circle(center.translateY(height * 0.5f), handlerRadius);
        Geometry.Cylinder handlerCylinder = new Geometry.Cylinder(handlerCircle.center.translateY(-handlerHeight / 2f), handlerRadius, handlerHeight);
        builder.appendCircle(handlerCircle, numPoints);
        builder.appendOpenCylinder(handlerCylinder, numPoints);
        return builder.builder();
    }

    public interface DrawCommand {
        void draw();
    }

    public class GenerateData {
        public final float[] vertexData;
        public final List<ObjectBuilder.DrawCommand> drawList;

        public GenerateData(float[] vertexData, List<ObjectBuilder.DrawCommand> drawList) {
            this.vertexData = vertexData;
            this.drawList = drawList;
        }
    }
}
