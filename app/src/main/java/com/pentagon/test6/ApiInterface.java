package com.pentagon.test6;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("OCR")
    Call<String> getResult();
}
