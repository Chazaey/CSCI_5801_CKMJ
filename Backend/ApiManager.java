package com.example.ckmj_se_project.ui.Backend;
import com.example.ckmj_se_project.ui.Family.Family;
import com.example.ckmj_se_project.ui.Family.FamilyFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public class ApiManager {

    private static BackendService backendService;

    // Private constructor to prevent direct instantiation
    private ApiManager() {
    }

    // method to get the instance of BackendService
    public static BackendService getBackendService() {
        if (backendService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://e43f-2603-6010-4402-f8cd-8c35-bfd5-eb43-58bb.ngrok.io")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            backendService = retrofit.create(BackendService.class);
        }
        return backendService;
    }

    public void getItemsHave(int familyId, Callback<List<Item>> callback) {
        backendService.getItemsByFamilyAndStatus(familyId, "Have").enqueue(callback);
    }

    public void getItemsNeed(int familyId, Callback<List<Item>> callback) {
        backendService.getItemsByFamilyAndStatus(familyId, "Need").enqueue(callback);
    }

    public void getFamilyByEmailAndName(String email, String name, Callback<Family> callback) {
        backendService.getFamilyByEmailAndName(email, name).enqueue(callback);
    }

    public void postFamily(Family family, Callback<Family> callback) {
        backendService.postFamily(family).enqueue(callback);
    }

    public void getFamilyByFamilyId(int familyId, Callback<Family> callback) {
        Call<Family> call = backendService.getFamilyByFamilyId(familyId);
        call.enqueue(callback);
    }

    public void postItem(Item item, Callback<Item> callback) {
        backendService.postItem(item).enqueue(callback);
    }

    public void deleteItem(int itemId, Callback<Void> callback) {
        backendService.deleteItem(itemId).enqueue(callback);
    }

    public static void updateItemToHave(int itemId, Callback<Void> callback) {
        backendService.updateItemToHave(itemId).enqueue(callback);
    }

    public static void updateItemToNeed(int itemId, Callback<Void> callback) {
        backendService.updateItemToNeed(itemId).enqueue(callback);
    }

    public static void searchItems(int familyId, String itemStatus, String searchQuery, Callback<List<Item>> callback) {
        backendService.searchItems(familyId, itemStatus, searchQuery).enqueue(callback);
    }
}

