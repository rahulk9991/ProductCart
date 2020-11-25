package com.productscart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DashboardActivity extends AppCompatActivity {
    Button btn_add_product,btn_view_product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        getSupportActionBar().setTitle("Dashboard");

        btn_add_product=(Button)findViewById(R.id.btn_add_product);
        btn_view_product=(Button)findViewById(R.id.btn_view_product);
        btn_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(DashboardActivity.this,MainActivity.class));
            }
        });

        btn_view_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(DashboardActivity.this,ViewProductsActivity.class));
            }
        });
    }
}