package Graduation.example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

  private Button MainDoucumentionButton;
  private Button MainClassifictionButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    // 使用说明按钮
    MainDoucumentionButton = findViewById(R.id.main_doucumention_btn);
    MainDoucumentionButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this, doucumentionActivity.class);
        startActivity(intent);
      }
    });

    // 开始使用按钮
    MainClassifictionButton = findViewById(R.id.main_classification_btn);
    MainClassifictionButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this, classifictionActivity.class);
        startActivity(intent);
      }
    });
  }
}