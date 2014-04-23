package edu.ycp.cs496.main;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawView extends View {
    Paint paint;
    Context c;
    float x;
    float y;
    private Bitmap shipBitMap;

    public DrawView(Resources res,Context context) {
       super(context);
 
       shipBitMap = BitmapFactory.decodeResource(res, R.drawable.image_ship);
    }

    @Override
    public void onDraw(Canvas canvas) {
    	canvas.drawBitmap(shipBitMap, x, y, paint);
    }
 }