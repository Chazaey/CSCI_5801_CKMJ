package com.example.ckmj_se_project.ui.Family;



import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ckmj_se_project.R;
import com.example.ckmj_se_project.ui.Backend.ApiManager;
import com.example.ckmj_se_project.ui.Backend.BackendService;
import com.example.ckmj_se_project.ui.Family.Family;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FamilyFragment extends Fragment {
    private EditText familyNameInput, familyEmailInput, familyIdInput;
    private Button buttonCreateFamily, buttonInputFamilyCode, submitButton;
    private TextView familyLinkedText, familyNameDisplay, familyCodeDisplay, familyEmailDisplay, orTextView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_family, container, false);

        familyLinkedText = view.findViewById(R.id.familyLinkedText);
        familyNameDisplay = view.findViewById(R.id.familyNameDisplay);
        familyCodeDisplay = view.findViewById(R.id.familyCodeDisplay);
        familyEmailDisplay = view.findViewById(R.id.familyEmailDisplay);
        orTextView = view.findViewById(R.id.orText);

        familyNameInput = view.findViewById(R.id.familyNameInput);
        familyEmailInput = view.findViewById(R.id.familyEmailInput);
        familyIdInput = view.findViewById(R.id.familyIdInput);
        buttonCreateFamily = view.findViewById(R.id.buttonCreateFamily);
        buttonInputFamilyCode = view.findViewById(R.id.buttonInputFamilyCode);
        submitButton = view.findViewById(R.id.submitButton);

        buttonCreateFamily.setOnClickListener(v -> showFamilyCreationFields());
        buttonInputFamilyCode.setOnClickListener(v -> showFamilyInputFields());
        submitButton.setOnClickListener(v -> submitFamilyDetails());

        checkIfFamilyDetailsStored();

        return view;
    }

    private void showFamilyCreationFields() {
        familyNameInput.setVisibility(View.VISIBLE);
        familyEmailInput.setVisibility(View.VISIBLE);
        familyIdInput.setVisibility(View.GONE);
        submitButton.setVisibility(View.VISIBLE);
    }

    private void showFamilyInputFields() {
        familyIdInput.setVisibility(View.VISIBLE);
        familyNameInput.setVisibility(View.GONE);
        familyEmailInput.setVisibility(View.GONE);
        submitButton.setVisibility(View.VISIBLE);
    }

    private void submitFamilyDetails() {
        if (familyIdInput.getVisibility() == View.VISIBLE) {
            int FamilyId = Integer.parseInt(familyIdInput.getText().toString());
            fetchFamilyDetails(FamilyId);
            Log.d("FamilyFragment", "FamilyID1:" + FamilyId);
        } else {
            createNewFamily();
        }
    }

    private void fetchFamilyDetails(int familyId) {
        Log.d("FamilyFragment", "FamilyID5:" + familyId);
        ApiManager.getBackendService().getFamilyByFamilyId(familyId).enqueue(new Callback<Family>() {
            @Override
            public void onResponse(Call<Family> call, Response<Family> response) {
                if (response.isSuccessful()) {
                    Family family = response.body();
                    if (family != null) {
                        Log.d("FamilyFragment", "FamilyID2:" + family.getFamilyId());
                        // Store family details in shared preferences
                        storeFamilyDetailsInSharedPreferences(family);
                        checkIfFamilyDetailsStored();
                    } else {
                        Toast.makeText(getActivity(), "Family data is empty", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Family not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Family> call, Throwable t) {
                Toast.makeText(getActivity(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void createNewFamily() {
        String familyName = familyNameInput.getText().toString();
        String email = familyEmailInput.getText().toString();

        if (!familyName.isEmpty() && !email.isEmpty()) {
            Family newFamily = new Family(email, familyName);
            ApiManager.getBackendService().postFamily(newFamily).enqueue(createFamilyCallback);
        } else {
            Toast.makeText(getActivity(), "Please enter all details", Toast.LENGTH_SHORT).show();
        }
    }

    // retrofit for creating a family
    private Callback<Family> createFamilyCallback = new Callback<Family>() {
        @Override
        public void onResponse(Call<Family> call, Response<Family> response) {
            if (response.isSuccessful() && response.body() != null) {
                Family family = response.body();
                // Store family details in shared preferences
                storeFamilyDetailsInSharedPreferences(family);
                checkIfFamilyDetailsStored();
            } else {
                Toast.makeText(getActivity(), "Failed to create family", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<Family> call, Throwable t) {
            Toast.makeText(getActivity(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    // Make FamilyID a permanent object
    private void storeFamilyDetailsInSharedPreferences(Family family) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("FamilyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("FamilyID", family.getFamilyId());
        editor.putString("FamilyName", family.getName());
        editor.putString("Email", family.getEmail());
        editor.apply();
    }


    // Display Family after submission
    private void checkIfFamilyDetailsStored() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("FamilyPref", Context.MODE_PRIVATE);
        int familyId = sharedPref.getInt("FamilyID", 0);
        String familyName = sharedPref.getString("FamilyName", "");
        String email = sharedPref.getString("Email", "");

        Log.d("FamilyFragment", "Stored Family ID: " + familyId);
        Log.d("FamilyFragment", "Stored Family Name: " + familyName);
        Log.d("FamilyFragment", "Stored Email: " + email);

        if (familyId != 0) {
            // Family details are stored, update UI
            Log.d("FamilyFragment", "No family details stored in SharedPreferences.");

            updateFamilyDetailsUI(familyName, email, familyId);
        }
    }
    private void updateFamilyDetailsUI(String familyName, String email, int familyId) {

        familyLinkedText.setVisibility(View.VISIBLE);
        familyNameDisplay.setVisibility(View.VISIBLE);
        familyCodeDisplay.setVisibility(View.VISIBLE);
        familyEmailDisplay.setVisibility(View.VISIBLE);

        familyNameDisplay.setText("Family Name: " + familyName);
        familyCodeDisplay.setText("Family Code: " + familyId);
        familyEmailDisplay.setText("Email: " + email);

        // Hide the input fields and buttons
        familyNameInput.setVisibility(View.GONE);
        familyEmailInput.setVisibility(View.GONE);
        familyIdInput.setVisibility(View.GONE);
        buttonCreateFamily.setVisibility(View.GONE);
        buttonInputFamilyCode.setVisibility(View.GONE);
        submitButton.setVisibility(View.GONE);
        orTextView.setVisibility(View.GONE);
    }
}
