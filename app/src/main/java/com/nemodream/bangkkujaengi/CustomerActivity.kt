package com.nemodream.bangkkujaengi

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.nemodream.bangkkujaengi.databinding.ActivityCustomerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomerActivity : AppCompatActivity() {
    private val binding: ActivityCustomerBinding by lazy { ActivityCustomerBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val navController = setupNavHost()
        setupListeners(navController)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.customer_container) as NavHostFragment

        onBackPressedDispatcher.addCallback(this) {
            val navController = navHostFragment.navController
            if (!navController.popBackStack()) {
                finish()
            }
        }
    }

    // NavHost 설정
    private fun setupNavHost(): NavController {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.customer_container) as NavHostFragment
        val navController = navHostFragment.navController
        binding.customerBottomNavigation.setupWithNavController(navController)
        return navController
    }

    private fun setupListeners(navController: NavController) {
        // 특정 화면에서 BottomNavigation 숨기기/보이기
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                // BottomNavigation이 보여야 되는 화면 정의
                R.id.navigation_home, R.id.navigation_social -> {
                    binding.customerBottomNavigation.visibility = View.VISIBLE
                }

                else -> {
                    binding.customerBottomNavigation.visibility = View.GONE
                }
            }
        }
    }
}