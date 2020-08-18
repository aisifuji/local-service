package com.herocheer.zhsq.localservice.core.device.box.YTBox;


import com.herocheer.zhsq.localservice.core.device.box.YTBox.pojo.RetrivalFaceResult;
import com.herocheer.zhsq.localservice.core.device.entity.BaseDevice;

/**
 * Event management interface class
 */
public interface IEventManager {
    interface IFaceResultEventHandler
    {
        /**
         * captureFaceResult callback
         *
         * @param retrivalFaceResult
         */
        void onFaceResult(RetrivalFaceResult retrivalFaceResult, BaseDevice baseDevice);
    }

    /**
     * subscribeCaptureResultEvent
     *
     * @param handler
     */
    void subscribeFaceResultEvent(IFaceResultEventHandler handler);
}
