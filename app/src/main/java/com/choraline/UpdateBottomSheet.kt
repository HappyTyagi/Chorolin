package com.choraline

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class UpdateBottomSheet : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_update, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnUpdateNow = view.findViewById<Button>(R.id.btnUpdateNow)
        btnUpdateNow.setOnClickListener {
            val updateUrl = "https://play.google.com/store/apps/details?id=your.package.name"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl))
            startActivity(intent)
            dismiss()
        }
    }
}
