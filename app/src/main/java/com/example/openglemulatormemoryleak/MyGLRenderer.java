package com.example.openglemulatormemoryleak;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private final byte[] data = new byte[1024*1024];
    private final ByteBuffer dataBuffer = ByteBuffer.wrap(data);

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Each frame create buffer and upload 1M data to GPU memory
        int[] buffers = new int[1];
        GLES20.glGenBuffers( 1, buffers, 0 );
        int id = buffers[0];
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, id);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, data.length, dataBuffer, GLES20.GL_STATIC_DRAW);

        // Don't delete buffer.
        // Once context is destroyed all memory occupied width these buffers should be released.
        // Works as expected on physical devices but this memory never released on Emulator.
        // GLES20.glDeleteBuffers(1, buffers, 0);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }
}
