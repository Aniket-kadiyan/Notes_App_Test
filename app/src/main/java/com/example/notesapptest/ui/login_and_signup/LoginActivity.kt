package com.example.notesapptest.ui.login_and_signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notesapptest.MainActivity
import com.example.notesapptest.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class LoginActivity : AppCompatActivity() {
    private var _binding : ActivityLoginBinding?=null
    val binding
    get()=_binding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        auth = FirebaseAuth.getInstance()
        auth.signOut()
        binding?.apply {
            loginButton.setOnClickListener(){
                val username = loginusernameET.text.toString()
                val password = loginpasswordET.text.toString()
                var ref = FirebaseDatabase.getInstance().reference
                var stat=-1
                if(android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches()){
                    auth.signInWithEmailAndPassword(username,password).addOnSuccessListener {
                        var intent = Intent(applicationContext,MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        var bundle = Bundle()
                        bundle.putString("username",username)
                        intent.putExtras(bundle)
                        this@LoginActivity.applicationContext.startActivity(intent,bundle)
                    }

                }
                else{
                    ref.child("credentials").child(username)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot != null) {
                                    val userEmail = dataSnapshot.getValue(String::class.java)
                                    if (userEmail != null) {
                                        auth.signInWithEmailAndPassword(userEmail,password).addOnSuccessListener {
                                            var intent = Intent(applicationContext,MainActivity::class.java)
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            var bundle = Bundle()
                                            bundle.putString("username",username)
                                            intent.putExtras(bundle)
                                            this@LoginActivity.applicationContext.startActivity(intent,bundle)
                                        }
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                               Toast.makeText(this@LoginActivity,"incorrect username/password",Toast.LENGTH_SHORT).show()
                            }


                        })
                }

                if (stat==0){
                    var intent = Intent(applicationContext,MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    var bundle = Bundle()
                    bundle.putString("username",username)
                    intent.putExtras(bundle)
                    this@LoginActivity.applicationContext.startActivity(intent,bundle)
                }


            }
            loginsignupselectTV.setOnClickListener(){
                var intent = Intent(applicationContext,SignupActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                this@LoginActivity.applicationContext.startActivity(intent)
            }
        }
    }


}