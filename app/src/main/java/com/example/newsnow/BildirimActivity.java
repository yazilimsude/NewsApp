package com.example.newsnow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BildirimActivity extends Activity {
    Button kaydet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bildirim);

        kaydet = findViewById(R.id.kaydet);

        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BildirimActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
