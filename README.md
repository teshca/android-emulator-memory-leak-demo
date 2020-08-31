# android-emulator-memory-leak-demo
## Sample project to reproduce memory leak in android emulator when using OpenGL api

Here is memory profile for this project running on device:
![Device](https://raw.githubusercontent.com/teshca/android-emulator-memory-leak-demo/master/memory_profile_images/device.png)

and on emulator it takes 30 second to get OOM:
![Device](https://raw.githubusercontent.com/teshca/android-emulator-memory-leak-demo/master/memory_profile_images/emulator.png)


The problem demonstarted in this demo:
When OpenGL context is destroyed memory occupied with OpenGl resources is not released on emulator.

To demonstarate this behaviour this sample uploads 1 megabyte buffer in every drawn frame and never delete it.
```
@Override
public void onDrawFrame(GL10 unused) {
    ...
    int[] buffers = new int[1];
    GLES20.glGenBuffers( 1, buffers, 0 );
    int id = buffers[0];
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, id);
    GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, data.length, dataBuffer, GLES20.GL_STATIC_DRAW);
    ...
    // GLES20.glDeleteBuffers(1, buffers, 0);
}
```
This obviously should lead to memory leak, but all this memory should be released once OpenGL context is destroyed.

This sample recreate main activity every 3 seconds
```
@Override
protected void onCreate(Bundle savedInstanceState) {
    ...
    final Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
        @Override
        public void run() {
            recreate();
        }
    }, 3000);
}
```
So every 3 second OpenGL context recreated, and memory occupied with buffers should be released.
It works this way on every device I tested, but not on Emulator
