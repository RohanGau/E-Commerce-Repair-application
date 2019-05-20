package com.kalamcantre.cricket_project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kalamcantre.cricket_project.RepairProductViewHolder.Repair;
import com.kalamcantre.cricket_project.RepairProductViewHolder.RepairProductViewHolder;
import com.kalamcantre.cricket_project.ViewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

public class Requestcard extends AppCompatActivity {
    private DatabaseReference productref;
    private FirebaseAuth mAuth;
    private FirebaseRecyclerAdapter<Repair, RepairProductViewHolder> adapter;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Repair> options =
                new FirebaseRecyclerOptions.Builder<Repair>()
                        .setQuery(productref, Repair.class)
                        .build();
        adapter=
                new FirebaseRecyclerAdapter<Repair, RepairProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull RepairProductViewHolder holder, int position, @NonNull Repair model) {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtuserphone.setText(model.getPhone());
                        holder.txtuseraddress.setText(model.getAddress());
                        holder.txtusername.setText(model.getUsername());
                        holder.txtusercity.setText(model.getCity());
                        Picasso.get().load(model.getImage2()).into(holder.imageView);
                    }

                    @NonNull
                    @Override
                    public RepairProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.repair_product_view, viewGroup, false);
                       RepairProductViewHolder holder = new RepairProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requestcard);
        recyclerView=(RecyclerView) findViewById(R.id.Rrecycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAuth = FirebaseAuth.getInstance();

        productref= FirebaseDatabase.getInstance().getReference().child("Repair Products").child(mAuth.getCurrentUser().getUid());


    }
}
