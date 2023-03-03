package com.vango.orderprocessing

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.vango.orderprocessing.auth.models.AuthResult
import com.vango.orderprocessing.databinding.ActivityMainBinding
import com.vango.orderprocessing.ui.UserActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.authenticate()
        viewModel.livedata.observe(this){
            Timber.d("result ${it.javaClass.simpleName}")
            if(it is AuthResult.Authorized) login() else Toast.makeText(this, getString(R.string.wrong_password), Toast.LENGTH_SHORT).show()
        }
        binding.login.setOnClickListener {
            viewModel.signIn(binding.username.text.toString(), binding.password.text.toString())
        }
        binding.register.setOnClickListener {
            viewModel.signUp(binding.usernamereg.text.toString(), binding.passwordreg.toString())
        }
    }

    private fun login() {
        val intent = Intent(this, UserActivity::class.java)
        startActivity(intent)
    }

}