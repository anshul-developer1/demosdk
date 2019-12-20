
package com.aeropay_merchant.Model;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class RegisterMerchantDeviceResponse {

    @SerializedName("majorID")
    private Object mMajorID;
    @SerializedName("minorID")
    private Object mMinorID;
    @SerializedName("success")
    private Object mSuccess;
    @SerializedName("UUID")
    private Object mUUID;

    public Object getMajorID() {
        return mMajorID;
    }

    public void setMajorID(Object majorID) {
        mMajorID = majorID;
    }

    public Object getMinorID() {
        return mMinorID;
    }

    public void setMinorID(Object minorID) {
        mMinorID = minorID;
    }

    public Object getSuccess() {
        return mSuccess;
    }

    public void setSuccess(Object success) {
        mSuccess = success;
    }

    public Object getUUID() {
        return mUUID;
    }

    public void setUUID(Object uUID) {
        mUUID = uUID;
    }

}
