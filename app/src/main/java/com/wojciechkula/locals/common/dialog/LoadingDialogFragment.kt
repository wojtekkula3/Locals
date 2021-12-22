package com.wojciechkula.locals.common.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.wojciechkula.locals.R

class LoadingDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false

        return inflater.inflate(R.layout.layout_loading_dialog, container, false)
    }

    companion object {
        private const val TAG = "loading_dialog_fragment"

        fun toggle(fragmentManager: FragmentManager, show: Boolean) {
            fragmentManager.executePendingTransactions()
            val loadingFragment = findIn(fragmentManager)
            when {
                show && loadingFragment == null -> show(fragmentManager)
                !show -> loadingFragment?.dismiss()
            }
        }

        private fun findIn(fragmentManager: FragmentManager): LoadingDialogFragment? =
            fragmentManager.findFragmentByTag(TAG) as LoadingDialogFragment?

        private fun show(fragmentManager: FragmentManager): LoadingDialogFragment {
            val fragment = LoadingDialogFragment()
            fragment.show(fragmentManager, TAG)
            return fragment
        }
    }
}