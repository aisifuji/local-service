package com.herocheer.zhsq.localservice.core.util;


import okhttp3.OkHttpClient;
import okhttp3.Protocol;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class HttpClientUtils {

    public static OkHttpClient getOkHttpClient() {
        try {
            return new OkHttpClient.Builder()
                    .protocols(Collections.singletonList(Protocol.H2_PRIOR_KNOWLEDGE))
                    .retryOnConnectionFailure(true)
                    .readTimeout(0, TimeUnit.SECONDS)
                    .pingInterval(10, TimeUnit.SECONDS)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
