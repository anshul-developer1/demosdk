package com.aeropaymerchantsdk.Model

class AeropayModelManager {

    companion object modelManager {
        var objModelManager: AeropayModelManager? = null
    }

    var merchantLocationsModel: FetchMerchantLocationModel = FetchMerchantLocationModel()
    var merchantDevicesModel: FetchMerchantDevicesList = FetchMerchantDevicesList()
    var merchantProfileModel: FetchMerchantProfileModel = FetchMerchantProfileModel()

    constructor() {}

    fun getInstance(): AeropayModelManager {

        if (objModelManager != null) {
            return objModelManager as AeropayModelManager

        } else {
            objModelManager = AeropayModelManager()
            return objModelManager as AeropayModelManager
        }
    }

}