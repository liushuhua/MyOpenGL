package com.example.liushuhua.opengltest.program;

import android.content.Context;

import com.example.liushuhua.opengltest.helper.ShaderHelper;
import com.example.liushuhua.opengltest.utils.TextResourceReader;

import static android.opengl.GLES20.glUseProgram;

/**
 * Created by LiuShuHua on 2017/6/28.
 * description：
 */

public class ShaderProgram {
    //Uniform constants
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";

    //Attribute constants
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    //Shader program
    protected int program;

    public ShaderProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId) {
        program = ShaderHelper.buildProgram(
                TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId),
                TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId));
    }

    public void userProgram() {
        glUseProgram(program);
    }
}
