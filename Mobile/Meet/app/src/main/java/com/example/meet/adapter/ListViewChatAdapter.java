package com.example.meet.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.meet.R;
import com.example.meet.model.ChatModel;

import java.util.List;

public class ListViewChatAdapter extends BaseAdapter {
    private Context context;
    private int idLayout;
    private List<ChatModel> chatModels;
    private int positionSelect = -1;

    public ListViewChatAdapter(Context context, int idLayout, List<ChatModel> chatModels) {
        this.context = context;
        this.idLayout = idLayout;
        this.chatModels = chatModels;
        this.positionSelect = positionSelect;
    }

    @Override
    public int getCount() {
        if(chatModels.size() != 0 && !chatModels.isEmpty())
            return chatModels.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(idLayout, parent, false);
        }
        TextView edtNameFriends = (TextView) convertView.findViewById(R.id.name_friends);
        TextView edtLastChat = (TextView)  convertView.findViewById(R.id.last_chat);
        TextView timeChat = (TextView) convertView.findViewById(R.id.time_inbox);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.avatar);
        final ConstraintLayout constraintLayout = (ConstraintLayout) convertView.findViewById(R.id.layout_item_listview);
        ChatModel chatModel = chatModels.get(position);

        if(chatModels != null && !chatModels.isEmpty()){
            edtNameFriends.setText(chatModel.getName());
            edtLastChat.setText(chatModel.getChat());
            timeChat.setText(chatModel.getTime());
            imageView.setImageResource(R.drawable.avatar);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, chatModel.getName(),Toast.LENGTH_SHORT).show();
                positionSelect = position;
                notifyDataSetChanged();
            }
        });
        if(positionSelect == position){
            System.out.println("hi " + position);
        }
        else {
            constraintLayout.setBackgroundColor(Color.WHITE);
        }
        return convertView;
    }
}
