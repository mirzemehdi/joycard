package com.mmk.joycard;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.mmk.joycard.Constants.UserData;
import com.mmk.joycard.Model.User;

import java.io.IOException;

public class CardScanActivity extends AppCompatActivity {
    private Button backButton;
    private SurfaceView cameraView;
    private TextView resultText;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private View cardImageView;
    private Toolbar toolbar;
    private TextView userDisplayNameTextView,userBalanceTextView;
    private UserData userData;
    private final static int CAMERA_REQUEST_ID=1002;
    int i=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardscan);
        setup();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                Log.d("surfaceCallBack","aaaa");

                if (ActivityCompat.checkSelfPermission(CardScanActivity.this, Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
                    setVisibilities(false);
                    ActivityCompat.requestPermissions(CardScanActivity.this,new String[]{Manifest.permission.CAMERA},CAMERA_REQUEST_ID);

                }


                if (cameraView.getVisibility()==View.VISIBLE){




                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

                cameraSource.stop();

            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();

                if (qrCodes.size() != 0){

                    resultText.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(VIBRATOR_SERVICE);
                            vibrator.vibrate(100);
                            String value=qrCodes.valueAt(0).displayValue;
                            resultText.setText(value);


                        }
                    });


            }


            else{





                }

            }
        });

        cardImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startAnimation();


            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ObjectAnimator oa1 = ObjectAnimator.ofFloat(cameraView, "rotationY", 180f, 0f);
                oa1.setInterpolator(new DecelerateInterpolator());
                oa1.setDuration(300);



                oa1.addListener(new AnimatorListenerAdapter() {


                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        setVisibilities(false);
                        cameraView.setRotationY(0);


                    }


                });

                oa1.start();




            }
        });







    }

    public void setup(){

        backButton=(Button)findViewById(R.id.backButton);

        toolbar=(Toolbar)findViewById(R.id.toolbar_main);
        toolbar.setBackgroundColor(Color.TRANSPARENT);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("JoyCard");
        getSupportActionBar().setLogo(R.drawable.joycard);




        cameraView=(SurfaceView)findViewById(R.id.cameraPreview);
        resultText=(TextView)findViewById(R.id.resultTextView);
        cardImageView=(View) findViewById(R.id.cardImageView);

        barcodeDetector=new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource=new CameraSource.Builder(this,barcodeDetector).build();

        userBalanceTextView=cardImageView.findViewById(R.id.cardBalanceTextView);
        userDisplayNameTextView=cardImageView.findViewById(R.id.cardDisplayNameTextView);

        userData=new UserData();
        userData.getCurrentUserData(new UserData.onGetData() {
            @Override
            public void onSuccess(User currentUser) {


                userBalanceTextView.setText( "Balans: "+currentUser.getBalance()+" AZN"   );
                userDisplayNameTextView.setText(currentUser.getDisplayName());

            }
        });



            }



    public void setVisibilities(boolean visibilityOfCamera){

        if (visibilityOfCamera){

            cardImageView.setVisibility(View.INVISIBLE);
            cameraView.setVisibility(View.VISIBLE);
            resultText.setVisibility(View.VISIBLE);
            backButton.setVisibility(View.VISIBLE);


        }

        else {


            cardImageView.setVisibility(View.VISIBLE);
            cameraView.setVisibility(View.INVISIBLE);
            resultText.setVisibility(View.INVISIBLE);
            backButton.setVisibility(View.INVISIBLE);

        }



    }
    public void startAnimation(){


        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(cardImageView, "rotationY", 0f, 180f);
        oa1.setInterpolator(new DecelerateInterpolator());
        oa1.setDuration(300);



        oa1.addListener(new AnimatorListenerAdapter() {


            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setVisibilities(true);
                cardImageView.setRotationY(0);


            }


        });

        oa1.start();




    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){


            case CAMERA_REQUEST_ID:{
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED) {

                    try {
                        setVisibilities(true);
                        cameraSource.start(cameraView.getHolder());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                else {


                    setVisibilities(false);

                }

            }
            break;

        }
    }



}
