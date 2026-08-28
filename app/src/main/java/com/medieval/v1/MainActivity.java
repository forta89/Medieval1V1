package com.medieval.v1;

import android.app.Activity;
import android.os.Bundle;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.View;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class MainActivity extends Activity {

    private GLSurfaceView glView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        glView = new GLSurfaceView(this);

        glView.setEGLContextClientVersion(2);
        glView.setRenderer(new MedievalRenderer());

        glView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        setContentView(glView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (glView != null) {
            glView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (glView != null) {
            glView.onResume();
        }
    }

    private static class MedievalRenderer implements GLSurfaceView.Renderer {

        private final float[] cubeVertices = {
                // Frente
                -1.0f, -1.0f,  1.0f,
                 1.0f, -1.0f,  1.0f,
                 1.0f,  1.0f,  1.0f,
                -1.0f,  1.0f,  1.0f,

                // Trás
                -1.0f, -1.0f, -1.0f,
                -1.0f,  1.0f, -1.0f,
                 1.0f,  1.0f, -1.0f,
                 1.0f, -1.0f, -1.0f,

                // Esquerda
                -1.0f, -1.0f, -1.0f,
                -1.0f, -1.0f,  1.0f,
                -1.0f,  1.0f,  1.0f,
                -1.0f,  1.0f, -1.0f,

                // Direita
                 1.0f, -1.0f, -1.0f,
                 1.0f,  1.0f, -1.0f,
                 1.0f,  1.0f,  1.0f,
                 1.0f, -1.0f,  1.0f,

                // Cima
                -1.0f,  1.0f, -1.0f,
                -1.0f,  1.0f,  1.0f,
                 1.0f,  1.0f,  1.0f,
                 1.0f,  1.0f, -1.0f,

                // Baixo
                -1.0f, -1.0f, -1.0f,
                 1.0f, -1.0f, -1.0f,
                 1.0f, -1.0f,  1.0f,
                -1.0f, -1.0f,  1.0f
        };

        private final FloatBuffer vertexBuffer;

        private float rotationX = 20.0f;
        private float rotationY = 30.0f;

        private float previousX;
        private float previousY;

        private int program;

        private final String vertexShaderCode =
                "attribute vec4 vPosition;" +
                "uniform mat4 uMVPMatrix;" +
                "void main() {" +
                "  gl_Position = uMVPMatrix * vPosition;" +
                "}";

        private final String fragmentShaderCode =
                "precision mediump float;" +
                "uniform vec4 vColor;" +
                "void main() {" +
                "  gl_FragColor = vColor;" +
                "}";

        private final float[] mvpMatrix = new float[16];
        private final float[] projectionMatrix = new float[16];
        private final float[] viewMatrix = new float[16];
        private final float[] modelMatrix = new float[16];
        private final float[] tempMatrix = new float[16];

        private int positionHandle;
        private int colorHandle;
        private int matrixHandle;

        MedievalRenderer() {

            ByteBuffer bb = ByteBuffer.allocateDirect(
                    cubeVertices.length * 4
            );

            bb.order(ByteOrder.nativeOrder());

            vertexBuffer = bb.asFloatBuffer();

            vertexBuffer.put(cubeVertices);
            vertexBuffer.position(0);
        }

        @Override
        public void onSurfaceCreated(
                javax.microedition.khronos.egl.EGLConfig config) {

            GLES20.glClearColor(
                    0.05f,
                    0.04f,
                    0.03f,
                    1.0f
            );

            GLES20.glEnable(GLES20.GL_DEPTH_TEST);

            int vertexShader = loadShader(
                    GLES20.GL_VERTEX_SHADER,
                    vertexShaderCode
            );

            int fragmentShader = loadShader(
                    GLES20.GL_FRAGMENT_SHADER,
                    fragmentShaderCode
            );

            program = GLES20.glCreateProgram();

            GLES20.glAttachShader(program, vertexShader);
            GLES20.glAttachShader(program, fragmentShader);

            GLES20.glLinkProgram(program);

            positionHandle =
                    GLES20.glGetAttribLocation(
                            program,
                            "vPosition"
                    );

            colorHandle =
                    GLES20.glGetUniformLocation(
                            program,
                            "vColor"
                    );

            matrixHandle =
                    GLES20.glGetUniformLocation(
                            program,
                            "uMVPMatrix"
                    );
        }

        @Override
        public void onSurfaceChanged(
                javax.microedition.khronos.opengles.GL10 gl,
                int width,
                int height) {

            GLES20.glViewport(
                    0,
                    0,
                    width,
                    height
            );

            float ratio =
                    (float) width / (float) height;

            android.opengl.Matrix.frustumM(
                    projectionMatrix,
                    0,
                    -ratio,
                    ratio,
                    -1,
                    1,
                    3,
                    20
            );
        }

        @Override
        public void onDrawFrame(
                javax.microedition.khronos.opengles.GL10 gl) {

            GLES20.glClear(
                    GLES20.GL_COLOR_BUFFER_BIT |
                    GLES20.GL_DEPTH_BUFFER_BIT
            );

            android.opengl.Matrix.setLookAtM(
                    viewMatrix,
                    0,

                    0,
                    0,
                    7,

                    0,
                    0,
                    0,

                    0,
                    1,
                    0
            );

            android.opengl.Matrix.setIdentityM(
                    modelMatrix,
                    0
            );

            android.opengl.Matrix.rotateM(
                    modelMatrix,
                    0,
                    rotationX,
                    1,
                    0,
                    0
            );

            android.opengl.Matrix.rotateM(
                    modelMatrix,
                    0,
                    rotationY,
                    0,
                    1,
                    0
            );

            android.opengl.Matrix.multiplyMM(
                    tempMatrix,
                    0,
                    viewMatrix,
                    0,
                    modelMatrix,
                    0
            );

            android.opengl.Matrix.multiplyMM(
                    mvpMatrix,
                    0,
                    projectionMatrix,
                    0,
                    tempMatrix,
                    0
            );

            GLES20.glUseProgram(program);

            GLES20.glEnableVertexAttribArray(
                    positionHandle
            );

            GLES20.glVertexAttribPointer(
                    positionHandle,
                    3,
                    GLES20.GL_FLOAT,
                    false,
                    12,
                    vertexBuffer
            );

            GLES20.glUniformMatrix4fv(
                    matrixHandle,
                    1,
                    false,
                    mvpMatrix,
                    0
            );

            // Cor do bloco medieval
            GLES20.glUniform4f(
                    colorHandle,
                    0.45f,
                    0.25f,
                    0.10f,
                    1.0f
            );

            for (int i = 0; i < 6; i++) {

                GLES20.glDrawArrays(
                        GLES20.GL_TRIANGLE_FAN,
                        i * 4,
                        4
                );
            }

            GLES20.glDisableVertexAttribArray(
                    positionHandle
            );
        }

        private int loadShader(
                int type,
                String shaderCode) {

            int shader =
                    GLES20.glCreateShader(type);

            GLES20.glShaderSource(
                    shader,
                    shaderCode
            );

            GLES20.glCompileShader(shader);

            return shader;
        }
    }
}
