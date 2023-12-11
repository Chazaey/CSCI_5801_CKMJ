package com.example.ckmj_se_project.ui.Need;

import android.util.Log;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ckmj_se_project.R;
import com.example.ckmj_se_project.ui.Backend.ApiManager;
import com.example.ckmj_se_project.ui.Backend.Item;
import com.example.ckmj_se_project.ui.Backend.ItemAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NeedFragment extends Fragment {
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private EditText searchInput;
    private Button searchButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_have, container, false);

        recyclerView = view.findViewById(R.id.itemsRecyclerView);
        searchInput = view.findViewById(R.id.searchEditText);
        searchButton = view.findViewById(R.id.searchButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ItemAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("FamilyPref", Context.MODE_PRIVATE);
        int familyId = sharedPref.getInt("FamilyID", -1);

        Log.d("HaveFragment", "FamilyID" + familyId); //debuggggggggggg

        searchButton.setOnClickListener(v -> performSearch(familyId));

        loadItems(familyId); // 49

        return view;
    }

    private void loadItems(int familyId) {
        ApiManager.getBackendService().searchItems(familyId, "Need", "").enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setItems(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performSearch(int familyId) {
        String query = searchInput.getText().toString();
        ApiManager.getBackendService().searchItems(familyId, "Need", query).enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Item> items = response.body();
                    if (!items.isEmpty()) {
                        adapter.setItems(items);
                    } else {
                        Toast.makeText(getContext(), "No Items found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
