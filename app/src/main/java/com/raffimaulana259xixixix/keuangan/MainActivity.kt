package com.raffimaulana259xixixix.keuangan

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.raffimaulana259xixixix.keuangan.databinding.ActivityMainBinding
import com.raffimaulana259xixixix.keuangan.fragments.AddTransactionFragment
import com.raffimaulana259xixixix.keuangan.fragments.HistoryFragment
import com.raffimaulana259xixixix.keuangan.fragments.HomeFragment

/**
 * MainActivity - Activity utama dengan Navigation Drawer
 * Menampilkan drawer menu dengan Home, Riwayat Transaksi, dan Tambah Transaksi
 */
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Toolbar
        setSupportActionBar(binding.toolbar)

        // Setup Navigation Drawer
        setupNavigationDrawer()

        // Setup OnBackPressed
        setupBackPressed()

        // Load default fragment (Home)
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
            binding.navigationView.setCheckedItem(R.id.nav_home)
        }
    }

    private fun setupNavigationDrawer() {
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener(this)
    }

    private fun setupBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    // Handle back navigation untuk fragment
                    if (supportFragmentManager.backStackEntryCount > 0) {
                        supportFragmentManager.popBackStack()
                    } else {
                        isEnabled = false
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                loadFragment(HomeFragment())
            }
            R.id.nav_history -> {
                loadFragment(HistoryFragment())
            }
            R.id.nav_add_transaction -> {
                loadFragment(AddTransactionFragment())
            }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    /**
     * Method publik untuk load fragment dari fragment lain
     * dan update checked item di navigation drawer
     */
    fun loadFragmentAndUpdateMenu(fragment: Fragment, menuItemId: Int) {
        loadFragment(fragment)
        binding.navigationView.setCheckedItem(menuItemId)
    }
}


