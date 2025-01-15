package com.nemodream.bangkkujaengi

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nemodream.bangkkujaengi.databinding.ActivityAdminBinding

class AdminActivity : AppCompatActivity() {
    private val binding: ActivityAdminBinding by lazy { ActivityAdminBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}