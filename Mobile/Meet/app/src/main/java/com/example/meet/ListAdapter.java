package com.example.meet;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.meet.model.ListChat;

import java.util.List;

public class ListAdapter extends BaseAdapter {
    private Context context;
    private int idLayout;
    private List<ListChat> listChats;
    private int positionSelect = -1;

    public ListAdapter(Context context, int idLayout, List<ListChat> listChats) {
        this.context = context;
        this.idLayout = idLayout;
        this.listChats = listChats;
        this.positionSelect = positionSelect;
    }

    @Override
    public int getCount() {
        if(listChats.size() != 0 && !listChats.isEmpty())
            return listChats.size();
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
        ListChat listChat = listChats.get(position);

        if(listChats != null && !listChats.isEmpty()){
            edtNameFriends.setText(listChat.getName());
            edtLastChat.setText(listChat.getChat());
            timeChat.setText(listChat.getTime());
            imageView.setImageResource(R.drawable.avatar);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,listChat.getName(),Toast.LENGTH_SHORT).show();
                positionSelect = position;
                notifyDataSetChanged();
            }
        });
        if(positionSelect == position){
            constraintLayout.setBackgroundColor(Color.BLACK);
        }
        else {
            constraintLayout.setBackgroundColor(Color.WHITE);
        }
        return convertView;
    }
}
