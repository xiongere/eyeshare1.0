package com.classify.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.pytorch.IValue;
import org.pytorch.LiteModuleLoader;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DetectResultActivity extends AppCompatActivity {

    ImageView mImageView;
    ResultView mResultView;
    TextView mTextResult;

    private Module mModule = null;
    private float mImgScaleX, mImgScaleY, mIvScaleX, mIvScaleY, mStartX, mStartY;

    String mResultString = "";


    public static String assetFilePath(Context context, String assetName) throws IOException{
        File file = new File(context.getFilesDir(), assetName);
        if (file.exists() && file.length() > 0){
            return file.getAbsolutePath();
        }

        try (InputStream is = context.getAssets().open(assetName)){
            try (OutputStream os = new FileOutputStream(file)){
                byte[] buffer = new byte[4 * 1024];
                int read;
                while ((read = is.read(buffer)) != -1){
                    os.write(buffer,0,read);
                }
                os.flush();
            }
            return file.getAbsolutePath();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // 读取文件，加载模型
        try {
            mModule = LiteModuleLoader.load(assetFilePath(getApplicationContext(), "yolov5s.torchscript.ptl"));
            BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("classes.txt")));
            String line;
            List<String> classes = new ArrayList<>();//classes 放置classes
            while ((line = br.readLine()) != null){
                classes.add(line);
            }
            //
            PrePostProcessor.mClasses = new String[classes.size()];
            classes.toArray(PrePostProcessor.mClasses);
        } catch (IOException e) {
            Log.e("Object Detection","Error reading assets",e);
            finish();
        }


        setContentView(R.layout.activity_detect_result);

        mImageView = findViewById(R.id.imageView);
        mResultView = findViewById(R.id.resultView);

        mTextResult = findViewById(R.id.textResult);


        DataService instance = DataService.getInstance();

        if (instance.getmBitmap()!=null){
            Bitmap mBitmap = instance.getmBitmap();

            mImageView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mImageView.setImageBitmap(mBitmap); // 先把传进来的图片显示在界面中


                                    mImgScaleX = (float)mBitmap.getWidth() / PrePostProcessor.mInputWidth;          // 获取图片放大比例 mInputWidth = 640
                                    mImgScaleY = (float)mBitmap.getHeight() / PrePostProcessor.mInputHeight;

                                    mIvScaleX = (mBitmap.getWidth() > mBitmap.getHeight() ? (float)mImageView.getWidth() / mBitmap.getWidth() : (float)mImageView.getHeight() / mBitmap.getHeight());
                                    mIvScaleY  = (mBitmap.getHeight() > mBitmap.getWidth() ? (float)mImageView.getHeight() / mBitmap.getHeight() : (float)mImageView.getWidth() / mBitmap.getWidth());


                                    mStartX = (mImageView.getWidth() - mIvScaleX * mBitmap.getWidth())/2;
                                    mStartY = (mImageView.getHeight() -  mIvScaleY * mBitmap.getHeight())/2;


                                    // 缩放Bitmap
                                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(mBitmap, PrePostProcessor.mInputWidth, PrePostProcessor.mInputHeight, true);
                                    // Bitmap -> Tensor
                                    final Tensor inputTensor = TensorImageUtils.bitmapToFloat32Tensor(resizedBitmap, PrePostProcessor.NO_MEAN_RGB, PrePostProcessor.NO_STD_RGB);    // 将bitmap转为Tensor
                                    IValue[] outputTuple = mModule.forward(IValue.from(inputTensor)).toTuple();     // 前向传播
                                    final Tensor outputTensor = outputTuple[0].toTensor();                          // 获取结果里面的值
                                    final float[] outputs = outputTensor.getDataAsFloatArray();                     // 将结果转为浮点矩阵
                                    final ArrayList<Result> results =  PrePostProcessor.outputsToNMSPredictions(outputs, mImgScaleX, mImgScaleY, mIvScaleX, mIvScaleY, mStartX, mStartY);   // 非极大值抑制



                                    mResultView.setResults(results);
                                    mResultView.invalidate();
                                    mResultView.setVisibility(View.VISIBLE);
                                    Set<Integer> set = new HashSet<>();

                                    for(int i = 0;i<results.size();i++){
                                        set.add(results.get(i).classIndex);
                                    }
                                    List<Integer> list = new ArrayList<>(set);
                                    Collections.sort(list);
                                    for(int i =0;i<list.size();i++){
                                        int count=0;
                                        for(int j=0;j<results.size();j++){
                                            if(list.get(i).equals(results.get(j).classIndex)){
                                                count++;
                                            }
                                        }
                                        mResultString += PrePostProcessor.mClasses[list.get(i)]+" 数目："+count+"\n";
                                    }
                                    mTextResult.setText(mResultString);

                                }
                            }

            );







        }



    }
}