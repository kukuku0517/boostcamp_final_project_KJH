package com.example.android.selfns.Helper;

import com.example.android.selfns.Data.DTO.Retrofit.FriendAddDTO;
import com.example.android.selfns.Data.DTO.Retrofit.FriendDTO;
import com.example.android.selfns.Data.DTO.Retrofit.PostIdDTO;
import com.example.android.selfns.Data.DTO.Retrofit.UrlDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.RetrofitShareableDTO;
import com.example.android.selfns.Data.DTO.Retrofit.UserDTO;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by samsung on 2017-08-22.
 */

public interface RetrofitService {
    @Headers({"Accept: application/json"})

    //1.데이터 저장(공유시) => 태그 TODO
    @POST("/users/{id}/post")
    Call<PostIdDTO> postDataToRetrofit(@Path("id") String userId, @Body RetrofitShareableDTO data);

    //2. 전체 데이터 불러오기 TODO
    @GET("/users/{id}/post")
    Call<List<RetrofitShareableDTO>> getDataFromRetrofit(@Path("id") String id, @Query("date") long millis);

    //3. 사용자 등록
    @POST("/users/new")
    Call<String> postUserRetrofit(@Body UserDTO user);

    //4. 전체 사용자 리스트(친구여부 판단)
    @GET("/users/{id}/others")
    Call<ArrayList<FriendDTO>> getUsersListRetrofit(@Path("id") String userId);

    //5. 친구 추가
    @POST("/users/{id}/friend")
    Call<String> postFriendRetrofit(@Path("id") String userId, @Body FriendAddDTO friend);

    //6. 친구 목록 가져오기(내친구) TODO Later
    @GET("/users/{id}/friends")
    Call<ArrayList<UserDTO>> getFriendsListRetrofit(@Path("id") String userId);

    //7. 친구 목록 가져오기(태그된사람 제외)
    @GET("/posts/{id}/others")
    Call<ArrayList<FriendDTO>> getTagFriendsListRetrofit(@Path("id") long postId, @Query("userId") String userId);

    //8. 친구 목록 가져오기(태그된 사람)
    @GET("/posts/{id}/tag")
    Call<ArrayList<FriendDTO>> getTaggedFriendsListRetrofit(@Path("id") long postId);

    //9. 친구 태그
    @POST("/posts/{id}/tag")
    Call<UrlDTO> postTagFriendRetrofit(@Path("id") long postId, @Body FriendAddDTO friendAddDTO);


    //    //10. 사진 올리기
//    @Multipart
//    @POST("/posts/{id}/photos")
//    Call<List<PostIdDTO>> postPhotosRetrofit(
//            @Path("id")long id,
//            @Part MultipartBody.Part[] photos);
//
//
    //10. 사진 하나씩 올리기
    @Multipart
    @POST("/posts/{id}/photoUpload")
    Call<UrlDTO> postPhotoRetrofit(
            @Path("id") long gid,
            @Part MultipartBody.Part photo);

    //11.PhotoDTO 올리기
    @POST("/posts/{id}/item")
    Call<PostIdDTO> postPhotoDTORetrofit(@Path("id") long gid, @Body RetrofitShareableDTO data);

    //12.PhotoDTO 요청하기
    @GET("posts/{id}/items")
    Call<List<RetrofitShareableDTO>> getItemsDTORetrofit(@Path("id") long gid);


    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(new OkHttpClient().newBuilder()
                    .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .addNetworkInterceptor(new StethoInterceptor()).build())

            .baseUrl("https://selfns-taejoonhong.c9users.io:8080/")
            .build();
}
