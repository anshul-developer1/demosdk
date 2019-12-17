package com.aeropaymerchantsdk.activity

import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import com.aeropaymerchantsdk.Model.AeropayModelManager
import com.aeropaymerchantsdk.R
import com.aeropaymerchantsdk.Utilities.*
import com.aeropaymerchantsdk.adapter.NavigationMenuAdapter
import com.aeropaymerchantsdk.communication.AWSConnectionManager
import com.aeropaymerchantsdk.communication.DefineID

class NavigationMenuActivity : BaseActivity() {

    lateinit var menuListView : ListView
    lateinit var logout : Button
    lateinit var merchantName : TextView
    lateinit var merchantEmail : TextView
    lateinit var merchantStore : TextView
    lateinit var backButton : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_menu)
        initialiseControls()
        setListeners()

    }

    // setting on click listeners for UI
    private fun setListeners() {
        menuListView.setOnItemClickListener { parent, view, position, id ->
            if(position == 0){
                var awsConnectionManager = AWSConnectionManager(this)
                awsConnectionManager.hitServer(DefineID().FETCH_MERCHANT_LOCATIONS,this,null)
            }
            else if(position == 1){
                launchActivity(FastLoginActivity::class.java)
            }
        }

        backButton.setOnClickListener(View.OnClickListener {
            finish()
        })
    }


    // inflating UI controls
    private fun initialiseControls() {
        menuListView = findViewById(R.id.itemsList)
        logout = findViewById(R.id.logoutButton)
        merchantName = findViewById(R.id.merchantNameText)
        merchantEmail = findViewById(R.id.merchantEmail)
        merchantStore = findViewById(R.id.storeNameText)
        backButton = findViewById(R.id.back_button)


        var itemsNameArray =  resources?.getStringArray(R.array.navigation_items)
        var itemsImageArray =  arrayOf(R.drawable.settings, R.drawable.timer)
        val itemsListView = NavigationMenuAdapter(this,itemsNameArray!!,itemsImageArray)
        menuListView ?.adapter = itemsListView

        var objModelManager = AeropayModelManager().getInstance()
        merchantName.text = objModelManager.merchantProfileModel.merchant.name.toString()

        var usernameEncryptValue = PrefKeeper.username

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var usernameIvEncryptValue = PrefKeeper.usernameIV
            var username = KeystoreManager.decryptText(usernameEncryptValue!!,usernameIvEncryptValue!!, KeystoreManager.ALIAS.USERNAME)
            merchantEmail.text = username
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            var usernameEncryptValue = PrefKeeper.username
            var username = KeyStoreHelper.decrypt(KeystoreManager.ALIAS.USERNAME,usernameEncryptValue)
            merchantEmail.text = username
        }

        var storeName = PrefKeeper.storeName
        if(storeName.equals(ConstantsStrings().noValue))
        {
            merchantStore.text = ""
        }
        else{
            merchantStore.text = storeName
        }
    }

    // sign out button click event
    fun onLogoutButtonClick(view: View) {
        if(PrefKeeper.isPinEnabled || PrefKeeper.isLoggedIn){
            finishAffinity()
            launchActivity(SignInScreenActivity::class.java)
        }
        else{
            PrefKeeper.clear()
            finishAffinity()
            launchActivity(SignInScreenActivity::class.java)
        }
    }
}
