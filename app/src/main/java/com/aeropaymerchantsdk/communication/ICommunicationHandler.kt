package com.aeropaymerchantsdk.communication

interface ICommunicationHandler {

    fun onSuccess(outputParms: Int)

    fun onFailure(outputParms: Int)

}