package com.herocheer.zhsq.localservice.core.device.box.YTBox;

public interface YTBoxApi {

    String TOKEN_URL = "http://%s:%s/authentication/v1/token";

    String GET_BOX_ID = "http://%s:%s/ops_agent/v1/box/id";

    String SET_ONE_FACE = "http://%s:%s/face/v1/repositories/%s/faces/%s";

    String FACE_RESULT_PATH = "http://%s:%s/v1/face_results";

     String SCHEMA_HEADER = "x-kassq-schema";

    String STREAMING_CLIENT_PORT ="17332";

    String DEL_ONE_FACE = "http://%s:%s/face/v1/repositories/%s/faces/%s";
}
