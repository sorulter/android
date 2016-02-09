package com.iproxier.ipx4andorid.api;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

public class IPXResetClient {
    private static final String BASE_URL="http://10.211.55.15/api/v1/";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static RequestHandle get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        return client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static RequestHandle post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        return client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private  static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
