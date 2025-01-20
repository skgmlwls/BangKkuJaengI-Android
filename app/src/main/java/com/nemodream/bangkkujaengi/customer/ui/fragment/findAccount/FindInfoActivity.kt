package com.nemodream.bangkkujaengi.customer.ui.fragment.findAccount

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.nemodream.bangkkujaengi.R
import com.nemodream.bangkkujaengi.databinding.ActivityFindInfoBinding

class FindInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFindInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityFindInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Toolbar 설정
        val toolbar: MaterialToolbar = findViewById(R.id.materialToolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        // TabLayout 및 ViewPager2 연결
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)

        // ViewPager2 어댑터 설정
        val fragments = listOf(
            FindIdFragment(),
            FindPasswordFragment()
        )
        val adapter = FindInfoPagerAdapter(this, fragments)
        viewPager.adapter = adapter

        // TabLayoutMediator로 TabLayout과 ViewPager2 연결
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "아이디 찾기"  // 탭1 제목
                1 -> "비밀번호 찾기" // 탭2 제목
                else -> null
            }
        }.attach()
    }
}

class FindInfoPagerAdapter(
    activity: FragmentActivity,
    private val fragments: List<Fragment>
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}