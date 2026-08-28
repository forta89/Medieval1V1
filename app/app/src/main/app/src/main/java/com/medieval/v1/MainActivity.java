package com.medieval.v1;

import android.app.Activity;
import android.os.Bundle;
import android.opengl.GLSurfaceView;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.view.MotionEvent;

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

        glView.setOnTouchListener((v, event) -> {
            return true;
        });

        setContentView(glView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        glView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glView.onResume();
    }

    public static class MedievalRenderer implements GLSurfaceView.Renderer {

        private float[] projection = new float[16];
        private float[] view = new float[16];
        private float[] model = new float[16];
        private float[] mvp = new float[16];

        private Cube cube;

        @Override
        public void onSurfaceCreated(
                javax.microedition.khronos.egl.EGLConfig config) {

            GLES20.glClearColor(
                    0.08f,
                    0.10f,
                    0.08f,
                    1.0f
            );

            GLES20.glEnable(GLES20.GL_DEPTH_TEST);

            cube = new Cube();
        }

        @Override
        public void onSurfaceChanged(
                javax.microedition.khronos.opengles.GL10 gl,
                int width,
                int height) {

            GLES20.glViewport(0, 0, width, height);

            float ratio = (float) width / height;

            Matrix.frustumM(
                    projection,
                    0,
                    -ratio,
                    ratio,
                    -1,
                    1,
                    2,
                    100
            );
        }

        @Override
        public void onDrawFrame(
                javax.microedition.khronos.opengles.GL10 gl) {

            GLES20.glClear(
                    GLES20.GL_COLOR_BUFFER_BIT |
                    GLES20.GL_DEPTH_BUFFER_BIT
            );

            Matrix.setLookAtM(
                    view,
                    0,
                    0,
                    5,
                    8,
                    0,
                    0,
                    0,
                    0,
                    1,
                    0
            );

            // TERRENO
            drawCube(
                    0,
                    -2,
                    0,
                    8,
                    0.5f,
                    8,
                    0.25f,
                    0.55f,
                    0.25f
            );

            // PERSONAGEM
            drawCube(
                    0,
                    0,
                    0,
                    1,
                    2,
                    0.7f,
                    0.45f,
                    0.35f,
                    0.20f
            );

            // CABEÇA
            drawCube(
                    0,
                    1.5f,
                    0,
                    0.7f,
                    0.7f,
                    0.7f,
                    0.75f,
                    0.55f,
                    0.35f
            );

            // ÁRVORE 1
            drawCube(
                    -3,
                    -0.5f,
                    -2,
                    0.5f,
                    3,
                    0.5f,
                    0.35f,
                    0.20f,
                    0.08f
            );

            drawCube(
                    -3,
                    1.5f,
                    -2,
                    2,
                    2,
                    2,
                    0.05f,
                    0.35f,
                    0.05f
            );

            // ÁRVORE 2
            drawCube(
                    3,
                    -0.5f,
                    -3,
                    0.5f,
                    3,
                    0.5f,
                    0.35f,
                    0.20f,
                    0.08f
            );

            drawCube(
                    3,
                    1.5f,
                    -3,
                    2,
                    2,
                    2,
                    0.05f,
                    0.35f,
                    0.05f
            );

            // PEDRA
            drawCube(
                    2,
                    -1,
                    1,
                    1.2f,
                    1,
                    1.2f,
                    0.30f,
                    0.30f,
                    0.30f
            );
        }

        private void drawCube(
                float x,
                float y,
                float z,
                float sx,
                float sy,
                float sz,
                float r,
                float g,
                float b) {

            Matrix.setIdentityM(model, 0);

            Matrix.translateM(
                    model,
                    0,
                    x,
                    y,
                    z
            );

            Matrix.scaleM(
                    model,
                    0,
                    sx,
                    sy,
                    sz
            );

            float[] temp = new float[16];

            Matrix.multiplyMM(
                    temp,
                    0,
                    view,
                    0,
                    model,
                    0
            );

            Matrix.multiplyMM(
                    mvp,
                    0,
                    projection,
                    0,
                    temp,
                    0
            );

            cube.draw(mvp, r, g, b);
        }
    }

    public static class Cube {

        private final FloatBuffer vertexBuffer;

        private final float[] vertices = {

                // Frente
                -1, -1,  1,
                 1, -1,  1,
                 1,  1,  1,

                -1, -1,  1,
                 1,  1,  1,
                -1,  1,  1,

                // Trás
                -1, -1, -1,
                -1,  1, -1,
                 1,  1, -1,

                -1, -1, -1,
                 1,  1, -1,
                 1, -1, -1,

                // Esquerda
                -1, -1, -1,
                -1, -1,  1,
                -1,  1,  1,

                -1, -1, -1,
                -1,  1,  1,
                -1,  1, -1,

                // Direita
                 1, -1, -1,
                 1,  1, -1,
                 1,  1,  1,

                 1, -1, -1,
                 1,  1,  1,
                 1, -1,  1,

                // Cima
                -1, 1, -1,
                -1, 1,  1,
                 1, 1,  1,

                -1, 1, -1,
                 1, 1,  1,
                 1, 1, -1,

                // Baixo
                -1, -1, -1,
                 1, -1, -1,
                 1, -1,  1,

                -1, -1, -1,
                 1, -1,  1,
                -1, -1,  1
        };

        private final int program;

        private final String vertexShader =
                "attribute vec4 vPosition;" +
                "uniform mat4 uMVP;" +
                "void main() {" +
                "gl_Position = uMVP * vPosition;" +
                "}";

        private final String fragmentShader =
                "precision mediump float;" +
                "uniform vec4 uColor;" +
                "void main() {" +
                "gl_FragColor = uColor;" +
                "}";

        public Cube() {

            ByteBuffer bb =
                    ByteBuffer.allocateDirect(
                            vertices.length * 4
                    );

            bb.order(
                    ByteOrder.nativeOrder()
            );

            vertexBuffer = bb.asFloatBuffer();

            vertexBuffer.put(vertices);
            vertexBuffer.position(0);

            int vs = loadShader(
                    GLES20.GL_VERTEX_SHADER,
                    vertexShader
            );

            int fs = loadShader(
                    GLES20.GL_FRAGMENT_SHADER,
                    fragmentShader
            );

            program = GLES20.glCreateProgram();

            GLES20.glAttachShader(
                    program,
                    vs
            );

            GLES20.glAttachShader(
                    program,
                    fs
            );

            GLES20.glLinkProgram(program);
        }

        public void draw(
                float[] mvp,
                float r,
                float g,
                float b) {

            GLES20.glUseProgram(program);

            int position =
                    GLES20.glGetAttribLocation(
                            program,
                            "vPosition"
                    );

            int matrix =
                    GLES20.glGetUniformLocation(
                            program,
                            "uMVP"
                    );

            int color =
                    GLES20.glGetUniformLocation(
                            program,
                            "uColor"
                    );

            GLES20.glEnableVertexAttribArray(
                    position
            );

            GLES20.glVertexAttribPointer(
                    position,
                    3,
                    GLES20.GL_FLOAT,
                    false,
                    3 * 4,
                    vertexBuffer
            );

            GLES20.glUniformMatrix4fv(
                    matrix,
                    1,
                    false,
                    mvp,
                    0
            );

            GLES20.glUniform4f(
                    color,
                    r,
                    g,
                    b,
                    1.0f
            );

            GLES20.glDrawArrays(
                    GLES20.GL_TRIANGLES,
                    0,
                    vertices.length / 3
            );

            GLES20.glDisableVertexAttribArray(
                    position
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
