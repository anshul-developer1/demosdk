package com.aeropaymerchantsdk.activity

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import com.aeropaymerchantsdk.R
import com.aeropaymerchantsdk.Utilities.ConstantsStrings
import com.aeropaymerchantsdk.Utilities.PrefKeeper
import com.aeropaymerchantsdk.communication.DefineID
import com.aeropaymerchantsdk.communication.ICommunicationHandler

var loader : Dialog? = null
lateinit var idToken : String

open class BaseActivity : AppCompatActivity() , ICommunicationHandler{

    private var alertDialog: Dialog? = null
    private var mToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }

    fun launchActivity(activityClass: Class<out BaseActivity>, intent : Intent? = null) {
        if(intent != null){
            startActivity(intent)
        }
        else {
            startActivity(Intent(this, activityClass))
        }
    }

    fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showProgressBar(msg: String, context : Context?) {
        if (alertDialog != null && alertDialog!!.isShowing && this.isFinishing) {
            return
        }
        alertDialog = Dialog(context!!)
        alertDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog!!.setCancelable(false)
        alertDialog!!.show()
    }

    fun hideProgressBar() {
        if (alertDialog != null && alertDialog!!.isShowing) {
            alertDialog!!.dismiss()
            alertDialog = null
        }
    }

    fun showMsgToast(@StringRes resId: Int) {
        if (mToast != null) {
            mToast?.cancel()
            mToast = null
        }
        mToast = Toast.makeText(this, resId, Toast.LENGTH_SHORT)
        mToast?.show()
    }

    fun showMsgToast(msg: String) {
        if (mToast != null) {
            mToast?.cancel()
            mToast = null
        }
        mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
        mToast?.show()
    }

    override fun onSuccess(outputParms: Int) {
        if(outputParms.equals(DefineID().FETCH_MERCHANT_PROFILE)){
            launchActivity(HomeActivity::class.java)
        }
        else if(outputParms.equals(DefineID().FETCH_MERCHANT_LOCATIONS)){
            launchActivity(SettingsScreenActivity::class.java)
        }
        else if(outputParms.equals(DefineID().FETCH_MERCHANT_DEVICES)){
            (this as SettingsScreenActivity).createDeviceSpinner()
        }
    }


    override fun onFailure(outputParms: Int) {
        if(outputParms.equals(DefineID().FETCH_MERCHANT_PROFILE)){

        }
        else if(outputParms.equals(DefineID().FETCH_MERCHANT_LOCATIONS)){
            showMsgToast("failure")
        }
        else if(outputParms.equals(DefineID().FETCH_MERCHANT_DEVICES)){
            showMsgToast("failure")
        }
    }

    override fun onBackPressed() {

    }
}
