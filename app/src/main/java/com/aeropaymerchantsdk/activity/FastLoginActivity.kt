package com.aeropaymerchantsdk.activity

import android.os.Bundle
import android.view.View
import android.widget.*
import com.aeropaymerchantsdk.R
import com.aeropaymerchantsdk.Utilities.ConstantsStrings
import com.aeropaymerchantsdk.Utilities.PrefKeeper

class FastLoginActivity : BaseActivity() {

    lateinit var save : Button
    lateinit var autoLoginToggle : ToggleButton
    lateinit var pinLoginToggle : ToggleButton
    lateinit var updateTextView: TextView
    lateinit var backButton : ImageView

    var isLogin : Boolean = false
    var isPin : Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fast_login)

        initialiseControls()
        setListeners()
    }

    override fun onResume() {
        super.onResume()
        setInitialToggleStage()
    }

    private fun setListeners() {
        autoLoginToggle.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                    if(p1) {
                        isLogin = true
                        isPin = false
                        pinLoginToggle.isChecked = false
                    }
                    else {
                        isLogin = false
                    }
            }
        })

        pinLoginToggle.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                if(p1) {
                    moveToSetPin()
                }
                else {
                    isPin = false
                }
            }
        })

        updateTextView.setOnClickListener(View.OnClickListener {
            moveToSetPin()
        })
        backButton.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    private fun initialiseControls() {
        save = findViewById(R.id.saveButton)
        backButton = findViewById(R.id.back_button)
        autoLoginToggle = findViewById(R.id.autoLoginToggle)
        pinLoginToggle = findViewById(R.id.pinLoginToggle)
        updateTextView = findViewById(R.id.updateText)

        setInitialToggleStage()
    }

    private fun setInitialToggleStage() {
            var isLoginEnabled = PrefKeeper.isLoggedIn
            if(isLoginEnabled){
                autoLoginToggle.isChecked = true
                isLogin = true
            }
            else{
                autoLoginToggle.isChecked = false
                isLogin = false
            }

            var isPinEnabled = PrefKeeper.isPinEnabled
            if(isPinEnabled){
                pinLoginToggle.isChecked = true
                isPin = true
            }
            else{
                pinLoginToggle.isChecked = false
                isPin = false
            }
    }

    fun onSaveButtonClick(view: View) {
        PrefKeeper.isLoggedIn = isLogin
        PrefKeeper.isPinEnabled = isPin
        launchActivity(NavigationMenuActivity::class.java)
        finish()
    }

    fun moveToSetPin(){
        var isLoginEnabled = PrefKeeper.isLoggedIn
        launchActivity(SetPinLogin::class.java)
    }

}
