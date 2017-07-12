package com.example.liushuhua.opengltest.mode;

import com.example.liushuhua.opengltest.Constants;
import com.example.liushuhua.opengltest.ObjectBuilder;
import com.example.liushuhua.opengltest.program.ColorShaderProgram;
import com.example.liushuhua.opengltest.utils.Geometry;

import java.util.List;

/**
 * Created by LiuShuHua on 2017/6/28.
 * description：木槌
 */

public class Mallet {

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    public final float radius, height;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;
    private static final float[] VERTEX_DATA = {
            //Order of coordinates:x y r g b

            0f, -0.4f, 0f, 0f, 1f,
            0f, 0.4f, 1f, 0f, 0f,
    };
    private VertexArray vertexArray;
    private final List<ObjectBuilder.DrawCommand> drawList;

    public Mallet(float radius, float height, int numPointsAroundPuck) {
        this.radius = radius;
        this.height = height;
        ObjectBuilder.GenerateData generateData = ObjectBuilder.createMallet(new Geometry.Point(0f, 0f, 0f), radius, height, numPointsAroundPuck);
        vertexArray = new VertexArray(generateData.vertexData);
        drawList = generateData.drawList;
    }

    public void bindData(ColorShaderProgram colorShaderProgram) {
        vertexArray.setVertexAttributePointer(0,
                colorShaderProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE
        );
        vertexArray.setVertexAttributePointer(POSITION_COMPONENT_COUNT,
                colorShaderProgram.getColorAttributeLocation(),
                COLOR_COMPONENT_COUNT,
                STRIDE
        );
    }

    public void draw() {
        for (ObjectBuilder.DrawCommand command : drawList) {
            command.draw();
        }
    }
}
