package com.agri.chattla.ui.register;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.agri.chattla.R;

import androidx.appcompat.app.AppCompatActivity;

public class Terms_and_Conditions extends AppCompatActivity {

    private ImageView imageView ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_and_conditions);

        imageView = findViewById(R.id.imageview_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
