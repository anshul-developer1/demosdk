package com.aeropaymerchantsdk.communication

import AP.AeroPayStagingClient
import AP.model.MerchantLocationDevices
import AP.model.MerchantLocationDevicesResponse
import AP.model.MerchantLocationsResponse
import AP.model.MerchantResponse
import android.content.Context
import android.os.AsyncTask
import com.aeropaymerchantsdk.Model.AeropayModelManager
import com.aeropaymerchantsdk.Model.FetchMerchantDevicesList
import com.aeropaymerchantsdk.Model.FetchMerchantLocationModel
import com.aeropaymerchantsdk.Model.FetchMerchantProfileModel
import com.aeropaymerchantsdk.Utilities.ConstantsStrings
import com.aeropaymerchantsdk.Utilities.GlobalMethods
import com.aeropaymerchantsdk.activity.idToken
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory
import com.amazonaws.regions.Regions
import com.google.gson.Gson
import android.content.pm.ActivityInfo
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.R.attr.orientation
import android.app.Activity
import android.content.res.Configuration


class AWSConnectionManager  {

    var mContext: Context? = null

    constructor(context: Context) {
        this.mContext = context
    }

    fun hitServer(requestID: Int, callbackHandler: ICommunicationHandler, requestObject : Any?) {
        object : AsyncTask<Void, Void, Any>() {

            override fun onPreExecute() {
                super.onPreExecute()
                lockScreenOrientation()
                GlobalMethods().showLoader(mContext!!)
            }

            override fun doInBackground(vararg p0: Void?): Any? {

                var credentialsProvider = object : CognitoCachingCredentialsProvider(mContext,
                    ConstantsStrings().aws_identitypool_id, Regions.US_EAST_1){}

                var logins = HashMap<String,String>()
                logins.put(ConstantsStrings().userPoolLoginType, idToken);
                credentialsProvider.setLogins(logins);

                var factory = object : ApiClientFactory(){}.credentialsProvider(credentialsProvider)

                var client = factory.build(AeroPayStagingClient::class.java)

                var output : Any? = null

                if(requestID.equals(DefineID().FETCH_MERCHANT_PROFILE)){
                    output = client.fetchMerchantPost()
                }
                else if(requestID.equals(DefineID().FETCH_MERCHANT_LOCATIONS)){
                    output = client.fetchMerchantLocationsPost()
                }
                else if(requestID.equals(DefineID().FETCH_MERCHANT_DEVICES)){
                    var merchantLocationDevice = requestObject as MerchantLocationDevices
                    output = client.fetchMerchantLocationDevicesPost(merchantLocationDevice)
                }

                return output as Any
            }

            override fun onPostExecute(result: Any?) {
                super.onPostExecute(result)
                GlobalMethods().dismissLoader()
                var objModelManager = AeropayModelManager().getInstance()
                if(requestID.equals(DefineID().FETCH_MERCHANT_PROFILE)){
                    var output = result as MerchantResponse
                    var statusCode = output.success.toString()
                    if(statusCode.equals("1")){
                        var stringOutput = Gson().toJson(output)
                        objModelManager.merchantProfileModel = Gson().fromJson(stringOutput,
                            FetchMerchantProfileModel::class.java)
                        callbackHandler.onSuccess(requestID)
                    }
                    else{
                        callbackHandler.onFailure(requestID)
                    }
                }
                else if(requestID.equals(DefineID().FETCH_MERCHANT_LOCATIONS)){
                    var output = result as MerchantLocationsResponse
                    var statusCode = output.success.toString()
                    if(statusCode.equals("1")){
                        var stringOutput = Gson().toJson(output)
                        objModelManager.merchantLocationsModel = Gson().fromJson(stringOutput,
                            FetchMerchantLocationModel::class.java)
                        callbackHandler.onSuccess(requestID)
                    }
                    else{
                        callbackHandler.onFailure(requestID)
                    }
                }
                else if(requestID.equals(DefineID().FETCH_MERCHANT_DEVICES)){
                    var output = result as MerchantLocationDevicesResponse
                    var statusCode = output.success.toString()
                    if(statusCode.equals("1")){
                        var stringOutput = Gson().toJson(output)
                        objModelManager.merchantDevicesModel = Gson().fromJson(stringOutput,
                            FetchMerchantDevicesList::class.java)
                        callbackHandler.onSuccess(requestID)
                    }
                    else{
                        callbackHandler.onFailure(requestID)
                    }
                }
                unlockScreenOrientation()
            }
        }.execute()}

    private fun lockScreenOrientation() {
        val currentOrientation = mContext!!.getResources().getConfiguration().orientation
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            (mContext as Activity).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        } else {
            (mContext as Activity).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        }
    }

    private fun unlockScreenOrientation() {
        (mContext as Activity).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR)
    }
}