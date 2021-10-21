package com.multinetinventiv.gastropay.ui.profile.settings.notification

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.load.engine.Resource
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.common.BaseFragment
import com.inventiv.gastropaysdk.data.request.ConfirmProvisionRequest
import com.inventiv.gastropaysdk.databinding.FragmentNotificationPreferencesGastropaySdkBinding
import com.inventiv.gastropaysdk.repository.MainRepositoryImp
import com.inventiv.gastropaysdk.repository.ProfileRepositoryImp
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.ui.MainViewModel
import com.inventiv.gastropaysdk.ui.MainViewModelFactory
import com.inventiv.gastropaysdk.ui.settings.notification.*
import com.inventiv.gastropaysdk.ui.settings.notification.NotificationPreferencesAdapter
import com.inventiv.gastropaysdk.ui.settings.notification.NotificationPreferencesViewModel
import com.inventiv.gastropaysdk.ui.settings.notification.NotificationPreferencesViewModelFactory
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.ConvertUtils
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.LogUtils
import com.inventiv.gastropaysdk.utils.delegate.viewBinding
import com.inventiv.gastropaysdk.utils.handleError
import com.inventiv.gastropaysdk.utils.itemdecorator.RecyclerMarginDecoration
import com.inventiv.gastropaysdk.view.GastroPaySdkToolbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


internal class NotificationPreferencesFragment : BaseFragment(R.layout.fragment_notification_preferences_gastropay_sdk) {

    override fun prepareToolbar(toolbar: GastroPaySdkToolbar, logo: AppCompatImageView) {
        hideToolbar(toolbar, logo)
    }

    override fun showBottomNavigation() = false

    private val binding by viewBinding(FragmentNotificationPreferencesGastropaySdkBinding::bind)

    private lateinit var notificationPreferencesAdapter: NotificationPreferencesAdapter

    private var notificationPreferencesList = ArrayList<NotificationPreferencesBases>()

    private val preferencesViewModel: NotificationPreferencesViewModel by lazy {
        val viewModelFactory = NotificationPreferencesViewModelFactory(
            ProfileRepositoryImp(GastroPaySdk.getComponent().gastroPayService)
        )
        ViewModelProvider(this, viewModelFactory).get(NotificationPreferencesViewModel::class.java)
    }

    private val sharedViewModel: MainViewModel by lazy {
        val viewModelFactory = MainViewModelFactory(
            MainRepositoryImp(GastroPaySdk.getComponent().gastroPayService)
        )
        ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO: -data oluştur
        //TODO: -liste oluştur--> adapter -->recyclera ver
        setupObservers()
        //setupListeners()
        notificationPreferencesList.add(NotificationPreferencesBases.NotificationPreferencesHeader(id = 1,label="test"))
        notificationPreferencesList.add(NotificationPreferencesBases.NotificationPreferencesItem(id = 1,preferencesChannel= NotificationPreferencesChannelType.SMS,preferencesState=NotificationPreferencesStateType.ON))

        setupNotificationPreferencesAdapter()
    }

    private fun setupNotificationPreferencesAdapter() {
        notificationPreferencesAdapter = NotificationPreferencesAdapter(notificationPreferencesList)
        { id: Int, channel: Int, newState: Int ->
        }
        binding.notificationPreferencesRecyclerViewGastroPaySdk.addItemDecoration(
            RecyclerMarginDecoration(ConvertUtils.dp2px(16f))
        )
        binding.notificationPreferencesRecyclerViewGastroPaySdk.adapter = notificationPreferencesAdapter

    }


    private fun setupListeners() {
        //binding.toolbar.setNavigationOnClickListener {
          //  sharedViewModel.onBackPressed()
        //}

    }

   private fun setupObservers() {
       viewLifecycleOwner.lifecycleScope.launchWhenStarted {
           launch {
               preferencesViewModel.notificationPreferencesState.collect { uiState ->
                   when (uiState) {
                       is com.inventiv.gastropaysdk.data.Resource.Loading -> {
                           if (uiState.isLoading) {
                               binding.loadingLayout.visibility = View.VISIBLE
                           } else {
                               binding.loadingLayout.visibility = View.GONE
                           }
                       }
                       is com.inventiv.gastropaysdk.data.Resource.Success -> {
                           LogUtils.d(uiState.data)
                           notificationPreferencesList.clear()
                           notificationPreferencesList.addAll(uiState.data)
                           notificationPreferencesAdapter.notifyDataSetChanged()
                       }
                       is com.inventiv.gastropaysdk.data.Resource.Error -> {
                           uiState.apiError.handleError(requireActivity())
                       }
                       else -> {
                       }
                   }
               }
           }
       }
/*
           binding.switch.setOnClickListener {
               preferencesViewModel.updateNotificationPreferencesState(
               ConfirmProvisionRequest(
                   channel=  ,
                   newstate=
               )
           )
       }

*/
   }

}