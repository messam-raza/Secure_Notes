package com.messamraza.securenotes

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.messamraza.securenotes.databinding.DialogPinBinding

class PinDialogFragment(
    private val onPinEntered: (String) -> Unit
) : DialogFragment() {

    private var _binding: DialogPinBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogPinBinding.inflate(layoutInflater)

        return AlertDialog.Builder(requireContext())
            .setTitle("Enter PIN")
            .setView(binding.root)
            .setPositiveButton("OK") { _, _ ->
                val pin = binding.etPin.text.toString()
                onPinEntered(pin)
            }
            .setNegativeButton("Cancel", null)
            .create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
