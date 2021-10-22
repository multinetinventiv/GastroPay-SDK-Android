package com.inventiv.gastropaysdk.ui.settings.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inventiv.gastropaysdk.databinding.ItemNotificationPermissionHeaderGastropaySdkBinding
import com.inventiv.gastropaysdk.databinding.ItemSwitchGastropaySdkBinding

internal class NotificationPreferencesAdapter(
    private val notifications: ArrayList<NotificationPreferencesBase>,
    private val channelStatedChanged: (id: Int, channel: Int, newState: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }

    override fun getItemCount(): Int = notifications.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_HEADER) {
            NotificationHeaderViewHolder(
                ItemNotificationPermissionHeaderGastropaySdkBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )
        } else {
            NotificationItemViewHolder(
                ItemSwitchGastropaySdkBinding.inflate(
                    inflater,
                    parent,
                    false
                ), channelStatedChanged
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NotificationHeaderViewHolder -> holder.bind(notifications[position] as NotificationPreferencesBase.NotificationPreferencesHeader)
            is NotificationItemViewHolder -> holder.bind(
                notifications[position] as NotificationPreferencesBase.NotificationPreferencesItem,
                position == notifications.lastIndex
            )
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (notifications[position] is NotificationPreferencesBase.NotificationPreferencesHeader) TYPE_HEADER else TYPE_ITEM
    }
}

class NotificationHeaderViewHolder(val binding: ItemNotificationPermissionHeaderGastropaySdkBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(notificationHeader: NotificationPreferencesBase.NotificationPreferencesHeader) {
        binding.textNotificationHeaderGastroPaySdk.text = notificationHeader.label
    }

}

class NotificationItemViewHolder(
    val binding: ItemSwitchGastropaySdkBinding,
    private val channelStatedChanged: (id: Int, channel: Int, newState: Int) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        notificationItem: NotificationPreferencesBase.NotificationPreferencesItem,
        isLastIndex: Boolean
    ) {
        binding.switchNotification.text = notificationItem.preferencesChannel?.name
        binding.switchNotification.isChecked =
            notificationItem.preferencesState?.equals(NotificationPreferencesStateType.ON)!!
        if (isLastIndex) {
            binding.viewDivider.visibility = View.GONE
        }
        binding.switchNotification.setOnCheckedChangeListener { buttonView, isChecked ->
            val stateValue =
                if (isChecked) NotificationPreferencesStateType.ON else NotificationPreferencesStateType.OFF
            val channelValue = notificationItem.preferencesChannel?.value
            channelStatedChanged.invoke(
                notificationItem.id!!,
                channelValue?.toInt()!!,
                stateValue.value.toInt()
            )
        }
    }
}