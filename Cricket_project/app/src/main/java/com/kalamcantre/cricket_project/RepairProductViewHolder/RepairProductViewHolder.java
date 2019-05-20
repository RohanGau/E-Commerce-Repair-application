package com.kalamcantre.cricket_project.RepairProductViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kalamcantre.cricket_project.Interface.ItemClicklistner;
import com.kalamcantre.cricket_project.R;

public class RepairProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, txtProductDescription, txtusername,txtuseraddress,txtuserphone,txtusercity;
    public ImageView imageView;
    public ItemClicklistner listner;
    public RepairProductViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.product_image);
        txtProductName = (TextView) itemView.findViewById(R.id.R_product_name);
        txtProductDescription = (TextView) itemView.findViewById(R.id.R_product_description);
        txtuseraddress = (TextView) itemView.findViewById(R.id.R_user_address);
        txtusername = (TextView) itemView.findViewById(R.id.R_user_name);
        txtusercity = (TextView) itemView.findViewById(R.id.R_user_city);
        txtuserphone = (TextView) itemView.findViewById(R.id.R_user_phone);

    }
    public void setItemClickListner(ItemClicklistner listner) {

        this.listner = listner;

    }

    @Override
    public void onClick(View v) {
        listner.onClick(v, getAdapterPosition(), false);
    }
}
