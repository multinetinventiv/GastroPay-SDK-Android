package com.inventiv.gastropaysdk.ui.settings.notification

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.common.BaseFragment
import com.inventiv.gastropaysdk.data.Resource
import com.inventiv.gastropaysdk.databinding.FragmentNotificationPreferencesGastropaySdkBinding
import com.inventiv.gastropaysdk.repository.MainRepositoryImp
import com.inventiv.gastropaysdk.repository.ProfileRepositoryImp
import com.inventiv.gastropaysdk.shared.GastroPaySdk
import com.inventiv.gastropaysdk.ui.MainViewModel
import com.inventiv.gastropaysdk.ui.MainViewModelFactory
import com.inventiv.gastropaysdk.utils.REFERENCE_UNDEFINED
import com.inventiv.gastropaysdk.utils.blankj.utilcode.util.StringUtils
import com.inventiv.gastropaysdk.utils.delegate.viewBinding
import com.inventiv.gastropaysdk.utils.handleError
import com.inventiv.gastropaysdk.view.GastroPaySdkToolbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


internal class NotificationPreferencesFragment :
    BaseFragment(R.layout.fragment_notification_preferences_gastropay_sdk) {

    override fun prepareToolbar(toolbar: GastroPaySdkToolbar, logo: AppCompatImageView) {
        toolbar.apply {
            changeToLoginStyle()
            setTitle(
                StringUtils.getString(R.string.settings_notification_toolbar_title_gastropay_sdk),
                R.color.celtic_gastropay_sdk
            )
            setLeftIcon(R.drawable.ic_arrow_back_gastropay_sdk)
            setRightIcon(REFERENCE_UNDEFINED, null)
            onLeftIconClick {
                sharedViewModel.onBackPressed()
            }
        }
        showToolbar(false, toolbar, logo)
    }

    override fun showBottomNavigation() = false

    private val binding by viewBinding(FragmentNotificationPreferencesGastropaySdkBinding::bind)

    private lateinit var notificationPreferencesAdapter: NotificationPreferencesAdapter

    private var notificationPreferencesList = ArrayList<NotificationPreferencesBase>()

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
        setupObservers()
        setupNotificationPreferencesAdapter()
        preferencesViewModel.notificationPreferences()
    }

    private fun setupNotificationPreferencesAdapter() {
        notificationPreferencesAdapter = NotificationPreferencesAdapter(notificationPreferencesList)
        { id: Int, channel: Int, newState: Int ->
            preferencesViewModel.updateNotificationPreferences(id, channel, newState)
        }
        binding.notificationPreferencesRecyclerView.adapter = notificationPreferencesAdapter

    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            launch {
                preferencesViewModel.notificationPreferencesState.collect { uiState ->
                    when (uiState) {
                        is Resource.Loading -> {
                            if (uiState.isLoading) {
                                binding.loading.loadingLayout.visibility = View.VISIBLE
                            } else {
                                binding.loading.loadingLayout.visibility = View.GONE
                            }
                        }
                        is Resource.Success -> {
                            notificationPreferencesList.clear()
                            notificationPreferencesList.addAll(uiState.data)
                            notificationPreferencesAdapter.notifyDataSetChanged()
                        }
                        is Resource.Error -> {
                            uiState.apiError.handleError(requireActivity())
                        }
                        else -> {
                        }
                    }
                }
            }
        }
    }

}