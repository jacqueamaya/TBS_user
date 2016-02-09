package citu.teknoybuyandselluser.services;

import java.util.List;

import citu.teknoybuyandselluser.Constants;
import citu.teknoybuyandselluser.models.AvailableDonation;
import citu.teknoybuyandselluser.models.AvailableItemForRent;
import citu.teknoybuyandselluser.models.AvailableItemToSell;
import citu.teknoybuyandselluser.models.DonateItem;
import citu.teknoybuyandselluser.models.Notification;
import citu.teknoybuyandselluser.models.PendingItem;
import citu.teknoybuyandselluser.models.RentItem;
import citu.teknoybuyandselluser.models.RentedItem;
import citu.teknoybuyandselluser.models.ReservedItemForRent;
import citu.teknoybuyandselluser.models.ReservedItemOnSale;
import citu.teknoybuyandselluser.models.ReservedItemToDonate;
import citu.teknoybuyandselluser.models.ResponseStatus;
import citu.teknoybuyandselluser.models.SellItem;
import citu.teknoybuyandselluser.models.UserProfile;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 ** Created by jack on 1/02/16.
 */
public interface TBSUserInterface {

    @FormUrlEncoded
    @POST(Constants.UrlUser.LOGIN)
    Call<ResponseStatus> login(@Field(Constants.User.USERNAME) String username, @Field(Constants.User.PASSWORD) String password);

    @FormUrlEncoded
    @POST(Constants.UrlUser.REGISTER)
    Call<ResponseStatus> register(@Field(Constants.User.ID_NUMBER) String id_number,
                                  @Field(Constants.User.FIRST_NAME) String first_name,
                                  @Field(Constants.User.LAST_NAME) String last_name,
                                  @Field(Constants.User.USERNAME) String username,
                                  @Field(Constants.User.PASSWORD) String password);

    @FormUrlEncoded
    @POST(Constants.UrlUser.EDIT_PROFILE)
    Call<ResponseStatus> editProfile(@Field(Constants.User.USERNAME) String username,
                                     @Field(Constants.User.NEW_USERNAME) String new_username,
                                     @Field(Constants.User.OLD_PASSWORD) String old_password,
                                     @Field(Constants.User.NEW_PASSWORD) String new_password,
                                     @Field(Constants.User.CONFIRM_PASSWORD) String confirm_password,
                                     @Field(Constants.User.PICTURE) String picture);

    @FormUrlEncoded
    @POST(Constants.UrlUser.SELL_ITEM)
    Call<ResponseStatus> sellItem(@Field(Constants.Item.OWNER) String owner,
                                  @Field(Constants.Item.NAME) String name,
                                  @Field(Constants.Item.DESCRIPTION) String description,
                                  @Field(Constants.Item.PRICE) String price,
                                  @Field(Constants.Item.QUANTITY) String quantity,
                                  @Field(Constants.Item.IMAGE_URL) String url);

    @FormUrlEncoded
    @POST(Constants.UrlUser.BUY_ITEM)
    Call<ResponseStatus> buyItem(@Field(Constants.Item.BUYER) String buyer,
                                  @Field(Constants.Item.ID) String item_id,
                                  @Field(Constants.Item.QUANTITY) String quantity,
                                  @Field(Constants.Item.STARS_TO_USE) String stars_to_use);

    @FormUrlEncoded
    @POST(Constants.UrlUser.FOR_RENT_ITEM)
    Call<ResponseStatus> forRentItem(@Field(Constants.Item.OWNER) String owner,
                                  @Field(Constants.Item.NAME) String name,
                                  @Field(Constants.Item.DESCRIPTION) String description,
                                  @Field(Constants.Item.PRICE) String price,
                                  @Field(Constants.Item.QUANTITY) String quantity,
                                  @Field(Constants.Item.IMAGE_URL) String url);

    @FormUrlEncoded
    @POST(Constants.UrlUser.RENT_ITEM)
    Call<ResponseStatus> rentItem(@Field(Constants.Item.RENTER) String renter,
                                 @Field(Constants.Item.ID) String item_id,
                                 @Field(Constants.Item.QUANTITY) String quantity);

    @FormUrlEncoded
    @POST(Constants.UrlUser.DONATE_ITEM)
    Call<ResponseStatus> donateItem(@Field(Constants.Item.OWNER) String owner,
                                     @Field(Constants.Item.NAME) String name,
                                     @Field(Constants.Item.DESCRIPTION) String description,
                                     @Field(Constants.Item.QUANTITY) String quantity,
                                     @Field(Constants.Item.IMAGE_URL) String url);

    @FormUrlEncoded
    @POST(Constants.UrlUser.GET_DONATED_ITEM)
    Call<ResponseStatus> getDonatedItem(@Field(Constants.Item.BUYER) String buyer,
                                  @Field(Constants.Item.ID) String item_id,
                                  @Field(Constants.Item.QUANTITY) String quantity);

    @FormUrlEncoded
    @POST(Constants.UrlUser.EDIT_ITEM)
    Call<ResponseStatus> editItem(@Field(Constants.Item.OWNER) String owner,
                                  @Field(Constants.Item.ID) String item_id,
                                  @Field(Constants.Item.NAME) String name,
                                  @Field(Constants.Item.DESCRIPTION) String description,
                                  @Field(Constants.Item.PRICE) String price,
                                  @Field(Constants.Item.QUANTITY) String quantity,
                                  @Field(Constants.Item.IMAGE_URL) String url);

    @FormUrlEncoded
    @POST(Constants.UrlUser.DELETE_ITEM)
    Call<ResponseStatus> deleteItem(@Field(Constants.Item.OWNER) String owner,
                                    @Field(Constants.Item.ID) String item_id);

    @FormUrlEncoded
    @POST(Constants.UrlUser.CANCEL_RESERVED_ITEM)
    Call<ResponseStatus> cancelReservedItem(@Field(Constants.Item.BUYER) String buyer,
                                  @Field(Constants.Item.ID) String item_id,
                                  @Field(Constants.Item.RESERVATION_ID) String reservation_id);

    @POST(Constants.UrlUser.CHECK_EXPIRATION)
    Call<ResponseStatus> checkExpiration();

    @GET(Constants.UrlUser.PROFILE)
    Call<List<UserProfile>> getUser(@Query(Constants.User.USERNAME) String username);

    @GET(Constants.UrlUser.NOTIFICATION)
    Call<List<Notification>> getNotifications(@Query(Constants.User.USERNAME) String username);

    @GET(Constants.UrlUser.PENDING_ITEMS)
    Call<List<PendingItem>> getPendingItems(@Query(Constants.User.USERNAME) String username);

    @GET(Constants.UrlUser.ITEMS_TO_SELL)
    Call<List<SellItem>> getItemsToSell(@Query(Constants.User.USERNAME) String username);

    @GET(Constants.UrlUser.ITEMS_FOR_RENT)
    Call<List<RentItem>> getItemsForRent(@Query(Constants.User.USERNAME) String username);

    @GET(Constants.UrlUser.ITEMS_TO_DONATE)
    Call<List<DonateItem>> getItemsToDonate(@Query(Constants.User.USERNAME) String username);

    @GET(Constants.UrlUser.RENTED_ITEMS)
    Call<List<RentedItem>> getRentedItems(@Query(Constants.User.USERNAME) String username);

    @GET(Constants.UrlUser.RESERVED_ITEMS_ON_SALE)
    Call<List<ReservedItemOnSale>> getReservedItemsOnSale(@Query(Constants.User.USERNAME) String username);

    @GET(Constants.UrlUser.RESERVED_ITEMS_FOR_RENT)
    Call<List<ReservedItemForRent>> getReservedItemsForRent(@Query(Constants.User.USERNAME) String username);

    @GET(Constants.UrlUser.RESERVED_ITEMS_FOR_DONATION)
    Call<List<ReservedItemToDonate>> getReservedItemsForDonation(@Query(Constants.User.USERNAME) String username);

    @GET(Constants.UrlUser.AVAILABLE_ITEMS_TO_SELL)
    Call<List<AvailableItemToSell>> getAvailableItemsToSell(@Query(Constants.User.USERNAME) String username);

    @GET(Constants.UrlUser.AVAILABLE_ITEMS_FOR_RENT)
    Call<List<AvailableItemForRent>> getAvailableItemsForRent(@Query(Constants.User.USERNAME) String username);

    @GET(Constants.UrlUser.ALL_DONATIONS)
    Call<List<AvailableDonation>> getAllDonations(@Query(Constants.User.USERNAME) String username);
}
