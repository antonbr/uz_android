package com.uzapp.view.main.tickets;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;

/**
 * Created by vika on 24.08.16.
 */
public class TicketPageTransformer extends BaseTransformer {

    private static final Matrix OFFSET_MATRIX = new Matrix();
    private static final Camera OFFSET_CAMERA = new Camera();
    private static final float[] OFFSET_TEMP_FLOAT = new float[2];

    @Override
    protected void onTransform(View view, float position) {
        final float rotationY = (position < 0 ? 5f : -5f) * Math.abs(position);
        float translationY = (float) (view.getHeight() * 0.09 * Math.abs(position));
        view.setTranslationX(getOffsetXForRotation(rotationY, view.getWidth(), view.getHeight()));
        view.setTranslationY(translationY);
        view.setPivotX(view.getWidth() * 0.5f);
        view.setPivotY(0);
        view.setRotationY(rotationY);

        final float width = view.getWidth();
        final float height = view.getHeight();
        final float rotation =  position * 4f;

        view.setPivotX(width * 0.5f);
        view.setPivotY(height);
        view.setRotation(rotation);
    }

    protected static final float getOffsetXForRotation(float degrees, int width, int height) {
        OFFSET_MATRIX.reset();
        OFFSET_CAMERA.save();
        OFFSET_CAMERA.rotateY(Math.abs(degrees));
        OFFSET_CAMERA.getMatrix(OFFSET_MATRIX);
        OFFSET_CAMERA.restore();

        OFFSET_MATRIX.preTranslate(-width * 0.5f, -height * 0.5f);
        OFFSET_MATRIX.postTranslate(width * 0.5f, height * 0.5f);
        OFFSET_TEMP_FLOAT[0] = width;
        OFFSET_TEMP_FLOAT[1] = height;
        OFFSET_MATRIX.mapPoints(OFFSET_TEMP_FLOAT);
        return (width - OFFSET_TEMP_FLOAT[0]) * (degrees > 0.0f ? 1.0f : -1.0f);
    }

}