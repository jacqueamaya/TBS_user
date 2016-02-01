package citu.teknoybuyandselluser.services;

import java.util.List;

import citu.teknoybuyandselluser.Constants;
import citu.teknoybuyandselluser.models.Notification;
import citu.teknoybuyandselluser.models.ResponseStatus;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by jack on 1/02/16.
 */
public interface TBSUserInterface {
    String BASE_URL = "http://tbs-admin.herokuapp.com/";

    @FormUrlEncoded
    @POST(Constants.UrlUser.URL_LOGIN)
    Call<ResponseStatus> login(@Field(Constants.User.USERNAME) String username, @Field(Constants.User.PASSWORD) String password);

    @GET(Constants.UrlUser.URL_NOTIFICATION)
    Call<List<Notification>> getNotifications();

    @FormUrlEncoded
    @POST(Constants.UrlUser.URL_BUY_ITEM)
    Call<ResponseStatus> buyItem(@Field(Constants.User.USERNAME) String username, @Field(Constants.User.PASSWORD) String password);
}
