package com.example.liushuhua.opengltest.helper;

import com.example.liushuhua.opengltest.utils.LogUtils;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;

/**
 * Created by LiuShuHua on 2017/6/26.
 * description：着色器帮助类
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

    /**
     * 关联到Program
     *
     * @param vertexShaderId   顶点shader
     * @param fragmentShaderId 片shader
     * @return Program对象(programObjectId)
     */
    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        int programObjectId = glCreateProgram();
        if (programObjectId == 0) {
            LogUtils.w(TAG, "Could not create Program");
        }
        //绑定顶点和面
        glAttachShader(programObjectId, vertexShaderId);
        glAttachShader(programObjectId, fragmentShaderId);
        //把着色器联合起来
        glLinkProgram(programObjectId);
        final int[] linkStatus = new int[1];
        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == 0) {
            glDeleteProgram(programObjectId);
            LogUtils.w(TAG, "Compile of program failed");
        }
        return programObjectId;
    }

    /**
     * 证实Program是否可用
     *
     * @param program program
     * @return 是否可用
     */
    public static boolean validateStatus(int program) {
        glValidateProgram(program);
        final int[] validateStatus = new int[1];
        glGetProgramiv(program, GL_VALIDATE_STATUS, validateStatus, 0);
        LogUtils.w(TAG, "Result of validating program: " + validateStatus[0]);
        return validateStatus[0] != 0;
    }

    /**
     * 获取program
     *
     * @param vertexShaderSource   顶点资源
     * @param fragmentShaderSource 片资源
     * @return program
     */
    public static int buildProgram(String vertexShaderSource, String fragmentShaderSource) {
        int program;
        int vertexShader = compileVertexShader(vertexShaderSource);
        int fragmentShader = compileFragmentShader(fragmentShaderSource);
        program = linkProgram(vertexShader, fragmentShader);
        //打印信息
        validateStatus(program);
        return program;
    }
}
