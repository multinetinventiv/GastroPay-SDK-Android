package com.inventiv.gastropaysdk.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.common.BaseActivity
import com.inventiv.gastropaysdk.common.BaseFragment
import com.inventiv.gastropaysdk.databinding.ActivityMainGastropaySdkBinding
import com.inventiv.gastropaysdk.repository.MainRepositoryImp
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.ui.merchants.MerchantsFragment
import com.inventiv.gastropaysdk.ui.pay.PayFragment
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.LogUtils
import com.inventiv.gastropaysdk.utils.delegate.viewBinding
import com.ncapdevi.fragnav.FragNavController
import java.util.*

internal class MainActivity : BaseActivity() {

    private val binding by viewBinding(ActivityMainGastropaySdkBinding::inflate)

    private lateinit var controller: FragNavController
    private val rootFragments = ArrayList<Fragment>()

    private val viewModel: MainViewModel by lazy {
        val viewModelFactory = MainViewModelFactory(
            MainRepositoryImp(GastroPaySdk.getComponent().gastroPayService)
        )
        ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        prepareNavigationViews(savedInstanceState)
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigationViewGastroPaySdk.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_merchants -> {
                    switchTab(FragNavController.TAB1)
                }
                R.id.navigation_pay -> {
                    switchTab(FragNavController.TAB2)
                    return@setOnNavigationItemSelectedListener false
                }
                R.id.navigation_wallet -> {
                    switchTab(FragNavController.TAB3)
                }
            }
            true
        }
    }

    private fun prepareNavigationViews(savedInstanceState: Bundle?) {
        rootFragments.add(MerchantsFragment())
        rootFragments.add(PayFragment())
        rootFragments.add(MerchantsFragment())

        controller = FragNavController(supportFragmentManager, R.id.mainContainerGastroPaySdk)
        controller.rootFragments = rootFragments

        controller.fragmentHideStrategy = FragNavController.DETACH_ON_NAVIGATE_HIDE_ON_SWITCH
        controller.initialize(FragNavController.TAB1, savedInstanceState)

        controller.transactionListener = object : FragNavController.TransactionListener {
            override fun onFragmentTransaction(
                fragment: Fragment?,
                transactionType: FragNavController.TransactionType
            ) {

            }

            override fun onTabTransaction(fragment: Fragment?, index: Int) {
                LogUtils.d("FRAGMENT_INDEX", index)
                if (index == FragNavController.TAB2) {
                    //TODO hide toolbar & bottom navigation
                    binding.bottomNavigationViewGastroPaySdk.visibility = View.GONE
                } else {
                    binding.bottomNavigationViewGastroPaySdk.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun switchTab(index: Int) {
        controller.switchTab(index)
    }

    fun pushFragment(framgnet: BaseFragment) {
        controller.pushFragment(framgnet)
    }

    override fun onBackPressed() {
        if (controller.isRootFragment.not()) {
            controller.popFragment()
        } else {
            super.onBackPressed()
        }
    }
}