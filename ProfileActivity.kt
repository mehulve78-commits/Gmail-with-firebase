package com.app.gmail_activitywithfirebasekotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ProfileActivity : AppCompatActivity() {
    lateinit var ivimage : ImageView
    lateinit var tvimage : TextView
    lateinit var bt_logout : Button
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        ivimage = findViewById(R.id.iv_profile)
        tvimage = findViewById(R.id.tv_profile)
        bt_logout = findViewById(R.id.btn_logout)

        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser

        if(firebaseUser!=null) {
            Glide.with(this@ProfileActivity).load(firebaseUser.photoUrl).into(ivimage)
            tvimage.text = firebaseUser.displayName
        }
        googleSignInClient = GoogleSignIn.getClient(this@ProfileActivity,GoogleSignInOptions.DEFAULT_SIGN_IN)
        bt_logout.setOnClickListener {
            googleSignInClient.signOut().addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    firebaseAuth.signOut()
                    Toast.makeText(applicationContext,"Logout Sucessfull",Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}