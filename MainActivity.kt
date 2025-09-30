package com.app.gmail_activitywithfirebasekotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {
    lateinit var btnsignin : ImageView
    lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnsignin = findViewById(R.id.img_g)
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("583759610543-8440tsc97s7k0a1s2d29lcsh07r5t6ho.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this@MainActivity, googleSignInOptions)
        btnsignin.setOnClickListener {
            val intent: Intent = googleSignInClient.signInIntent
            startActivityForResult(intent, 100)
        }
        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
        if (firebaseUser != null) {
            startActivity(Intent(this@MainActivity,ProfileActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

      /*  if(resultCode==100){
            var account=GoogleSignIn.getSignedInAccountFromIntent(data)
            if(account.isSuccessful){
                var acc=account.result
                if(acc!=null) {
                    Log.d("@EMAIl",acc.email.toString())
                }
            }
    }*/
        if (requestCode == 100){
            val signInTask : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)

            if (signInTask.isSuccessful){
                val m = "Google SignIn Successfull"
                displayToast(m)
                try {
                    val googleSignInAccount = signInTask.getResult(ApiException::class.java)
                    if (googleSignInAccount!=null) {
                        val authCredential : AuthCredential = GoogleAuthProvider.getCredential(googleSignInAccount.idToken,null)
                        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this) {task->
                                if(task.isSuccessful) {
                                    startActivity(Intent(this@MainActivity,ProfileActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                                    displayToast("Firebase Authentication is successfull")
                                } else {
                                    displayToast("Authentication Failed:" + task.exception?.message)

                                }
                        }

                    }

                }catch (e:ApiException) {
                    e.printStackTrace()

                }

            }
        }

    }

    private fun displayToast(m: String) {
        Toast.makeText(applicationContext,m,Toast.LENGTH_SHORT).show()
    }
}