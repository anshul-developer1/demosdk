package com.aeropaymerchantsdk.activity

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.aeropaymerchantsdk.R
import com.aeropaymerchantsdk.view.CustomTextView


class SetPinLogin : BaseActivity() {

    var userEntered: String? = null
    var userPin = "8888"
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

        setContentView(R.layout.activity_set_pin_login)

        backButton = findViewById(R.id.back_button)
        backButton.setOnClickListener(View.OnClickListener {
            finish()
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
                Log.v("PinView", "User entered=$userEntered")

                //Update pin boxes
                pinBoxArray!![userEntered!!.length - 1]!!.text = "8"

                if (userEntered!!.length == PIN_LENGTH) {
                    var intent = Intent(this@SetPinLogin,ConfirmPinActivity::class.java)
                    intent.putExtra("PIN VALUE",userEntered)
                    launchActivity(ConfirmPinActivity::class.java,intent)
                }
            } else {
                pinBoxArray!![0]!!.text = ""
                pinBoxArray!![1]!!.text = ""
                pinBoxArray!![2]!!.text = ""
                pinBoxArray!![3]!!.text = ""
                userEntered = ""

                statusView!!.text = ""
                userEntered = userEntered + pressedButton.text

                //Update pin boxes
                pinBoxArray!![userEntered!!.length - 1]!!.text = "8"

            }
        }


        button0 = findViewById<View>(R.id.button0) as CustomTextView
        button0!!.setOnClickListener(pinButtonHandler)
        button1 = findViewById<View>(R.id.button1) as CustomTextView
        button1!!.setOnClickListener(pinButtonHandler)
        button2 = findViewById<View>(R.id.button2) as CustomTextView
        button2!!.setOnClickListener(pinButtonHandler)
        button3 = findViewById<View>(R.id.button3) as CustomTextView
        button3!!.setOnClickListener(pinButtonHandler)
        button4 = findViewById<View>(R.id.button4) as CustomTextView
        button4!!.setOnClickListener(pinButtonHandler)
        button5 = findViewById<View>(R.id.button5) as CustomTextView
        button5!!.setOnClickListener(pinButtonHandler)
        button6 = findViewById<View>(R.id.button6) as CustomTextView
        button6!!.setOnClickListener(pinButtonHandler)
        button7 = findViewById<View>(R.id.button7) as CustomTextView
        button7!!.setOnClickListener(pinButtonHandler)
        button8 = findViewById<View>(R.id.button8) as CustomTextView
        button8!!.setOnClickListener(pinButtonHandler)
        button9 = findViewById<View>(R.id.button9) as CustomTextView
        button9!!.setOnClickListener(pinButtonHandler)
        buttonDelete = findViewById<View>(R.id.buttonDeleteBack) as CustomTextView
    }

    // locking the keypad after PIN length is matched
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