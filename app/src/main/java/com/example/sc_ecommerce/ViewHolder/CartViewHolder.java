package com.example.sc_ecommerce.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.sc_ecommerce.Interface.ItemClickListener;
import com.example.sc_ecommerce.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtproductname, txtproductprice, txtproductqty;
    private ItemClickListener itemClickListener;



    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtproductname = itemView.findViewById(R.id.TxtProductNameCart);
        txtproductqty = itemView.findViewById(R.id.TxtProductQtyCart);
        txtproductprice = itemView.findViewById(R.id.TxtProductPriceCart);

    }

    @Override
    public void onClick(View v)
    {

        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener)
    {

        this.itemClickListener = itemClickListener;
    }
}
