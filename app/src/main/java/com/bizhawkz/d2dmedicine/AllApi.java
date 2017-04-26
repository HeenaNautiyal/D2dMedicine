package com.bizhawkz.d2dmedicine;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Heena on 3/30/2017.
 */
public interface AllApi {
    @FormUrlEncoded
    @POST("insertdata.php")
    public Call<ResponseBody> loginUser(@Field("caseid") String caseid,
                                        @Field("user_session") String cat1,
                                        @Field("medicine_id") String subname,
                                        @Field("cart_seesion") String med,
                                        @Field("uadd") String address);



}
