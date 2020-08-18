package com.herocheer.zhsq.localservice.core.device.box.YTBox;

import com.herocheer.zhsq.localservice.core.device.box.YTBox.pojo.RetrivalFaceResult;
import com.herocheer.zhsq.localservice.core.device.entity.BaseDevice;
import com.herocheer.zhsq.localservice.core.util.AvroUtil;
import okhttp3.OkHttpClient;
import okio.Buffer;
import org.apache.avro.AvroRuntimeException;
import org.apache.avro.generic.GenericRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * FaceResultRunnable
 * 数据处理任务，读取和拼接服务器的流式Response
 * FaceResultExecutor的消费者
 */
public class FaceResultRunnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(FaceResultRunnable.class);
    private IEventManager.IFaceResultEventHandler mFaceResultEventHandler;
    private final int LENGTH_BYTE = 4; // 4 bytes to restore data length
    private String mSchemaString;
    private Buffer mFaceResultBytes = new Buffer(); // Cumulative frame
    private OkHttpClient mHttpClient;

    private static final Logger logger = LoggerFactory.getLogger(FaceResultRunnable.class);


    /**
     * 初始化
     * @param handler FaceResult处理回调
     * @param schema 数据的Write Schema，可用于反序列化数据
     * @param httpClient
     */
    public FaceResultRunnable(IEventManager.IFaceResultEventHandler handler, String schema, OkHttpClient httpClient) {
        this.mFaceResultEventHandler = handler;
        this.mSchemaString = schema;
        this.mHttpClient = httpClient;
    }

    public class FaceResultThread extends Thread {
        BlockingQueue<Buffer> mFaceResultQueue;
        private volatile boolean running = true;
        private BaseDevice baseDevice;

        public void terminate() {
            running = false;
        }

        public FaceResultThread(BlockingQueue<Buffer> FaceResultQueue,BaseDevice baseDevice) {
            this.mFaceResultQueue = FaceResultQueue;
            this.baseDevice = baseDevice;
        }
        @Override
        public void run() {
            consumer();
        }
        private void consumer() {
            int size = 0;
            Buffer data;
            while (running) {
                try {
                    if ( mFaceResultQueue == null || mFaceResultQueue.size() == 0) {
                        continue;
                    }
                    // 读取一帧数据
                    data = mFaceResultQueue.take();
                    long dataSize = data.size();
                    if (mFaceResultBytes != null && dataSize > 0) {
                        data.read(mFaceResultBytes, dataSize);
                    }
                    /**
                     * 每个包的前4个byte标识这个包的大小，大端表示
                     */
                    if (size == 0 && mFaceResultBytes.size() >= LENGTH_BYTE) {
                        size = mFaceResultBytes.readInt();
                    }

                    long bufferSize = mFaceResultBytes.size();
                    if (size > 0 && bufferSize >= size) {
                        Buffer decodeData = new Buffer();
                        /**
                         * 从Buffer中读取一个完整的包
                         */
                        mFaceResultBytes.read(decodeData, size);
                        size = 0;
                        // 反序列化
                        RetrivalFaceResult retrivalFaceResult = getFaceResult(decodeData.readByteArray());

                        if (retrivalFaceResult != null) {
                            // 通过回调传出
                            mFaceResultEventHandler.onFaceResult(retrivalFaceResult,baseDevice);
                        }
                    }
                } catch (AvroRuntimeException e) {
                    LOGGER.error("AvroRuntimeException, cause: {}", e);
                } catch (Exception e) {
                    LOGGER.error("Exception, cause: {}", e);
//                    running = false;
//                    mHttpClient.dispatcher().executorService().shutdown();
                }
            }
            LOGGER.info("FaceResultThread stopped!");
        }
    }

    /**
     * 反序列化结果
     */
    public RetrivalFaceResult getFaceResult(byte[] data) throws Exception {
        RetrivalFaceResult retrievalFaceResult = new RetrivalFaceResult();
        List<GenericRecord> records = AvroUtil.decode(RetrivalFaceResult.getClassSchema(), data);

        if (records != null && records.size() > 0) {
            GenericRecord genericRecord = records.get(0);
            retrievalFaceResult.put(0, genericRecord.get("face"));
            retrievalFaceResult.put(1, genericRecord.get("retrieval_results"));
            retrievalFaceResult.put(2, genericRecord.get("result_type"));
        }
        return retrievalFaceResult;
    }
}
