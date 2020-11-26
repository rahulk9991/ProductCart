package com.productscart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ViewAllProductsAdapter extends BaseAdapter {
    ProgressDialog progressDialog;
    List<GetAllProductsPojo> getAllProductsPojos;
    Context cnt;
    public ViewAllProductsAdapter(List<GetAllProductsPojo> ar, Context cnt)
    {
        this.getAllProductsPojos=ar;
        this.cnt=cnt;
    }
    @Override
    public int getCount() {
        return getAllProductsPojos.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int pos, View view, ViewGroup viewGroup)
    {
        LayoutInflater obj1 = (LayoutInflater)cnt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View obj2=obj1.inflate(R.layout.list_products,null);

        ImageView image_view=(ImageView)obj2.findViewById(R.id.image_view);
        Glide.with(cnt).load(getAllProductsPojos.get(pos).getImage()).into(image_view);


        TextView tv_cname=(TextView)obj2.findViewById(R.id.tv_cname);
        tv_cname.setText(getAllProductsPojos.get(pos).getName());


        CardView card_view=(CardView)obj2.findViewById(R.id.card_view);
        card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(cnt,ProductBookingActivity.class);
                intent.putExtra("name",getAllProductsPojos.get(pos).getName());
                intent.putExtra("image",getAllProductsPojos.get(pos).getImage());
                intent.putExtra("price",getAllProductsPojos.get(pos).getPrice());
                intent.putExtra("quantity",getAllProductsPojos.get(pos).getQuantity());
                intent.putExtra("description",getAllProductsPojos.get(pos).getDescription());
                cnt.startActivity(intent);
                //((Activity)cnt).finish();

            }
        });



        return obj2;
    }

}