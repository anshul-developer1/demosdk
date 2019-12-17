package com.aeropaymerchantsdk.activity

import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aeropaymerchantsdk.R
import com.aeropaymerchantsdk.Utilities.ConstantsStrings
import com.aeropaymerchantsdk.Utilities.GlobalMethods
import com.aeropaymerchantsdk.Utilities.PrefKeeper
import com.aeropaymerchantsdk.adapter.HomeCardRecyclerView
import com.aeropaymerchantsdk.adapter.HomeListRecyclerView

class HomeActivity : BaseActivity() {

    lateinit var menuButton : ImageView
    lateinit var listViewRecycler : RecyclerView
    lateinit var cardViewRecycler : RecyclerView
    lateinit var readyToPay : TextView
    lateinit var aeropayTransparent : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initialiseControls()
        setupView()
        setListeners()

        maintainUserLoginCount()

        var loginCount = PrefKeeper.logInCount
        if(loginCount< 4){
            var isPin = PrefKeeper.isPinEnabled
            var isLogin = PrefKeeper.isLoggedIn
            if(!isPin && !isLogin)
                GlobalMethods().showDialog(this)
        }
    }

    private fun setListeners() {
        menuButton.setOnClickListener(View.OnClickListener {
            launchActivity(NavigationMenuActivity::class.java)
        })
    }

    private fun setupView() {
        listViewRecycler.layoutManager = LinearLayoutManager(this)
        val payerName: ArrayList<String> = ArrayList()
        payerName.add("Daniel")
        payerName.add("Adam")
        listViewRecycler.adapter = HomeListRecyclerView(payerName,this)


        cardViewRecycler.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        val userName: ArrayList<String> = ArrayList()
        userName.add("Pete")
        userName.add("Smith")
        userName.add("Tanner")
        cardViewRecycler.adapter = HomeCardRecyclerView(userName,this)
    }

    private fun initialiseControls() {
        menuButton = findViewById(R.id.back_button)
        listViewRecycler = findViewById(R.id.recyclerListView)
        cardViewRecycler = findViewById(R.id.cardRecyclerView)
        readyToPay = findViewById(R.id.readyToPayText)
        aeropayTransparent = findViewById(R.id.aeropayTranparentLogo)

        var text = "<font color=#06dab3>4</font> <font color=#232323>ready to pay</font>";
        readyToPay.setText(Html.fromHtml(text));

        aeropayTransparent.visibility = View.GONE
    }

    private fun maintainUserLoginCount() {
        var initialLoginCount = PrefKeeper.logInCount
        var finalCount = initialLoginCount + 1
        PrefKeeper.logInCount = finalCount
    }
}
