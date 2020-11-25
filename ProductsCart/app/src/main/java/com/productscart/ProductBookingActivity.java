package com.productscart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class ProductBookingActivity extends AppCompatActivity {

    ImageView iv_image;
    TextView tv_name,tv_price,tv_quantity,tv_description;
    Button btn_book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_booking);

        getSupportActionBar().setTitle("Product Booking");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        iv_image=(ImageView)findViewById(R.id.iv_image);
        tv_name=(TextView)findViewById(R.id.tv_name);
        tv_price=(TextView)findViewById(R.id.tv_price);
        tv_quantity=(TextView)findViewById(R.id.tv_quantity);
        tv_description=(TextView)findViewById(R.id.tv_description);

        tv_name.setText("Name :"+getIntent().getStringExtra("name"));
        tv_price.setText("Price :"+getIntent().getStringExtra("price"));
        tv_quantity.setText("Quantity :"+getIntent().getStringExtra("quantity"));
        tv_description.setText("Description  :"+getIntent().getStringExtra("description"));

        Glide.with(ProductBookingActivity.this).load(getIntent().getStringExtra("image")).into(iv_image);



        btn_book=(Button)findViewById(R.id.btn_book);
        btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProductBookingActivity.this, getIntent().getStringExtra("name")+" Product Booked Succussfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ProductBookingActivity.this,ViewProductsActivity.class));
                finish();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}