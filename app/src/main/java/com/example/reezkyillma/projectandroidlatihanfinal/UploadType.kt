package com.example.reezkyillma.projectandroidlatihanfinal

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_upload_type.*
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.View
import android.widget.Toast


class UploadType : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_type)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        var fct: Button = findViewById(R.id.fullcosttype)
        var wtt: Button = findViewById(R.id.peoplewantedtype)
        var oat: Button = findViewById(R.id.otherasktype)

        fct.setOnClickListener {
            fullcosttype()
        }
        wtt.setOnClickListener {
            wantedtotype()
        }
        oat.setOnClickListener {
            otherasktype()
        }

        signout.setOnClickListener{
                auth.signOut()
                startActivity(Intent(this, Login::class.java))
        }

        val bottomNavigation = findViewById<View>(R.id.bottomnavigation) as BottomNavigationView

            bottomNavigation.setOnNavigationItemSelectedListener {item ->
                when (item.itemId){
                    R.id.navigation_home ->
                        // Action when tab 1 selected
                        Toast.makeText(this, "home", Toast.LENGTH_SHORT).show()
                    R.id.navigation_addnew ->
                        // Action when tab 2 selected
                        Toast.makeText(this, "add new", Toast.LENGTH_SHORT).show()
                    R.id.navigation_search ->
                        // Action when tab 2 selected
                        Toast.makeText(this, "navigation", Toast.LENGTH_SHORT).show()
                    R.id.navigation_profile ->
                        // Action when tab 2 selected
                        Toast.makeText(this, "profil", Toast.LENGTH_SHORT).show()
                    R.id.navigation_message ->
                        // Action when tab 2 selected
                        Toast.makeText(this, "message", Toast.LENGTH_SHORT).show()
                }
                true

            }



    }


    private fun fullcosttype(){
        startActivity(Intent(this, FullCostType::class.java))
    }

    private fun otherasktype(){
        startActivity(Intent(this, OtherAskType::class.java))
    }

    private fun wantedtotype(){
        startActivity(Intent(this, PeopleWantedType::class.java))
    }

//    private fun selectedMenu(item : MenuItem) {
//        item.isChecked = true
//        when(item.itemId) {
//            R.id.navigation_home -> selectedFragment(FragmentHome.getInstance())
//            R.id.navigation_search -> selectedFragment(FragmentSearch.getInstance())
//            R.id.navigation_addnew -> selectedFragment(FragmentAddNew.getInstance())
//            R.id.navigation_message -> selectedFragment(FragmentAddNew.getInstance())
//            R.id.navigation_profile -> selectedFragment(FragmentProfil.getInstance())
//        }
//    }
//
//
//    fun selectedFragment(fragment: Fragment) {
//        var transaction : FragmentTransaction? = supportFragmentManager.beginTransaction()
//        transaction?.replace(R.id.rootFragment, fragment)
//        transaction?.commit()
//    }






}
