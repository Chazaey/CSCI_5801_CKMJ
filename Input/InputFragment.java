package com.example.ckmj_se_project.ui.Input;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ckmj_se_project.R;
import com.example.ckmj_se_project.ui.Backend.ApiManager;
import com.example.ckmj_se_project.ui.Backend.Item;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InputFragment extends Fragment {

    private EditText itemNameInput, itemAmountInput;
    private Button buttonAddToHave, buttonAddToNeed;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input, container, false);

        itemNameInput = view.findViewById(R.id.itemNameInput);
        itemAmountInput = view.findViewById(R.id.itemAmountInput);
        buttonAddToHave = view.findViewById(R.id.buttonAddToHave);
        buttonAddToNeed = view.findViewById(R.id.buttonAddToNeed);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("FamilyPref", Context.MODE_PRIVATE);
        int familyId = sharedPref.getInt("FamilyID", -1);

        buttonAddToHave.setOnClickListener(v -> addItem(familyId, "Have"));
        buttonAddToNeed.setOnClickListener(v -> addItem(familyId, "Need"));

        return view;
    }

    private void addItem(int familyId, String itemStatus) {
        String itemName = itemNameInput.getText().toString().trim();
        Log.d("InputFragment","Name2: " + itemNameInput.getText().toString().trim());
        String itemAmountString = itemAmountInput.getText().toString().trim();
        int itemAmount = itemAmountString.isEmpty() ? 1 : Integer.parseInt(itemAmountString); // Default to 1 if blank

        if (!itemName.isEmpty()) {
            Log.d("InputFragment","Name2: " + itemNameInput.getText().toString().trim());
            Item item = new Item(itemName, itemAmount, itemStatus, familyId);
            ApiManager.getBackendService().postItem(item).enqueue(postItemCallback); // Assuming ApiManager is properly initialized
            resetInputFields();
        } else {
            Toast.makeText(getActivity(), "Please enter item name", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetInputFields() {
        itemNameInput.setText("");
        itemAmountInput.setText("");
    }

    // Callback for posting item
    private Callback<Item> postItemCallback = new Callback<Item>() {
        @Override
        public void onResponse(Call<Item> call, Response<Item> response) {
            if (response.isSuccessful()) {
                Toast.makeText(getActivity(), "Item Added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Failed to add item", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<Item> call, Throwable t) {
            Toast.makeText(getActivity(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };
}

