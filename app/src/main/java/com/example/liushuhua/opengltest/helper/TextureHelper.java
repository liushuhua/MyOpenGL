package com.example.liushuhua.opengltest.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.liushuhua.opengltest.utils.LogUtils;

import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGenerateMipmap;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLUtils.texImage2D;

/**
 * Created by LiuShuHua on 2017/6/28.
 * description：纹理帮助类
 */

public class TextureHelper {

    private static final String TAG = "TextureHelper";

    /**
     * 生成纹理
     *
     * @param context    上下文
     * @param resourceId 纹理资源
     * @return 纹理对象
     */
    public static int loadTexture(Context context, int resourceId) {
        final int[] textureObjectIds = new int[1];
        //generate a texture
        glGenTextures(1, textureObjectIds, 0);
        if (textureObjectIds[0] == 0) {
            LogUtils.w(TAG, "Could not generate a new OpenGL texture object");
            return 0;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        //保持图片没有缩放,是原图
        options.inScaled = false;
        //解码成bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        if (bitmap == null) {
            LogUtils.w(TAG, "Bitmap could not decode ");
            glDeleteTextures(1, textureObjectIds, 0);
            return 0;
        }
        //绑定纹理
        glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);
        //设置缩小纹理的过滤方式：三线
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        //设置放大纹理的过滤方式：双线
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        //把Bitmap放在OPenGL中
        texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
        //为了节省内存，用完bitmap就回收
        bitmap.recycle();
        //创建一个mip贴图
        glGenerateMipmap(GL_TEXTURE_2D);
        //解除绑定
        glBindTexture(GL_TEXTURE_2D, 0);
        return textureObjectIds[0];
    }
}
