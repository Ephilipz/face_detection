package com.example.eesab.facialrecognition;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button processBtn;
    private ImageView currentImage_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        processBtn = findViewById(R.id.process_btn);
        processBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.process_btn:
                currentImage_iv = findViewById(R.id.image_iv);
                //Load the image
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inMutable = true;
                Bitmap myBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.test1,options);

                //Create a Paint object to draw with
                Paint myRectPaint = new Paint();
                myRectPaint.setStrokeWidth(12);
                myRectPaint.setColor(Color.RED);
                myRectPaint.setStyle(Paint.Style.STROKE);

                //Create a canvas object to draw on
                Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(),myBitmap.getHeight(), Bitmap.Config.RGB_565);
                Canvas tempCanvas = new Canvas(tempBitmap);
                tempCanvas.drawBitmap(myBitmap,0,0, null);

                //Create face detector
                FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext()).build();
                if(!faceDetector.isOperational()){ //if face detector doesn't work create dialog
                    new AlertDialog.Builder(view.getContext()).setMessage("Could not set up the face detector!").show();
                    return;
                }

                //Detect faces
                Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
                SparseArray<Face> faces = faceDetector.detect(frame);

                //Draw rectangles on faces
                for (int i = 0; i < faces.size(); i++) {
                    Face thisFace = faces.valueAt(i);
                    //get positions of the face
                    Float x1 = thisFace.getPosition().x;
                    Float y1 = thisFace.getPosition().y;
                    Float x2 = x1 + thisFace.getWidth();
                    Float y2 = y1 + thisFace.getHeight();
                    tempCanvas.drawRoundRect(new RectF(x1,y1,x2,y2), 2, 2, myRectPaint);
                }
                currentImage_iv.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
        }
    }
}
