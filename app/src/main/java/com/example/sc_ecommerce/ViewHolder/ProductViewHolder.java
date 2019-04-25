package com.example.sc_ecommerce.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sc_ecommerce.Interface.ItemClickListener;
import com.example.sc_ecommerce.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener

{

    public TextView txtproductname, txtproductdesc, txtproductprice;
    public ImageView picproduct;
    public ItemClickListener itemClickListener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        picproduct = (ImageView) itemView.findViewById(R.id.PictProduct);
        txtproductname = (TextView) itemView.findViewById(R.id.TxtProductNameDisplay);
        txtproductdesc = (TextView) itemView.findViewById(R.id.TxtProductDescriptionDisplay);
        txtproductprice = (TextView) itemView.findViewById(R.id.TxtProductPriceDisplay);



    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.itemClickListener = listener;
    }
    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
