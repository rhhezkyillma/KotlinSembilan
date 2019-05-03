package com.example.reezkyillma.projectandroidlatihanfinal

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    //deklarasi untuk request code
    private val RC_SIGN_IN = 7

    //deklarasi untuk sign in client
    private lateinit var mGoogleSignInClient: GoogleSignInClient

      override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


          // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

          // Configure Google Sign In
          val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
              .requestIdToken(getString(R.string.default_web_client_id))
              .requestEmail()
              .build()

         mGoogleSignInClient = GoogleSignIn.getClient(this, gso)


        var btn: Button = findViewById(R.id.btnLogin)
        btn.setOnClickListener {
            login()
          }

        var register: TextView = findViewById(R.id.tvRegister)
        register.setOnClickListener {
            register()
          }

        btn_google.setOnClickListener{
              signIngoogle()
          }





      }

    private fun register(){
        startActivity(Intent(this, Register::class.java))
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }


    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            Toast.makeText(this, "Hello ${currentUser.email}", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, UploadType::class.java))
            finish()
        }

    }

    private fun login(){

        val emailTxt : EditText = findViewById(R.id.etEmail)
        var email = emailTxt.text.toString()
        val passwordTxt : EditText = findViewById(R.id.etPassword)
        var password = passwordTxt.text.toString()

        if (!email.isEmpty() && !password.isEmpty()) {
            this.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("LOGIN", "signInWithEmail:success")
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("LOGIN", "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(null)
                    }
                }
        } else {
            Toast.makeText(this,"Please fill up Username, Email, and Password :|", Toast.LENGTH_LONG).show()
        }


    }

    private fun signIngoogle() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(
                    ApiException::class.java
                )
                firebaseAuthWithGoogle(account!!)
            }catch (e: ApiException){
                Log.w("LOGIN", "Login failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("LOGIN", "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("LOGIN", "Sign in Success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("LOGIN", "Sign in Error", task.exception)
                    Toast.makeText(this, "Sign In Failure", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }
}
