package com.example.liushuhua.opengltest.mode;

import com.example.liushuhua.opengltest.ObjectBuilder;
import com.example.liushuhua.opengltest.program.ColorShaderProgram;
import com.example.liushuhua.opengltest.utils.Geometry;

import java.util.List;

/**
 * Created by LiuShuHua on 2017/6/29.
 * description：
 */

public class Puck {

    private static final int POSITION_COMPONENT_COUNT = 3;
    public final float radius, height;
    private final VertexArray vertexArray;
    private final List<ObjectBuilder.DrawCommand> drawList;

    public Puck(float radius, float height, int numPointsAroundPuck) {
        this.radius = radius;
        this.height = height;
        ObjectBuilder.GenerateData generatedData = ObjectBuilder.createPuck(new Geometry.Cylinder(new Geometry.Point(0f, 0f, 0f), radius, height), numPointsAroundPuck);
        vertexArray = new VertexArray(generatedData.vertexData);
        drawList = generatedData.drawList;

    }

    public void bindData(ColorShaderProgram colorShaderProgram) {
        vertexArray.setVertexAttributePointer(0, colorShaderProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, 0);
    }

    public void draw() {
        for (ObjectBuilder.DrawCommand drawCommand : drawList) {
            drawCommand.draw();
        }
    }
}
