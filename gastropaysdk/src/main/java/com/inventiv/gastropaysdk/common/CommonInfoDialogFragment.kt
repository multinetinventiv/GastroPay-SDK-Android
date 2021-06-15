package com.inventiv.gastropaysdk.common

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.inventiv.gastropaysdk.R
import com.inventiv.gastropaysdk.databinding.FragmentCommonDialogGastropaySdkBinding
import com.inventiv.gastropaysdk.utils.delegate.viewBinding


class CommonInfoDialogFragment(
    private val dismissed: () -> Unit,
    private val buttonClick: (dialog: CommonInfoDialogFragment) -> Unit
) : DialogFragment(R.layout.fragment_common_dialog_gastropay_sdk) {

    private val PARAM_IMAGE_RESID = "PARAM_IMAGE_RESID"
    private val PARAM_TITLE = "PARAM_TITLE"
    private val PARAM_DESCRIPTION = "PARAM_DESCRIPTION"
    private val PARAM_BUTTON_TEXT = "PARAM_BUTTON_TEXT"

    companion object {
        fun newInstance(
            resImageId: Int,
            title: String,
            description: String,
            buttonText: String,
            dismissed: () -> Unit,
            buttonClick: (dialog: CommonInfoDialogFragment) -> Unit
        ) =
            CommonInfoDialogFragment(dismissed, buttonClick).apply {
                val args = Bundle().apply {
                    putInt(PARAM_IMAGE_RESID, resImageId)
                    putString(PARAM_TITLE, title)
                    putString(PARAM_DESCRIPTION, description)
                    putString(PARAM_BUTTON_TEXT, buttonText)
                }
                arguments = args
            }
    }

    private val binding by viewBinding(FragmentCommonDialogGastropaySdkBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.apply {
            getInt(PARAM_IMAGE_RESID).let { binding.imageInfoDialog.setImageResource(it) }
            getString(PARAM_TITLE)?.let { binding.titleTextView.text = it }
            getString(PARAM_BUTTON_TEXT).let { binding.materialButton.text = it }
            getString(PARAM_DESCRIPTION).let {
                if (!it.isNullOrEmpty()) {
                    binding.descTextView.visibility = View.VISIBLE
                    binding.descTextView.text = it
                }
            }
        }
        binding.materialButton.setOnClickListener { buttonClick.invoke(this) }
        binding.closeButton.setOnClickListener { dismiss() }
    }

    override fun onDismiss(dialog: DialogInterface) {
        dismissed.invoke()
        super.onDismiss(dialog)
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = LinearLayout.LayoutParams.MATCH_PARENT
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }
}