
package com.herocheer.zhsq.localservice.core.device.box.YTBox;

import com.herocheer.zhsq.localservice.core.device.entity.BaseDevice;
import okhttp3.OkHttpClient;
import okio.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * FaceResultExecutor
 * 控制数据处理任务的起停，并作为FaceResultRunnable的生产者
 */
public class FaceResultExecutor {
    private static final Logger mLogger = LoggerFactory.getLogger(FaceResultExecutor.class);
    private FaceResultRunnable mFaceResultRunnable;
    private FaceResultRunnable.FaceResultThread mFaceResultThread;
    private BlockingQueue<Buffer> mFaceResultQueue = new LinkedBlockingQueue<Buffer>(100000);

    public FaceResultExecutor(IEventManager.IFaceResultEventHandler handler, BaseDevice baseDevice, String schema, OkHttpClient httpClient) {
        mFaceResultRunnable = new FaceResultRunnable(handler, schema, httpClient);
        mFaceResultThread = mFaceResultRunnable.new FaceResultThread(mFaceResultQueue,baseDevice);
        mFaceResultThread.start();
        mLogger.info("FaceResultThread start, host:{}", baseDevice.getIp());
    }

    public void terminate(HttpEventType type) throws UnsupportedOperationException {
        switch (type) {
            case FACE_RESULT:
                if (mFaceResultThread != null) {
                    mFaceResultThread.terminate();
                }
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    public void processData(Buffer data) throws Exception {
        mFaceResultQueue.put(data);
    }
}
