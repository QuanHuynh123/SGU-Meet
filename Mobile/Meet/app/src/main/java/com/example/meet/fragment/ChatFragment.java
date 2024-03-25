package com.example.meet.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.meet.R;
import com.example.meet.adapter.ListViewChatAdapter;
import com.example.meet.model.ChatModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<ChatModel> chatModelFriend;

    private ListView listView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat,container,false);

        listView = view.findViewById(R.id.list_view);
        chatModelFriend = new ArrayList<>();
        chatModelFriend.add(new ChatModel(1,"Quan","Hello anh ban","2h30pm"));
        chatModelFriend.add(new ChatModel(2,"Tai","Choi game khong","2h30pm"));
        chatModelFriend.add(new ChatModel(3,"Wang","Tft ne bro","2h30pm"));
        chatModelFriend.add(new ChatModel(4,"Loc","Tft ne bro","2h30pm"));
        chatModelFriend.add(new ChatModel(5,"Thai","Tft ne bro","2h30pm"));
        chatModelFriend.add(new ChatModel(6,"Quan Pham","Tft ne bro","2h30pm"));
        chatModelFriend.add(new ChatModel(7,"Quan Tan","Tft ne bro","2h30pm"));
        chatModelFriend.add(new ChatModel(8,"Cieu","Tft ne bro","2h30pm"));
        chatModelFriend.add(new ChatModel(9,"Tommy","Tft ne bro","2h30pm"));

        ListViewChatAdapter adapter = new ListViewChatAdapter(requireContext(),R.layout.item_custom_listview, chatModelFriend);
        listView.setAdapter(adapter);
        return view;
    }
}