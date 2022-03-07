package Graduation.example;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;




import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class classifictionActivity extends AppCompatActivity {

    private RadioGroup modelChoose;
    private TextView costTime;
    private Button beginInference;
    private ListView picList;

    private String modelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classifiction);

        //模型选择监视
        modelName = "resnet18_original_model.ptl";
        modelChoose = findViewById(R.id.rg_model_choose);
        modelChoose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton modelChoosed = findViewById(i);
                if (modelChoosed.getText() == "原模型")
                    modelName = "resnet18_original_model.ptl;" ;
                else
                    modelName = "resnet18_model_for_mobile.ptl";
                Toast.makeText(classifictionActivity.this, "您选择："+modelChoosed.getText(),Toast.LENGTH_SHORT).show();
            }
        });

        // 原列表设置
        picList = findViewById(R.id.list_view_pic);
        picList.setAdapter(new PicListAdapter(classifictionActivity.this));


//        try {
//            long start = System.currentTimeMillis( );
//            Thread.sleep(60*3);
//            long end = System.currentTimeMillis( );
//            long diff = end - start;
//            String costTimeTemp = String.valueOf(diff);
//            costTime = findViewById(R.id.cost_time);
//            costTime.setText("推理耗时："+costTimeTemp);
//        } catch (Exception e) {
//            System.out.println("Got an exception!");
//        }
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
//            ssdasdasdasd
        }

    }




}