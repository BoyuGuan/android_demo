package Graduation.example;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.pytorch.IValue;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;
import org.pytorch.MemoryFormat;
import org.pytorch.LiteModuleLoader;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class classifictionActivity extends AppCompatActivity {

    private RadioGroup modelChoose, gallery;
    private RadioButton model_1,model_2, gallery_1, gallery_2;

    private TextView costTime;
    private Button beginInference;
    private ListView picList;

    private String modelName;
    private int galleryName;
    private static final int imageNumber  = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classifiction);

        //模型选择监视
        modelName = "resnet18_original_model.pt";
        modelChoose = findViewById(R.id.rg_model_choose);
        model_1 = findViewById(R.id.rb_original_model);
        model_2 = findViewById(R.id.rb_compressed_model);
        modelChoose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton modelChoosed = findViewById(i);
                if (model_1.isChecked())
                    modelName = "resnet18_original_model.pt" ;
                else
                    modelName = "resnet18_model_for_mobile.pt";
                Toast.makeText(classifictionActivity.this, "您选择："+modelChoosed.getText(),Toast.LENGTH_SHORT).show();
            }
        });

        // 图库选择监视
        galleryName = 0;
        gallery = findViewById(R.id.rg_gallery_choose);
        gallery_1 = findViewById(R.id.rb_gallery_1);
        gallery_2 = findViewById(R.id.rb_gallery_2);

        gallery.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (gallery_1.isChecked())
                    galleryName = 0;
                else
                    galleryName = 1;
                Toast.makeText(classifictionActivity.this, "您选择第"+ String.valueOf(galleryName+1)+"组图片",Toast.LENGTH_SHORT).show();
            }
        });


        // 原列表设置
        picList = findViewById(R.id.list_view_pic);
        picList.setAdapter(new PicListAdapterBeforeClassification(classifictionActivity.this, galleryName));

        costTime = findViewById(R.id.cost_time);
        costTime.setText("推理耗时：");

        beginInference = findViewById(R.id.classification_start_infer);
        final String[] classNameInference = new String[imageNumber];

        beginInference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Module module = null;
                Bitmap[] bitmapOfPic = new Bitmap[imageNumber];
                try {
                    // creating bitmap from packaged into app android asset 'image.jpg',
                    // app/src/main/assets/image3.jpg
                    for (int picNO = 0; picNO < imageNumber; picNO++)
                        bitmapOfPic[picNO] = BitmapFactory.decodeStream(getAssets().open("image_"+String.valueOf(galleryName)+"_" +String.valueOf(picNO)+".jpg"));
                    // loading serialized torchscript module from packaged into app android asset model.pt,
                    // app/src/model/assets/model.pt
                    module = LiteModuleLoader.load(assetFilePath(classifictionActivity.this, modelName));
                } catch (Exception e) {
                    System.out.println("Got an exception!");
                }

                float[] NORM_MEAN = new float[] {0.4914f, 0.4822f, 0.4465f} ;
                float[] NORM_STD = new float[] {0.2023f, 0.1994f, 0.2010f} ;

                long diff = 0;

                for (int picNO = 0; picNO < imageNumber; picNO++) {

                    Tensor inputTensor = TensorImageUtils.bitmapToFloat32Tensor(bitmapOfPic[picNO],
                            NORM_MEAN,NORM_STD, MemoryFormat.CHANNELS_LAST);

                    // 计时开始
                    long start = System.currentTimeMillis( );
                    Tensor outputTensor = module.forward(IValue.from(inputTensor)).toTensor();
                    long end = System.currentTimeMillis( );
                    diff += end - start;

                    float[] scores = outputTensor.getDataAsFloatArray();
                    // searching for the index with maximum score
                    float maxScore = -Float.MAX_VALUE;
                    int maxScoreIdx = -1;
                    for (int i = 0; i < scores.length; i++) {
                        if (scores[i] > maxScore) {
                            maxScore = scores[i];
                            maxScoreIdx = i;
                        }
                    }
                    classNameInference[picNO] = cifarClass.CIFAR_CLASS[maxScoreIdx];
                }

                // 更新点之后的列表
                picList = findViewById(R.id.list_view_pic);
                picList.setAdapter(new PicListAdapterAfterClassification(classifictionActivity.this, classNameInference ,galleryName));




                String costTimeOfClassification = String.valueOf(diff);
                costTime.setText("推理耗时：" + costTimeOfClassification);

            }
        });


    }


    /**
     * Copies specified asset to the file in /files app directory and returns this file absolute path.
     *
     * @return absolute file path
     */
    public static String assetFilePath(Context context, String assetName) throws IOException {
        File file = new File(context.getFilesDir(), assetName);
        if (file.exists() && file.length() > 0) {
            return file.getAbsolutePath();
        }

        try (InputStream is = context.getAssets().open(assetName)) {
            try (OutputStream os = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    os.write(buffer, 0, read);
                }
                os.flush();
            }
            return file.getAbsolutePath();
        }

    }



}