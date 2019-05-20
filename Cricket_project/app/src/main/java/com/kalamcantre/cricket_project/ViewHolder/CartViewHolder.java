package com.kalamcantre.cricket_project.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.kalamcantre.cricket_project.Interface.ItemClicklistner;
import com.kalamcantre.cricket_project.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtproductname,txtproductprice,txtproductquantity;
    public ItemClicklistner itemClicklistner;

    public CartViewHolder(@NonNull View itemView) {


        super(itemView);
        txtproductname=itemView.findViewById(R.id.product_name1);
        txtproductquantity=itemView.findViewById(R.id.product_quantity);
        txtproductprice=itemView.findViewById(R.id.product_price1);
    }

    @Override
    public void onClick(View v) {
         itemClicklistner.onClick(v,getAdapterPosition(),false);

    }

    public void setItemClicklistner(ItemClicklistner itemClicklistner) {

        this.itemClicklistner = itemClicklistner;

    }
}
