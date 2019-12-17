package com.aeropaymerchantsdk.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.aeropaymerchantsdk.R
import com.aeropaymerchantsdk.Utilities.*
import com.aeropaymerchantsdk.communication.AWSConnectionManager
import com.aeropaymerchantsdk.communication.DefineID
import com.aeropaymerchantsdk.view.CustomTextView

class ValidatePinActivity : BaseActivity() {

    var userEntered: String? = null
    var userPin = ""
    lateinit var backButton : ImageView

    val PIN_LENGTH = 4
    var keyPadLockedFlag = false
    var appContext: Context? = null

    var pinBox0: TextView? = null
    var pinBox1: TextView? = null
    var pinBox2: TextView? = null
    var pinBox3: TextView? = null

    var pinBoxArray: Array<TextView?>? = null

    var statusView: TextView? = null

    var button0: CustomTextView? = null
    var button1: CustomTextView? = null
    var button2: CustomTextView? = null
    var button3: CustomTextView? = null
    var button4: CustomTextView? = null
    var button5: CustomTextView? = null
    var button6: CustomTextView? = null
    var button7: CustomTextView? = null
    var button8: CustomTextView? = null
    var button9: CustomTextView? = null
    var buttonDelete: CustomTextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContext = this
        userEntered = ""

        setContentView(R.layout.activity_validate_pin)
        userPin = PrefKeeper.pinValue!!

        backButton = findViewById(R.id.back_button)
        backButton.setOnClickListener(View.OnClickListener {
            launchActivity(SignInScreenActivity::class.java)
            finishAffinity()
        })

        buttonDelete = findViewById<View>(R.id.buttonDeleteBack) as CustomTextView
        buttonDelete!!.setOnClickListener(View.OnClickListener {
            if (keyPadLockedFlag == true) {
                return@OnClickListener
            }

            if (userEntered!!.length > 0) {
                userEntered = userEntered!!.substring(0, userEntered!!.length - 1)
                pinBoxArray?.get(userEntered!!.length)!!.text = ""
            }
        }
        )

        pinBox0 = findViewById<View>(R.id.pinBox0) as TextView
        pinBox1 = findViewById<View>(R.id.pinBox1) as TextView
        pinBox2 = findViewById<View>(R.id.pinBox2) as TextView
        pinBox3 = findViewById<View>(R.id.pinBox3) as TextView

        pinBoxArray = arrayOfNulls<TextView>(PIN_LENGTH)!!
        pinBoxArray!![0] = pinBox0
        pinBoxArray!![1] = pinBox1
        pinBoxArray!![2] = pinBox2
        pinBoxArray!![3] = pinBox3



        statusView = findViewById<View>(R.id.statusMessage) as TextView

        val pinButtonHandler = View.OnClickListener { v ->
            if (keyPadLockedFlag == true) {
                return@OnClickListener
            }

            val pressedButton = v as CustomTextView

            if (userEntered!!.length < PIN_LENGTH) {
                userEntered = userEntered + pressedButton.text
                pinBoxArray!![userEntered!!.length - 1]!!.text = "8"

                if (userEntered!!.length == PIN_LENGTH) {
                    if (userEntered == userPin) {
                        keyPadLockedFlag = true
                        LockKeyPadOperation().execute("")

                        PrefKeeper.pinValue = userPin
                        var usernameEncryptValue = PrefKeeper.username
                        var passwordEncryptValue = PrefKeeper.password

                        GlobalMethods().showLoader(this)

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            var usernameIvEncryptValue = PrefKeeper.usernameIV
                            var passwordIvEncryptValue = PrefKeeper.passwordIV
                            var username = KeystoreManager.decryptText(usernameEncryptValue!!,usernameIvEncryptValue!!,KeystoreManager.ALIAS.USERNAME)
                            var password = KeystoreManager.decryptText(passwordEncryptValue!!,passwordIvEncryptValue!!,KeystoreManager.ALIAS.PWD)

                            GlobalMethods().autoLoginAction(this@ValidatePinActivity,username!!,password!!,
                                ConstantsStrings().isValidatePinActivity)
                        }
                        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                            var username = KeyStoreHelper.decrypt(KeystoreManager.ALIAS.USERNAME,usernameEncryptValue)
                            var password = KeyStoreHelper.decrypt(KeystoreManager.ALIAS.PWD,passwordEncryptValue)
                            GlobalMethods().autoLoginAction(this@ValidatePinActivity,username!!,password!!,ConstantsStrings().isValidatePinActivity)
                        }
                    }
                    else {
                        keyPadLockedFlag = true
                        LockKeyPadOperation().execute("")
                        Toast.makeText(this,"Please enter a valid Pin",Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                //Roll over
                pinBoxArray!![0]!!.text = ""
                pinBoxArray!![1]!!.text = ""
                pinBoxArray!![2]!!.text = ""
                pinBoxArray!![3]!!.text = ""

                userEntered = ""

                statusView!!.text = ""

                userEntered = userEntered + pressedButton.text

                pinBoxArray!![userEntered!!.length - 1]!!.text = "8"

            }
        }


        button0 = findViewById<View>(R.id.button0) as CustomTextView
        // button0.setTypeface(xpressive);
        button0!!.setOnClickListener(pinButtonHandler)

        button1 = findViewById<View>(R.id.button1) as CustomTextView
        // button1.setTypeface(xpressive);
        button1!!.setOnClickListener(pinButtonHandler)

        button2 = findViewById<View>(R.id.button2) as CustomTextView
        // button2.setTypeface(xpressive);
        button2!!.setOnClickListener(pinButtonHandler)


        button3 = findViewById<View>(R.id.button3) as CustomTextView
        //  button3.setTypeface(xpressive);
        button3!!.setOnClickListener(pinButtonHandler)

        button4 = findViewById<View>(R.id.button4) as CustomTextView
        // button4.setTypeface(xpressive);
        button4!!.setOnClickListener(pinButtonHandler)

        button5 = findViewById<View>(R.id.button5) as CustomTextView
        //   button5.setTypeface(xpressive);
        button5!!.setOnClickListener(pinButtonHandler)

        button6 = findViewById<View>(R.id.button6) as CustomTextView
        //   button6.setTypeface(xpressive);
        button6!!.setOnClickListener(pinButtonHandler)

        button7 = findViewById<View>(R.id.button7) as CustomTextView
        //  button7.setTypeface(xpressive);
        button7!!.setOnClickListener(pinButtonHandler)

        button8 = findViewById<View>(R.id.button8) as CustomTextView
        //  button8.setTypeface(xpressive);
        button8!!.setOnClickListener(pinButtonHandler)

        button9 = findViewById<View>(R.id.button9) as CustomTextView
        //  button9.setTypeface(xpressive);
        button9!!.setOnClickListener(pinButtonHandler)

        buttonDelete = findViewById<View>(R.id.buttonDeleteBack) as CustomTextView
        //  buttonDelete.setTypeface(xpressive);

    }

    fun onCognitoSuccess(){
        var awsConnectionManager = AWSConnectionManager(this)
        awsConnectionManager.hitServer(DefineID().FETCH_MERCHANT_PROFILE,this,null)
    }

    fun onCognitoFailure(){
        GlobalMethods().dismissLoader()
        Toast.makeText(this,"Pin-Login was not successfull", Toast.LENGTH_LONG).show()
        launchActivity(SignInScreenActivity::class.java)
    }

    override fun onBackPressed() {
        // TODO Auto-generated method stub

        //App not allowed to go back to Parent activity until correct pin entered.
        return
        //super.onBackPressed();
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_pin_entry_view, menu);
        return true
    }

    private inner class LockKeyPadOperation : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String): String {
            for (i in 0..1) {
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }

            }

            return "Executed"
        }

        override fun onPostExecute(result: String) {
            statusView!!.text = ""

            //Roll over
            pinBoxArray!!.get(0)!!.text = ""
            pinBoxArray!![1]!!.text = ""
            pinBoxArray!![2]!!.text = ""
            pinBoxArray!![3]!!.text = ""

            userEntered = ""

            keyPadLockedFlag = false
        }

        override fun onPreExecute() {}

        override fun onProgressUpdate(vararg values: Void) {}
    }


}