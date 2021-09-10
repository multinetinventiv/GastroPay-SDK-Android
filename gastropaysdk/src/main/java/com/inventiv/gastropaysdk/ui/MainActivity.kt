package com.inventiv.gastropaysdk.ui

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.common.BaseActivity
import com.inventiv.gastropaysdk.common.BaseFragment
import com.inventiv.gastropaysdk.databinding.ActivityMainGastropaySdkBinding
import com.inventiv.gastropaysdk.repository.MainRepositoryImp
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.ui.login.LoginFragment
import com.inventiv.gastropaysdk.ui.merchants.MerchantsFragment
import com.inventiv.gastropaysdk.ui.pay.PayFragment
import com.inventiv.gastropaysdk.ui.pay.result.PayResultFragment
import com.inventiv.gastropaysdk.ui.wallet.WalletFragment
import com.inventiv.gastropaysdk.utils.delegate.viewBinding
import com.inventiv.gastropaysdk.utils.observeInLifecycle
import com.ncapdevi.fragnav.FragNavController
import kotlinx.coroutines.flow.onEach
import java.util.*

internal class MainActivity : BaseActivity(), FragNavController.RootFragmentListener {

    private val binding by viewBinding(ActivityMainGastropaySdkBinding::inflate)

    private lateinit var controller: FragNavController

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
        subscribeNavigationEvents()
    }

    private fun subscribeNavigationEvents() {
        viewModel.eventsFlow
            .onEach {
                when (it) {
                    MainViewModel.Event.CloseSDK -> {
                        closeSdk()
                    }
                    MainViewModel.Event.OnBackPressed -> {
                        onBackPressed()
                    }
                    is MainViewModel.Event.InitTab -> {
                        initTab(it.tabIndex)
                    }
                    is MainViewModel.Event.PushFragment -> {
                        pushFragment(it.fragment)
                    }
                    is MainViewModel.Event.PopFragment -> {
                        popFragment(it.depth)
                    }
                    else -> {
                    }
                }
            }
            .observeInLifecycle(this)
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigationViewGastroPaySdk.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_merchants -> {
                    switchTab(FragNavController.TAB1)
                }
                R.id.navigation_pay -> {
                    pushFragment(PayFragment())
                    return@setOnNavigationItemSelectedListener false
                }
                R.id.navigation_wallet -> {
                    switchTab(FragNavController.TAB2)
                }
            }
            true
        }
    }

    private fun prepareNavigationViews(savedInstanceState: Bundle?) {

        controller = FragNavController(supportFragmentManager, R.id.mainContainerGastroPaySdk)
        controller.rootFragmentListener = this

        controller.fragmentHideStrategy = FragNavController.HIDE
        controller.transactionListener = object : FragNavController.TransactionListener {
            override fun onFragmentTransaction(
                fragment: Fragment?,
                transactionType: FragNavController.TransactionType
            ) {
                prepareCommonViews(fragment)
            }

            override fun onTabTransaction(fragment: Fragment?, index: Int) {
                prepareCommonViews(fragment)
            }
        }

        controller.initialize(FragNavController.TAB1, savedInstanceState)
    }

    private fun prepareCommonViews(fragment: Fragment?) {
        if (fragment != null && fragment is BaseFragment) {
            fragment.prepareToolbar(binding.toolbarGastroPaySdk, binding.imageMainLogoGastroPaySdk)
            binding.bottomNavigationViewGastroPaySdk.visibility =
                if (fragment.showBottomNavigation()) {
                    VISIBLE
                } else {
                    GONE
                }
        }
    }

    private fun switchTab(index: Int) {
        controller.switchTab(index)
    }

    private fun pushFragment(fragment: BaseFragment) {
        controller.pushFragment(fragment)
    }

    private fun popFragment(depth: Int) {
        controller.popFragments(depth)
    }

    private fun initTab(tabIndex: Int) {
        controller.initialize(tabIndex)
        val selectedTabId = when (tabIndex) {
            FragNavController.TAB1 -> R.id.navigation_merchants
            FragNavController.TAB2 -> R.id.navigation_wallet
            else -> 0
        }
        binding.bottomNavigationViewGastroPaySdk.selectedItemId = selectedTabId
    }

    override fun onBackPressed() {
        if(controller.currentFrag is PayResultFragment){
            //Ignore onBackPressed
        }else{
            if (controller.isRootFragment.not()) {
                controller.popFragment()
            } else {
                closeSdk()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        controller.onSaveInstanceState(outState)
    }

    override val numberOfRootFragments: Int
        get() = if (GastroPaySdk.getComponent().isUserLoggedIn) 2 else 1

    override fun getRootFragment(index: Int): Fragment {
        return when (index) {
            FragNavController.TAB1 -> {
                if (GastroPaySdk.getComponent().isUserLoggedIn) {
                    MerchantsFragment()
                } else {
                    LoginFragment()
                }
            }
            FragNavController.TAB2 -> {
                WalletFragment()
            }
            else -> throw Exception("Not valid")
        }
    }
}