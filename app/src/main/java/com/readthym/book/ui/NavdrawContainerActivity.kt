package com.readthym.book.ui

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.animation.TranslateAnimation
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.downloader.PRDownloader
import com.google.android.material.navigation.NavigationView
import com.readthym.book.R
import com.readthym.book.data.local.MyPreference
import com.readthym.book.databinding.ActivityNavdrawContainerBinding
import com.readthym.book.databinding.NavHeaderNavdrawContainerBinding
import com.readthym.book.utils.base.BaseActivity
import org.koin.android.viewmodel.ext.android.viewModel


class NavdrawContainerActivity : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityNavdrawContainerBinding

    val viewModel: SharedViewModel by viewModel()

    lateinit var navController: NavController

    companion object {
        const val NEXT_DESTINATION = "Next Dest"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNavdrawContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        PRDownloader.initialize(applicationContext)

        initUi()
        initData()
        initObserver()

        binding.appBarNavdrawContainer.includeContent.apply {

        }


        setSupportActionBar(binding.appBarNavdrawContainer.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_content_navdraw_container)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
//        navViewBottom.setupWithNavController(navController)

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_logout -> {
                    handleDrawer()
                    MyPreference(this).clearPreferences()
                    navController.navigate(R.id.nav_home)
                    return@setNavigationItemSelectedListener false
                }
                else -> {
                    return@setNavigationItemSelectedListener false

                }
            }
        }

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            getSupportActionBar()?.hide()
            hideSoftKeyBoard(this)

            when (getCurrentNav()) {
                R.id.nav_home,
                R.id.readthymLoginFragment -> {
                    hideBottomNav()
                }
                else -> {
                    hideBottomNav()
                }
            }

            when (getCurrentNav()) {
                R.id.nav_home -> {
                }
            }
        }
    }


    private fun goToHome() {
        goToMenu(R.id.nav_home)
    }

    private fun initUi() {
        binding.appBarNavdrawContainer.includeContent.btnMenuDoc

        val headerView: View = binding.navView.getHeaderView(0)
        val headerBinding: NavHeaderNavdrawContainerBinding =
            NavHeaderNavdrawContainerBinding.bind(headerView)


    }

    private fun initData() {
    }

    private fun goToMenu(destination: Int) {
        val pref = MyPreference(this)
        val token = pref.getToken()
        navController.navigate(destination)
    }

    private fun initObserver() {
        val screen = binding.appBarNavdrawContainer.includeContent
    }



    private fun revokeOtherMenu(it: BottomMenu) {
        val screen = binding.appBarNavdrawContainer.includeContent

        val list = mutableListOf(
            Pair(screen.btnMenuDoc, BottomMenu.DOC),
            Pair(screen.btnMenuProfile, BottomMenu.PROFILE),
            Pair(screen.btnMenuNotif, BottomMenu.NOTIF),
            Pair(screen.btnMenuHome, BottomMenu.HOME)
        )

        list.forEachIndexed { index, imageButton ->
            if (imageButton.second != it) {
                imageButton.first.isSelected = false
            }
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.navdraw_container, menu)
//        return true
//    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_navdraw_container)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun handleDrawer(state: Int = 99) {
        val navDrawer: DrawerLayout = findViewById(R.id.drawer_layout)
        // If the navigation drawer is not open then open it, if its already open then close it.

        if (state == 0) {

        } else {
            if (!navDrawer.isDrawerOpen(Gravity.LEFT)) {
                navDrawer.openDrawer(Gravity.LEFT)
            } else {
                navDrawer.closeDrawer(
                    Gravity.RIGHT
                )
            }
        }

    }

    private fun getCurrentNav(): Int? {
        val navController = findNavController(R.id.nav_host_fragment_content_navdraw_container)
        return navController.currentDestination?.id
    }

    private fun showBottomNav(duration: Int = 400) {
//        val bottomNav = binding.appBarNavdrawContainer.includeContent.navViewBottom
        val bottomNav = binding.appBarNavdrawContainer.includeContent.customBnContainer
        val bnMenu = bottomNav
        if (bnMenu.visibility == View.VISIBLE) return
        bnMenu.visibility = View.VISIBLE
        val animate = TranslateAnimation(0f, 0f, bnMenu.height.toFloat(), 0f)
        animate.duration = duration.toLong()
        bnMenu.startAnimation(animate)
    }

    private fun hideBottomNav(duration: Int = 400) {
//        val bottomNav = binding.appBarNavdrawContainer.includeContent.navViewBottom
        val bottomNav = binding.appBarNavdrawContainer.includeContent.customBnContainer
        val bnMenu = bottomNav
        if (bnMenu.visibility == View.GONE) return
        val animate = TranslateAnimation(0f, 0f, 0f, bnMenu.height.toFloat())
        animate.duration = duration.toLong()
        bnMenu.startAnimation(animate)
        bnMenu.visibility = View.GONE
    }
}
