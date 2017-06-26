package com.example.liushuhua.opengltest.utils;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by LiuShuHua on 2017/6/26.
 * description：
 */

public class TextResourceReader {

    public static String readTextFileFromResource(Context context, int resourceId) {
        StringBuilder body = new StringBuilder();
        InputStream inputStream = context.getResources().openRawResource(resourceId);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String nextLine;
        try {
            while ((nextLine = reader.readLine()) != null) {
                body.append(nextLine);
                body.append('\n');
            }
        } catch (IOException e) {
            throw new RuntimeException("Could Not Open Resource"+resourceId,e);
        }catch (Resources.NotFoundException e){
            throw new RuntimeException("Resource Not Fount"+resourceId,e);
        }
        return body.toString();
    }
}
