package com.example.liushuhua.opengltest;

import com.example.liushuhua.opengltest.utils.LogUtils;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glShaderSource;

/**
 * Created by LiuShuHua on 2017/6/26.
 * description：
 */

public class ShaderHelper {

    private static final String TAG = "ShaderHelper";

    /**
     * 获取顶点Shader（vertexObjectId）
     *
     * @param shaderCode 顶点的源文件
     * @return 顶点着色器
     */
    public static int compileVertexShader(String shaderCode) {
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    /**
     * 获取片Shader（shaderObjectId）
     *
     * @param shaderCode 片着色器的源文件
     * @return 片着色器
     */
    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    private static int compileShader(int type, String shaderCode) {
        final int shaderObjectId = glCreateShader(type);//创建一个shader
        if (shaderObjectId == 0) {
            LogUtils.w(TAG, "Could not create shader");
        }
        glShaderSource(shaderObjectId, shaderCode);//把shader对象和shader的源文件关联起来
        glCompileShader(shaderObjectId);//编译着色器（shader）
        final int[] compileStatus = new int[1];
        //告诉OpenGl读取shaderObjectId关联编译器的状态，存到compileStatus的第0个位置
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);//取出编译器的状态
        //打印着色器的信息
        LogUtils.w(TAG, "Results of compiling source:" + "\n" + shaderCode + "\n:" + glGetShaderInfoLog(shaderObjectId));
        if (compileStatus[0] == 0) {
            glDeleteShader(shaderObjectId);
            LogUtils.w(TAG, "Compile of shader failed");
        }
        //说明着色器编译成功,返回编译器
        return shaderObjectId;
    }
}
