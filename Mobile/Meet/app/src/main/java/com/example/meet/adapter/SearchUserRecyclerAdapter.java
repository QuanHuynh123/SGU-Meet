package com.example.meet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meet.R;
import com.example.meet.model.AccountUserModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SearchUserRecyclerAdapter extends FirestoreRecyclerAdapter<AccountUserModel,SearchUserRecyclerAdapter.UserModelViewHolder > {

    Context context ;

    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<AccountUserModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder holder, int position, @NonNull AccountUserModel accountUserModel) {
            holder.username.setText(accountUserModel.getName());
    }

    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_cecycler_row, parent , false);
        return new UserModelViewHolder(view);
    }



    class UserModelViewHolder extends RecyclerView.ViewHolder{

        ImageView imgAvt ;
        TextView username ;
        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvt = itemView.findViewById(R.id.avatar);
            username = itemView.findViewById(R.id.name_user);
        }
    }
}
