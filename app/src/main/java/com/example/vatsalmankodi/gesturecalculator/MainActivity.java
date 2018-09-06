package com.example.vatsalmankodi.gesturecalculator;

import android.annotation.TargetApi;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;

public class MainActivity extends AppCompatActivity {

    static Bitmap viewCapture;
    static private TessBaseAPI mTess;
    String datapath = "";
    View drawView;
    static ImageView stillScreen;
    static LinearLayout linearLayout ;
    static TextView OCRTextView;
    static TextView AnswerTextView;
    static Button RunOCRbtn;
    static FingerDraw fingerDraw;
    static String toEvaluate = "";
    static String whitelist = "1234567890.-/+X";
    static double answer = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        linearLayout =  (LinearLayout) findViewById(R.id.linearLayout);
        OCRTextView =(TextView)findViewById(R.id.OCRTextView);
        AnswerTextView = (TextView)findViewById(R.id.AnswerTextView);
        fingerDraw = new FingerDraw(this);
        RunOCRbtn = (Button) findViewById(R.id.RunOCRbtn);
        RunOCRbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processImage();
            }
        });
        AnswerTextView.setText(Double.toString(answer));

        addDrawView();

        //initialize Tesseract API
        String language = "eng";
        datapath = getFilesDir()+ "/tesseract/";
        mTess = new TessBaseAPI();

        checkFile(new File(datapath + "tessdata/"));

        mTess.init(datapath, language);
    }

    public static void addDrawView(){

        fingerDraw.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        linearLayout.addView(fingerDraw);
    }

    public static void processImage(){
        mTess.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SINGLE_LINE);
        mTess.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST,whitelist);
        String OCRresult = null;
        mTess.setImage(viewCapture);
        OCRresult = mTess.getUTF8Text();
        toEvaluate += OCRresult;
       // if(!CalculatorFunctions.isOperator(OCRresult.charAt(0))){

        answer = CalculatorFunctions.evaluatePostfix(CalculatorFunctions.toPostfix(toEvaluate));
        AnswerTextView.setText(Double.toString(answer));

        OCRTextView.setText(toEvaluate);
    }
    public void clear(View view){
        answer=0;
        toEvaluate="";
        OCRTextView.setText(toEvaluate);
        AnswerTextView.setText(Double.toString(answer));

    }
    public void backspace(View view){
        if(toEvaluate.length()!=0)
            toEvaluate = toEvaluate.substring(0,toEvaluate.length()-1);
        OCRTextView.setText(toEvaluate);
    }

    public void refreshString(View view){
        toEvaluate="";
        OCRTextView.setText(toEvaluate);
    }
    public void clearBitmap(View view){
        linearLayout.removeView(fingerDraw);
        fingerDraw = new FingerDraw(this);
       // OCRTextView.setText("");
        addDrawView();
    }

    private void checkFile(File dir) {
        if (!dir.exists()&& dir.mkdirs()){
                copyFiles();
        }
        if(dir.exists()) {
            String datafilepath = datapath+ "/tessdata/eng.traineddata";
            File datafile = new File(datafilepath);

            if (!datafile.exists()) {
                copyFiles();
            }
        }
    }

    private void copyFiles() {
        try {
            String filepath = datapath + "/tessdata/eng.traineddata";
            AssetManager assetManager = getAssets();

            InputStream instream = assetManager.open("tessdata/eng.traineddata");
            OutputStream outstream = new FileOutputStream(filepath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }


            outstream.flush();
            outstream.close();
            instream.close();

            File file = new File(filepath);
            if (!file.exists()) {
                throw new FileNotFoundException();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
