package com.inventiv.gastropaysdk.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.common.BaseActivity
import com.inventiv.gastropaysdk.data.model.Resource
import com.inventiv.gastropaysdk.databinding.ActivityMainGastropaySdkBinding
import com.inventiv.gastropaysdk.repository.MainRepositoryImp
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.ui.home.HomeFragment
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.LogUtils
import com.inventiv.gastropaysdk.utils.viewBinding
import com.ncapdevi.fragnav.FragNavController
import kotlinx.coroutines.flow.collect
import java.util.*

internal class MainActivity : BaseActivity() {

    private val binding by viewBinding(ActivityMainGastropaySdkBinding::inflate)

    private lateinit var controller: FragNavController
    private val rootFragments = ArrayList<Fragment>()

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupViewModel()
        setupObservers()
        prepareNavigationViews(savedInstanceState)

        viewModel.getDummy(1)
    }

    private fun prepareNavigationViews(savedInstanceState: Bundle?) {
        rootFragments.add(HomeFragment())

        controller = FragNavController(supportFragmentManager, R.id.mainContainer)
        controller.rootFragments = rootFragments

        controller.fragmentHideStrategy = FragNavController.HIDE
        controller.initialize(FragNavController.TAB1, savedInstanceState)

        controller.transactionListener = object : FragNavController.TransactionListener {
            override fun onFragmentTransaction(
                fragment: Fragment?,
                transactionType: FragNavController.TransactionType
            ) {

            }

            override fun onTabTransaction(fragment: Fragment?, index: Int) {
            }
        }
    }

    private fun setupViewModel() {
        val viewModelFactory = MainViewModelFactory(
            MainRepositoryImp(GastroPaySdk.getComponent().gastroPayService)
        )
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(MainViewModel::class.java)
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    is Resource.Loading -> {
                        LogUtils.d("Resource", uiState.isLoading)
                    }
                    is Resource.Success -> {
                        LogUtils.d("Resource", uiState.data)
                    }
                    is Resource.Error -> {
                        LogUtils.d("Resource", uiState.apiError)
                    }
                    else -> {
                    }
                }
            }
        }
    }
}