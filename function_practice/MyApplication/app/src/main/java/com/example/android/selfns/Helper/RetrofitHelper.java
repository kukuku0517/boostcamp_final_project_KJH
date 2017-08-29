package com.example.android.selfns.Helper;

import android.app.DialogFragment;
import android.content.Context;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.selfns.Data.DTO.Detail.CustomDTO;
import com.example.android.selfns.Data.DTO.Detail.GpsDTO;
import com.example.android.selfns.Data.DTO.Detail.PhotoDTO;
import com.example.android.selfns.Data.DTO.Detail.SmsTradeDTO;
import com.example.android.selfns.Data.DTO.Group.GpsGroupDTO;
import com.example.android.selfns.Data.DTO.Group.PhotoGroupDTO;
import com.example.android.selfns.Data.DTO.Retrofit.FriendAddDTO;
import com.example.android.selfns.Data.DTO.Retrofit.FriendDTO;
import com.example.android.selfns.Data.DTO.Retrofit.PostIdDTO;
import com.example.android.selfns.Data.DTO.Retrofit.UrlDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.RetrofitShareableDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.ShareableDTO;
import com.example.android.selfns.Data.RealmData.GroupData.GpsGroupData;
import com.example.android.selfns.Data.RealmData.GroupData.PhotoGroupData;
import com.example.android.selfns.Data.RealmData.UnitData.CustomData;
import com.example.android.selfns.Data.RealmData.UnitData.GpsData;
import com.example.android.selfns.Data.RealmData.UnitData.PhotoData;
import com.example.android.selfns.Data.RealmData.UnitData.SmsTradeData;
import com.example.android.selfns.Data.DTO.Retrofit.UserDTO;
import com.example.android.selfns.Interface.DataReceiveListener;
import com.example.android.selfns.R;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.Path;

import static android.R.attr.id;

/**
 * Created by samsung on 2017-08-22.
 */

public class RetrofitHelper {
    private static RetrofitHelper instance;
    private static Context context;

    public RetrofitHelper(Context context) {
        this.context = context;
    }

    public static RetrofitHelper getInstance(Context context) {
        if (instance == null) {
            instance = new RetrofitHelper(context);
        }
        return instance;
    }

    //1.
    public void shareData(final ShareableDTO item, String friendId) {

        RetrofitShareableDTO rItem;
        Gson gson = new Gson();
        String json = gson.toJson(item);
        rItem = gson.fromJson(json, RetrofitShareableDTO.class);

        final RetrofitService retrofitService = RetrofitService.retrofit.create(RetrofitService.class);
        retrofitService.postDataToRetrofit(FirebaseHelper.getInstance(context).getCurrentUser().getEmail(), rItem).enqueue(new Callback<PostIdDTO>() {
            @Override
            public void onResponse(Call<PostIdDTO> call, final Response<PostIdDTO> response) {
                Log.d("retrofit", "savedata");
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        switch (item.getType()) {
                            case RealmClassHelper.CUSTOM_DATA:
                                CustomData customData = new CustomData((CustomDTO) item);
                                customData.set_id(response.body().getId());
                                realm.insertOrUpdate(customData);
                                break;
                            case RealmClassHelper.PHOTO_GROUP_DATA:
                                PhotoGroupData photoGroupData = new PhotoGroupData((PhotoGroupDTO) item);
                                photoGroupData.set_id(response.body().getId());
                                realm.insertOrUpdate(photoGroupData);
                                Log.d("retrofitPhoto", "groupdata");
                                for (PhotoDTO photoData : ((PhotoGroupDTO) item).getPhotoss()) {
                                    Log.d("retrofitPhoto", "single photo start");
                                    sharePhotoDTO(response.body().getId(), photoData);
                                }
                                break;
                            case RealmClassHelper.GPS_GROUP_DATA:
                                GpsGroupData gpsGroupData = new GpsGroupData((GpsGroupDTO) item);
                                gpsGroupData.set_id(response.body().getId());
                                realm.insertOrUpdate(gpsGroupData);
                                for (GpsDTO photoData : ((GpsGroupDTO) item).getGpsDatas()) {
                                    Log.d("retrofitPhoto", "single photo start");
                                    shareGpsDTO(response.body().getId(), photoData);
                                }
                                break;
                            case RealmClassHelper.SMS_TRADE_DATA:
                                SmsTradeData smsTradeData = new SmsTradeData((SmsTradeDTO) item);
                                smsTradeData.set_id(response.body().getId());
                                realm.insertOrUpdate(smsTradeData);
                                break;
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<PostIdDTO> call, Throwable t) {

            }
        });
    }
    public int nextId(Class c, Realm realm) {

        Number currentIdNum = realm.where(c).max("id");
        int nextId;
        if (currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        return nextId;
    }


    //2.
    public void getData(long millis) throws IOException {
        final Realm realm = Realm.getDefaultInstance();
        RetrofitService retrofitService = RetrofitService.retrofit.create(RetrofitService.class);
        String userId = FirebaseHelper.getInstance(context).getCurrentUserId();
        Call<List<RetrofitShareableDTO>> call = retrofitService.getDataFromRetrofit(userId, millis);
        call.enqueue(new Callback<List<RetrofitShareableDTO>>() {
            @Override
            public void onResponse(Call<List<RetrofitShareableDTO>> call, final Response<List<RetrofitShareableDTO>> response) {
                if (response.body() != null) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(final Realm realm) {
                            for (RetrofitShareableDTO data : response.body()) {
                                int type = data.getType();
                                long _id = data.get_id();
                                long id = data.getId();

                                Gson gson = new Gson();
                                String json = gson.toJson(data);
                                switch (type) {
                                    case RealmClassHelper.CUSTOM_DATA:
                                        CustomData customData = gson.fromJson(json, CustomData.class);
                                        CustomData cd = realm.where(CustomData.class).equalTo("_id", _id).findFirst();
                                        if (cd == null) {
                                            cd = customData;
                                            cd.setId(RealmHelper.getInstance().nextId(CustomData.class, realm));
                                        } else if (cd.getId() != id) {
                                            long myId = cd.getId();
                                            cd = customData;
                                            cd.setId(myId);
                                        } else {
                                            cd = customData;
                                        }
                                        realm.insertOrUpdate(cd);
                                        break;
                                    case RealmClassHelper.PHOTO_GROUP_DATA:
                                        PhotoGroupData photoGroupData = gson.fromJson(json, PhotoGroupData.class);

                                        final PhotoGroupData pgd[] = new PhotoGroupData[1];
                                        pgd[0] = realm.where(PhotoGroupData.class).equalTo("_id", _id).findFirst();
                                        if (pgd[0] == null) {
                                            pgd[0] = photoGroupData;
                                            pgd[0].setId(RealmHelper.getInstance().nextId(PhotoGroupData.class, realm));
                                        } else if (pgd[0].getId() != id) {
                                            long myId = pgd[0].getId();
                                            pgd[0] = photoGroupData;
                                            pgd[0].setId(myId);
                                        } else {
                                            pgd[0] = photoGroupData;
                                        }

                                        getPhotoGpsDTO(_id, new DataReceiveListener<List<RetrofitShareableDTO>>() {
                                            @Override
                                            public void onReceive(final List<RetrofitShareableDTO> response) {
                                                Realm realm = Realm.getDefaultInstance();
                                                realm.executeTransaction(new Realm.Transaction() {
                                                    @Override
                                                    public void execute(Realm realm) {
                                                        for (RetrofitShareableDTO data : response) {
                                                            int type = data.getType();
                                                            Gson gson = new Gson();
                                                            String json = gson.toJson(data);
                                                            long _id = data.get_id();
                                                            long id = data.getId();
                                                            switch (type) {
                                                                case RealmClassHelper.PHOTO_DATA:
                                                                    PhotoData photoData = gson.fromJson(json, PhotoData.class);
                                                                    PhotoData pd = realm.where(PhotoData.class).equalTo("_id", _id).findFirst();
                                                                    if (pd == null) {
                                                                        pd = photoData;
                                                                        pd.setId(RealmHelper.getInstance().nextId(PhotoData.class, realm));
                                                                    } else if (pd.getId() != id) {
                                                                        long myId = pd.getId();
                                                                        pd = photoData;
                                                                        pd.setId(myId);
                                                                    } else {
                                                                        pd = photoData;
                                                                    }

                                                                    final PhotoData finalPd = pd;
                                                                    realm.insertOrUpdate(finalPd);
                                                                    pgd[0].getPhotoss().add(finalPd);

                                                                    break;
                                                            }
                                                        }
                                                        realm.insertOrUpdate(pgd[0]);
                                                    }
                                                });


                                            }
                                        });

                                        break;
                                    case RealmClassHelper.GPS_GROUP_DATA:
                                        GpsGroupData gpsGroupData = gson.fromJson(json, GpsGroupData.class);
                                        final GpsGroupData ggd[] = new GpsGroupData[1];
                                        ggd[0] = realm.where(GpsGroupData.class).equalTo("_id", _id).findFirst();
                                        if (ggd[0] == null) {
                                            ggd[0] = gpsGroupData;
                                            ggd[0].setId(RealmHelper.getInstance().nextId(GpsGroupData.class, realm));
                                        } else if (ggd[0].getId() != id) {
                                            long myId = ggd[0].getId();
                                            ggd[0] = gpsGroupData;
                                            ggd[0].setId(myId);
                                        } else {
                                            ggd[0] = gpsGroupData;
                                        }

                                        getPhotoGpsDTO(_id, new DataReceiveListener<List<RetrofitShareableDTO>>() {
                                            @Override
                                            public void onReceive(final List<RetrofitShareableDTO> response) {
                                                Realm realm = Realm.getDefaultInstance();
                                                realm.executeTransaction(new Realm.Transaction() {
                                                    @Override
                                                    public void execute(Realm realm) {
                                                        for (RetrofitShareableDTO data : response) {
                                                            int type = data.getType();
                                                            Gson gson = new Gson();
                                                            String json = gson.toJson(data);
                                                            long _id = data.get_id();
                                                            long id = data.getId();
                                                            switch (type) {
                                                                case RealmClassHelper.GPS_DATA:
                                                                    GpsData gpsData = gson.fromJson(json, GpsData.class);
                                                                    GpsData gd = realm.where(GpsData.class).equalTo("_id", _id).findFirst();
                                                                    if (gd == null) {
                                                                        gd = gpsData;
                                                                        gd.setId(RealmHelper.getInstance().nextId(GpsData.class, realm));
                                                                    } else if (gd.getId() != id) {
                                                                        long myId = gd.getId();
                                                                        gd = gpsData;
                                                                        gd.setId(myId);
                                                                    } else {
                                                                        gd = gpsData;
                                                                    }
                                                                    final GpsData finalGd = gd;

                                                                    realm.insertOrUpdate(finalGd);
                                                                    ggd[0].getGpsDatas().add(finalGd);
                                                                    break;
                                                            }
                                                        }
                                                        realm.insertOrUpdate(ggd[0]);
                                                    }
                                                });
                                            }
                                        });

                                        break;
                                    case RealmClassHelper.SMS_TRADE_DATA:
                                        SmsTradeData smsTradeData = gson.fromJson(json, SmsTradeData.class);
                                        SmsTradeData std = realm.where(SmsTradeData.class).equalTo("_id", _id).findFirst();
                                        if (std == null) { //내꺼아님 처음저장
                                            std = smsTradeData;
                                            std.setId(RealmHelper.getInstance().nextId(SmsTradeData.class, realm));
                                        } else if (std.getId() != id) { //내꺼아님 두번저장
                                            long myId = std.getId();
                                            std = smsTradeData;
                                            std.setId(myId);
                                        } else {//내꺼임
                                            std = smsTradeData;
                                        }
                                        realm.insertOrUpdate(std);
                                        break;
                                }
                            }
                        }

                    });
                }
            }

            @Override
            public void onFailure(Call<List<RetrofitShareableDTO>> call, Throwable t) {

            }
        });

    }

    //3.
    public void saveUser(UserDTO item) {
        RetrofitService retrofitService = RetrofitService.retrofit.create(RetrofitService.class);
        retrofitService.postUserRetrofit(item).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("retrofit", "saveuser");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    /***********************************************************************************************/

    //4.
    public void getUsers(String userId, final DataReceiveListener<ArrayList<FriendDTO>> listener) {

        RetrofitService retrofitService = RetrofitService.retrofit.create(RetrofitService.class);
        retrofitService.getUsersListRetrofit(userId).enqueue(new Callback<ArrayList<FriendDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<FriendDTO>> call, Response<ArrayList<FriendDTO>> response) {
                listener.onReceive(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<FriendDTO>> call, Throwable t) {

            }
        });
    }

    //5.
    public void addFriend(String userId, String fid) {
        RetrofitService retrofitService = RetrofitService.retrofit.create(RetrofitService.class);
        FriendAddDTO friendAddDTO = new FriendAddDTO();
        friendAddDTO.setId(fid);
        retrofitService.postFriendRetrofit(userId, friendAddDTO).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    //6.
    public void getFriends(String userId, final DataReceiveListener<ArrayList<UserDTO>> listener) {
        RetrofitService retrofitService = RetrofitService.retrofit.create(RetrofitService.class);
        retrofitService.getFriendsListRetrofit(userId).enqueue(new Callback<ArrayList<UserDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<UserDTO>> call, Response<ArrayList<UserDTO>> response) {
                listener.onReceive(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<UserDTO>> call, Throwable t) {

            }
        });
    }

    //7.
    public void getTagFriends(long postId, String userId, final DataReceiveListener<ArrayList<FriendDTO>> listener) {
        RetrofitService retrofitService = RetrofitService.retrofit.create(RetrofitService.class);
        retrofitService.getTagFriendsListRetrofit(postId, userId).enqueue(new Callback<ArrayList<FriendDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<FriendDTO>> call, Response<ArrayList<FriendDTO>> response) {
                listener.onReceive(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<FriendDTO>> call, Throwable t) {

            }
        });
    }

    //8.
    public void getTaggedFriends(long postId, final DataReceiveListener<ArrayList<FriendDTO>> listener) {
        RetrofitService retrofitService = RetrofitService.retrofit.create(RetrofitService.class);
        retrofitService.getTaggedFriendsListRetrofit(postId).enqueue(new Callback<ArrayList<FriendDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<FriendDTO>> call, Response<ArrayList<FriendDTO>> response) {
                listener.onReceive(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<FriendDTO>> call, Throwable t) {

            }
        });
    }

    //9.
    public void tagFriend(long postId, FriendAddDTO friendAddDTO) {
        RetrofitService retrofitService = RetrofitService.retrofit.create(RetrofitService.class);
        retrofitService.postTagFriendRetrofit(postId, friendAddDTO).enqueue(new Callback<UrlDTO>() {
            @Override
            public void onResponse(Call<UrlDTO> call, Response<UrlDTO> response) {
                EventBus.getDefault().post(new FriendTagEvent());
            }

            @Override
            public void onFailure(Call<UrlDTO> call, Throwable t) {

            }
        });
    }

    public void sharePhotoDTO(long gid, final PhotoDTO data) {

        RetrofitShareableDTO rItem;

        Gson gson = new Gson();
        String json = gson.toJson(data);
        rItem = gson.fromJson(json, RetrofitShareableDTO.class);
        rItem.setShare(1);

        RetrofitService retrofitService = RetrofitService.retrofit.create(RetrofitService.class);
        retrofitService.postPhotoDTORetrofit(gid, rItem).enqueue(new Callback<PostIdDTO>() {
            @Override
            public void onResponse(Call<PostIdDTO> call, Response<PostIdDTO> response) {
                Log.d("retrofitPhoto", "single photo real photo");
                uploadPhoto(response.body().getId(), data);
            }

            @Override
            public void onFailure(Call<PostIdDTO> call, Throwable t) {

            }
        });
    }

    public void shareGpsDTO(long gid, final GpsDTO data) {

        RetrofitShareableDTO rItem;
        Gson gson = new Gson();
        String json = gson.toJson(data);
        rItem = gson.fromJson(json, RetrofitShareableDTO.class);
        rItem.setShare(1);


        RetrofitService retrofitService = RetrofitService.retrofit.create(RetrofitService.class);
        retrofitService.postPhotoDTORetrofit(gid, rItem).enqueue(new Callback<PostIdDTO>() {
            @Override
            public void onResponse(Call<PostIdDTO> call, Response<PostIdDTO> response) {

            }

            @Override
            public void onFailure(Call<PostIdDTO> call, Throwable t) {

            }
        });
    }


    public void getPhotoGpsDTO(long gid, final DataReceiveListener<List<RetrofitShareableDTO>> listener) {

        RetrofitService retrofitService = RetrofitService.retrofit.create(RetrofitService.class);
        retrofitService.getItemsDTORetrofit(gid).enqueue(new Callback<List<RetrofitShareableDTO>>() {
            @Override
            public void onResponse(Call<List<RetrofitShareableDTO>> call, final Response<List<RetrofitShareableDTO>> response) {
                listener.onReceive(response.body());

            }

            @Override
            public void onFailure(Call<List<RetrofitShareableDTO>> call, Throwable t) {

            }
        });
    }

    public void uploadPhoto(long pid, final PhotoDTO photoDTO) {


        File file = new File(photoDTO.getPath());
        RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("photos", file.getName(), surveyBody);
        RetrofitService retrofitService = RetrofitService.retrofit.create(RetrofitService.class);

        retrofitService.postPhotoRetrofit(pid, part).enqueue(new Callback<UrlDTO>() {
            @Override
            public void onResponse(Call<UrlDTO> call, final Response<UrlDTO> response) {
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Log.d("retrofitPhoto", "single photo complete");
                        PhotoData photoData = realm.where(PhotoData.class).equalTo("_id", photoDTO.get_id()).findFirst();
                        Log.d("retrofitPhoto", response.toString());
                        photoData.setPath(response.body().getUrl());
                    }
                });
            }

            @Override
            public void onFailure(Call<UrlDTO> call, Throwable t) {

            }
        });
    }

//    public void uploadPhoto(long postId, PhotoGroupDTO data) {
//        final ArrayList<PhotoDTO> photos = data.getPhotoss();
//
//        MultipartBody.Part[] photosParts = new MultipartBody.Part[photos.size()];
//
//        for (int index = 0; index < photos.size(); index++) {
//
//            File file = new File(photos.get(index).getPath());
//            RequestBody surveyBody = RequestBody.create(MediaType.parse("image/*"), file);
//            photosParts[index] = MultipartBody.Part.createFormData("photos", file.getName(), surveyBody);
//        }
//
//        RetrofitService retrofitService = RetrofitService.retrofit.create(RetrofitService.class);
//        retrofitService.postPhotosRetrofit(postId, photosParts).enqueue(new Callback<List<PostIdDTO>>() {
//            @Override
//            public void onResponse(Call<List<PostIdDTO>> call, final Response<List<PostIdDTO>> response) {
//                Realm realm = Realm.getDefaultInstance();
//                realm.executeTransaction(new Realm.Transaction() {
//                    @Override
//                    public void execute(Realm realm) {
//
//                        for (int i = 0; i < response.body().size(); i++) {
//                            PhotoData photoData = new PhotoData(photos.get(i));
//                            photoData.set_id(response.body().get(i).getId());
//                            realm.insertOrUpdate(photoData);
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(Call<List<PostIdDTO>> call, Throwable t) {
//
//            }
//        });
//    }


}
