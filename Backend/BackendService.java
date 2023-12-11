package com.example.ckmj_se_project.ui.Backend;

import com.example.ckmj_se_project.ui.Family.Family;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BackendService {

    @GET("items/{familyId}/{itemStatus}")
    Call<List<Item>> getItemsByFamilyAndStatus(@Path("familyId") int familyId, @Path("itemStatus") String itemStatus);

    @GET("families/{email}/{name}")
    Call<Family> getFamilyByEmailAndName(@Path("email") String email, @Path("name") String name);

    @GET("family/{familyId}")
    Call<Family> getFamilyByFamilyId(@Path("familyId") int familyId);

    @POST("families")
    Call<Family> postFamily(@Body Family family);

    @POST("items")
    Call<Item> postItem(@Body Item item);

    @DELETE("items/{itemId}")
    Call<Void> deleteItem(@Path("itemId") int itemId);

    @PUT("item/{itemId}/have")
    Call<Void> updateItemToHave(@Path("itemId") int itemId);

    @PUT("item/{itemId}/need")
    Call<Void> updateItemToNeed(@Path("itemId") int itemId);

    @GET("items/search")
    Call<List<Item>> searchItems(@Query("familyId") int familyId,
                                 @Query("itemStatus") String itemStatus,
                                 @Query("searchQuery") String searchQuery);

}


