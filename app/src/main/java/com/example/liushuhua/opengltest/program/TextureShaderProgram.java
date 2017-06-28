package com.example.liushuhua.opengltest.program;

import android.content.Context;

import com.example.liushuhua.opengltest.R;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;

/**
 * Created by LiuShuHua on 2017/6/28.
 * description：
 */

public class TextureShaderProgram extends ShaderProgram {
    //Uniform location
    private final int uMatrixLocation;
    private final int uTextureUnitLocation;
    //Attribute
    private final int aPositionLocation;
    private final int aTextureCoordinatesLocation;

    public TextureShaderProgram(Context context) {
        super(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader);//生成Program
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);

        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);
    }

    public void setUniforms(float[] matrix, int textureId) {
        //传递矩阵给他的Uniform
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        //将所有活动的纹理单元设置为零
        glActiveTexture(GL_TEXTURE0);
        //把纹理绑定到这个纹理单元
        glBindTexture(GL_TEXTURE_2D, textureId);
        //把选中的纹理单元传递给片段着色器的u_Texture_unit
        glUniform1i(uTextureUnitLocation, 0);

    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getTextureCoordinatesAttributeLocation() {
        return aTextureCoordinatesLocation;
    }
}
