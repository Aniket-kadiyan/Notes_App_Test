package com.example.notesapptest.ui.login_and_signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notesapptest.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class SignupActivity : AppCompatActivity() {
    private var _binding : ActivitySignupBinding? =null
    val binding
    get()=_binding
    lateinit var ref : DatabaseReference
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        ref = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        binding?.apply {


            signupButton.setOnClickListener(){
                var uemail = signupemailET.text.toString()
                var username = signupusernameET.text.toString()
                var upassword = signuppasswordET.text.toString()
                var upasswordc = signupconfirmpasswordET.text.toString()
                var stat = validateEntry(uemail,username,upassword,upasswordc)
                Log.d("signup_data:::  ", "\n\t$uemail\n\t$username\n\t$upassword\n\t$upasswordc")
                if(stat==0) {
                    signuperrorTV.visibility = TextView.INVISIBLE
                    auth.createUserWithEmailAndPassword(uemail, upassword).addOnCompleteListener(this@SignupActivity){
                        if(it.isSuccessful){
                            ref.child("credentials").child(username).setValue(uemail)
                            var intent = Intent(applicationContext,LoginActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            this@SignupActivity.applicationContext.startActivity(intent)
                            Toast.makeText(this@SignupActivity,"signup success $it",Toast.LENGTH_SHORT).show()
                            Log.d("signup", ":::\t$it ")
                        }
                        else{
                            Toast.makeText(this@SignupActivity,"signup fail $it",Toast.LENGTH_SHORT).show()
                            Log.d("signup", ":::\t$it ")
                        }
                    }

                }
                else{
                    signuperrorTV.visibility = TextView.VISIBLE
                    signuperrorTV.text = geterrormsg(stat)
                }
            }


        }

    }

    private fun geterrormsg(stat: Int): String{
        var msg = ""
        when (stat){
            -1 -> msg = "invalid email"
            -2 -> msg = "username already exists"
            -3 -> msg = "please enter all data"
            -4 -> msg = "passwords do not match"
            -5 -> msg = "password length should be >=6"
        }
        return msg
    }

    private fun validateEntry(uemail: String, username: String, upassword: String, upasswordc: String): Int {
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(uemail).matches())
            return -1 //invalid email
        else if(upassword.isBlank() || upassword.isEmpty() || uemail.isEmpty() || uemail.isBlank() || username.isEmpty() || username.isBlank() )
            return -3 //empty field
        else if(upassword != upasswordc)
            return -4 //mismatch confirm
        else if(upassword.length<6)
            return -5 //insufficient length

        var st=0;
        ref.child("credentials").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.hasChild(username)) {
                    st =-2
                }
            }

            override fun onCancelled(error: DatabaseError) {
               // do
            }
        })
            if(st==-2){
                return -2 // username exists
            }
        return 0
    }
}