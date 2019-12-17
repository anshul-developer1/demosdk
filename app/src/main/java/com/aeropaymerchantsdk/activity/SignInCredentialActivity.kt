package com.aeropaymerchantsdk.activity


import android.os.Build
import android.os.Bundle
import android.text.TextWatcher
import android.util.Base64
import android.view.View
import android.widget.Toast
import com.aeropaymerchantsdk.R
import com.aeropaymerchantsdk.Utilities.*
import com.aeropaymerchantsdk.communication.AWSConnectionManager
import com.aeropaymerchantsdk.communication.DefineID
import com.aeropaymerchantsdk.view.CustomEditText
import androidx.core.content.ContextCompat
import android.text.TextUtils
import android.text.Editable
import android.widget.Button
import android.widget.ImageView


class SignInCredentialActivity : BaseActivity(){

    lateinit var userNameEdit : CustomEditText
    lateinit var passwordEdit : CustomEditText
    lateinit var signInButton : ImageView
    lateinit var userName : String
    lateinit var password : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_credential)

        userNameEdit = findViewById(R.id.userEmail)
        passwordEdit = findViewById(R.id.userPassword)
        signInButton = findViewById(R.id.signInButton)
    }


   /* override fun onResume() {
        super.onResume()
        var textWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (!TextUtils.isEmpty(userNameEdit.getText().toString()) && !TextUtils.isEmpty(passwordEdit.getText().toString())) {
                    signInButton.text = ""
                    signInButton.setBackground(ContextCompat.getDrawable(this@SignInCredentialActivity, R.drawable.sign_in_button))
                } else {
                    signInButton.text = "Sign in"
                    signInButton.setBackground(ContextCompat.getDrawable(this@SignInCredentialActivity, R.drawable.sign_in_border))
                }
            }

            override fun afterTextChanged(editable: Editable) {

            }
        }
        userNameEdit.addTextChangedListener(textWatcher)
        passwordEdit.addTextChangedListener(textWatcher)
    }*/

    // check for email and password validations for Login
    fun createUserValidation(view: View) {
         userName = userNameEdit.text.toString()
         password = passwordEdit.text.toString()

        if(userName.trim().isNullOrEmpty() || password!!.trim().isNullOrEmpty()){
            Toast.makeText(this,"Please enter Email and password.",Toast.LENGTH_SHORT).show()
        }
        else if(userName.trim().isNullOrEmpty()){
            Toast.makeText(this,"Please enter your Email.",Toast.LENGTH_SHORT).show()
        }
        else if(password!!.trim().isNullOrEmpty()){
            Toast.makeText(this,"Please enter your password.",Toast.LENGTH_SHORT).show()
        }
        else if(!(GlobalMethods().isValidEmailId(userName))){
            Toast.makeText(this,"Please enter a valid Email ID.",Toast.LENGTH_SHORT).show()
        }
        else
        {
        GlobalMethods().showLoader(this)
        view.isClickable = false
        view.isEnabled = false
        GlobalMethods().userCognitoLoginHandler(this@SignInCredentialActivity, view,userName,password)
        }
    }

    //callback for AWS Login success
    fun onCognitoSuccess(){
        savingCredentialInkeystore()
        var awsConnectionManager = AWSConnectionManager(this)
        awsConnectionManager.hitServer(DefineID().FETCH_MERCHANT_PROFILE,this,null)
    }

    //saving Login Credentials in Key Store
    private fun savingCredentialInkeystore() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                var usernameArray = KeystoreEncryptor().encryptText(KeystoreManager.ALIAS.USERNAME, userName)
                PrefKeeper.username = Base64.encodeToString(usernameArray, Base64.DEFAULT)
                var passwordArray = KeystoreEncryptor().encryptText(KeystoreManager.ALIAS.PWD, password)
                PrefKeeper.password = Base64.encodeToString(passwordArray, Base64.DEFAULT)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                KeyStoreHelper.createKeys(this, KeystoreManager.ALIAS.USERNAME)
                KeyStoreHelper.createKeys(this, KeystoreManager.ALIAS.PWD)

                PrefKeeper.username =
                    KeyStoreHelper.encrypt(KeystoreManager.ALIAS.USERNAME, userName)
                PrefKeeper.password = KeyStoreHelper.encrypt(KeystoreManager.ALIAS.PWD, password)
            }
        }

    //callback for AWS Login failure
    fun onCognitoFailure(){
        GlobalMethods().dismissLoader()
        Toast.makeText(this,"Invalid username or password", Toast.LENGTH_LONG).show()
    }
}
