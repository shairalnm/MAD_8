package com.example.inclass09;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    ArrayList<Contact> myContact;
    Context myContext;
    DatabaseReference myRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth;

    public ContactAdapter(ArrayList<Contact> myContact, Context myContext) {
        this.myContact = myContact;
        this.myContext = myContext;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_list, viewGroup, false);

        ImageView contactImage = v.findViewById(R.id.imageView);
        TextView name = v.findViewById(R.id.textView_Name);
        TextView email = v.findViewById(R.id.textView_Email);
        TextView phone = v.findViewById(R.id.textView_PhNum);

        final Contact contact = myContact.get(i);

        //contactImage.setImageDrawable(contact.getImageBitmap());
        name.setText(contact.getName());
        email.setText(contact.getEmail());
        phone.setText(contact.getPhone());

        //contactImage.setImageBitmap(contact.getImageBitmap());

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.name.setText(myContact.get(i).getName());
        viewHolder.email.setText(myContact.get(i).getEmail());
        viewHolder.phone.setText(myContact.get(i).getPhone());
        //viewHolder.contactImage.setImageBitmap(myContact.get(i).getImageBitmap());

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                myRootRef.child("contacts").child(firebaseAuth.getCurrentUser().getUid()).child(myContact.get(i).getPhone()).removeValue();
                myContact.remove(i);
                notifyDataSetChanged();
                return false;
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return myContact.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView contactImage;
        TextView name, email, phone;

        public ViewHolder(View itemView) {
            super(itemView);

            contactImage = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.textView_Name);
            email = itemView.findViewById(R.id.textView_Email);
            phone = itemView.findViewById(R.id.textView_PhNum);
        }
    }
}
