package com.example.sc_ecommerce;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sc_ecommerce.Model.Cart;
import com.example.sc_ecommerce.Prevalent.Prevalent;
import com.example.sc_ecommerce.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recycleone;
    private RecyclerView.LayoutManager layoutmanager;
    private Button btnnext;
    private TextView txttotalprice;
    private int totalpriceall = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recycleone = findViewById(R.id.OrderList_recycler);
        recycleone.setHasFixedSize(true);
        layoutmanager = new LinearLayoutManager(this);
        recycleone.setLayoutManager(layoutmanager);

        btnnext = (Button) findViewById(R.id.BtnNext);
        txttotalprice = (TextView) findViewById(R.id.TxtTotalPrice);

    }

    @Override
    protected void onStart() {
        super.onStart();
        final DatabaseReference carlistref = FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>().setQuery(carlistref.child("User View")
                .child(Prevalent.onlineuser.getPhone()).child("Products"), Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {

                holder.txtproductname.setText(model.getProduct_name());
                holder.txtproductprice.setText("Rp " + model.getProduct_price());
                holder.txtproductqty.setText("Quantity " + model.getQuantity());

                int totalpricestart = Integer.valueOf(model.getProduct_price()) * Integer.valueOf(model.getQuantity());
                totalpriceall = totalpriceall + totalpricestart;
                txttotalprice.setText("Total Price = " + totalpriceall);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Edit",
                                        "Delete"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if (i == 0) {
                                    Intent intent = new Intent(CartActivity.this, ProductDetailActivity.class);
                                    intent.putExtra("product_id", model.getProduct_id());
                                    startActivity(intent);
                                }
                                if (i == 1) {
                                    carlistref.child("User View").child(Prevalent.onlineuser.getPhone())
                                            .child("Products").child(model.getProduct_id()).removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(CartActivity.this, "Cart item is deleted!",
                                                                Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_items_layout, viewGroup, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        recycleone.setAdapter(adapter);
        adapter.startListening();
    }

}
