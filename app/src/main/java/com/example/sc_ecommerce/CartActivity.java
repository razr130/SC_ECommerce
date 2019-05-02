package com.example.sc_ecommerce;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.sc_ecommerce.Model.Cart;
import com.example.sc_ecommerce.Prevalent.Prevalent;
import com.example.sc_ecommerce.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recycleone;
    private RecyclerView.LayoutManager layoutmanager;
    private Button btnnext;
    private TextView txttotalprice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recycleone = findViewById(R.id.OrderList_recycler);
        recycleone.setHasFixedSize(true);
        layoutmanager = new LinearLayoutManager(this );
        recycleone.setLayoutManager(layoutmanager);

        btnnext = (Button) findViewById(R.id.BtnNext);
        txttotalprice = (TextView) findViewById(R.id.TxtTotalPrice);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        final DatabaseReference carlistref = FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>().setQuery(carlistref.child("User View")
        .child(Prevalent.onlineuser.getPhone()).child("Products"), Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter  = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model)
            {

                holder.txtproductname.setText(model.getProduct_name());
                holder.txtproductprice.setText("Rp " + model.getProduct_price());
                holder.txtproductqty.setText("Quantity " + model.getQuantity());
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
            {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_items_layout, viewGroup, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        recycleone.setAdapter(adapter);
        adapter.startListening();
    }
}
