package org.techtown.quizalram;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Toast_Activity_1 extends AppCompatActivity {

    EditText editText;
    ArrayList<String> spinner_list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toast_1);

        editText = findViewById(R.id.toast1_edit);

        Intent intent = getIntent();
        spinner_list = intent.getStringArrayListExtra("spinner_list");

        Button no_button = findViewById(R.id.no_button);
        no_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(102);

                finish();
            }
        });

        Button yes_button = findViewById(R.id.yes_button);
        yes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                boolean value = false;
                if (!editText.getText().toString().equals("")){
                    for (int i = 0; i < spinner_list.size(); i++){
                        if (editText.getText().toString().equals(spinner_list.get(i))){
                            value = true;
                            intent.putExtra("value", i);
                        }
                    }

                    if (!value){
                        intent.putExtra("group", editText.getText().toString());
                    }

                    setResult(101, intent);
                    finish();
                }
            }
        });
    }
}
