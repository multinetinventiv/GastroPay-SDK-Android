package com.inventiv.gastropaysdk.ui.common.singleselectiondialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.inventiv.gastropaysdk.databinding.LayoutSingleSelectionBottomSheetGastropaySdkBinding

internal class CommonSingleItemSelectionDialogFragment<T : SingleItemSelectionModel>(
    private val itemClick: (item: T, dialog: CommonSingleItemSelectionDialogFragment<T>) -> Unit
) : BottomSheetDialogFragment() {

    companion object {
        const val ARG_SINGLE_ITEM_SELECTION_DIALOG_ITEM_LIST =
            "arg_single_item_selection_dialog_item_list"

        fun <T : SingleItemSelectionModel> newInstance(
            itemList: ArrayList<T>,
            itemClick: (item: T, dialog: CommonSingleItemSelectionDialogFragment<T>) -> Unit
        ) =
            CommonSingleItemSelectionDialogFragment(itemClick).apply {
                val args = Bundle().apply {
                    putParcelableArrayList(ARG_SINGLE_ITEM_SELECTION_DIALOG_ITEM_LIST, itemList)
                }
                arguments = args
            }
    }

    private var _binding: LayoutSingleSelectionBottomSheetGastropaySdkBinding? = null
    private val binding get() = _binding!!

    private lateinit var items: ArrayList<T>
    private val singleItemSelectionAdapter: SingleItemSelectionAdapter<T>
        get() = SingleItemSelectionAdapter(items) { item ->
            dismiss()
            itemClick.invoke(item, this)
        }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            LayoutSingleSelectionBottomSheetGastropaySdkBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        items = arguments?.getParcelableArrayList(ARG_SINGLE_ITEM_SELECTION_DIALOG_ITEM_LIST)
            ?: arrayListOf()

        prepareViews()
    }

    private fun prepareViews() {
        binding.recyclerSingleSelectionItems.apply {
            setHasFixedSize(true)
            adapter = singleItemSelectionAdapter
            itemAnimator = DefaultItemAnimator()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}