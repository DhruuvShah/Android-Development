package com.jg.imca_d_new;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.SubClass>{
    ArrayList<UserData> user = new ArrayList<>();
    public UserAdapter(ArrayList<UserData> data) {
        this.user = data;
    }

    @NonNull
    @Override // for assign the custom design file.
    public SubClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_user_design,parent,false);
        return new SubClass(view);
    }

    @Override // merging of design and data
    public void onBindViewHolder(@NonNull SubClass holder, int position) {
        UserData users = user.get(position); // 0 {hello,hello1} , 1 {"abc",xyz"}

        holder.txtFname.setText(users.fName);
        holder.txtLname.setText(users.lName);
    }

    @Override // decide how many data do you want to show.
    public int getItemCount() {
        return user.size();
    }

    class SubClass extends RecyclerView.ViewHolder{

        TextView txtFname,txtLname;

        public SubClass(@NonNull View itemView) {
            super(itemView);

            txtFname = itemView.findViewById(R.id.txtFirstName);
            txtLname = itemView.findViewById(R.id.txtLastName);

        }
    }
}
