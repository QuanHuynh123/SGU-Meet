package com.example.meet.fragment;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.meet.R;
import com.example.meet.adapter.SearchUserRecyclerAdapter;
import com.example.meet.utils.Firebaseutil;

import retrofit2.http.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private EditText searchInput;
    private ImageButton searchButton;

    SearchUserRecyclerAdapter adapter;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        View view = inflater.inflate(R.layout.fragment_search,container,false);

        recyclerView =  view.findViewById(R.id.recyclerview_search_friend);
        searchButton = view.findViewById(R.id.btn_search_user);
        searchInput = view.findViewById(R.id.input_search_user);

        searchInput.requestFocus();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm =  searchInput.getText().toString();
                if (searchTerm.isEmpty() || searchTerm.length() < 3){
                    searchInput.setError("Invalid Username");
                    return;
                }
                setupSearchRecyclerView(searchTerm);
            }
        });

//        listView = view.findViewById(R.id.listview_search_friend);
//        searchFriendModels = new ArrayList<>();
//        searchFriendModels.add(new SearchFriendModel(1,"Quan"));
//        searchFriendModels.add(new SearchFriendModel(2,"Tai"));
//        searchFriendModels.add(new SearchFriendModel(3,"Wang"));
//        searchFriendModels.add(new SearchFriendModel(4,"Loc"));
//        searchFriendModels.add(new SearchFriendModel(5,"Thai"));
//        searchFriendModels.add(new SearchFriendModel(6,"Quan Pham"));
//        searchFriendModels.add(new SearchFriendModel(7,"Quan Tan"));
//        searchFriendModels.add(new SearchFriendModel(8,"Cieu"));
//        searchFriendModels.add(new SearchFriendModel(9,"Tommy"));
//
//        ListSearchAdapter adapter = new ListSearchAdapter(requireContext(),R.layout.item_custom_listview_search, searchFriendModels);
//        listView.setAdapter(adapter);
        return view;
    }

    @SuppressLint("RestrictedApi")
    private void setupSearchRecyclerView(String searchTerm) {

        Query query = Firebaseutil.allUserCollectionReference()
                .whereGreaterThanOrEqualTo("")
                
        adapter  = new SearchUserRecyclerAdapter(, requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter!= null){
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter!= null){
            adapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter!= null){
            adapter.startListening();
        }
    }
}