package com.github.lzaytseva.kinopoiskapitest.presentation.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.github.lzaytseva.kinopoiskapitest.R
import java.util.Calendar

class YearPickerDialog(
    private val onYearChosen: (String?) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layoutInflater = requireActivity().layoutInflater
        val view = layoutInflater.inflate(R.layout.year_picker_dialog, null)

        val pickerFrom = view.findViewById<NumberPicker>(R.id.picker_from)
        val pickerTo = view.findViewById<NumberPicker>(R.id.picker_to)
        val reset = view.findViewById<TextView>(R.id.btn_reset)
        val apply = view.findViewById<TextView>(R.id.btn_apply)


        val calendar = Calendar.getInstance()
        val maxYear = calendar.get(Calendar.YEAR)

        val displayedValuesFrom = mutableListOf<String>().apply {
            add("с")
        }
        val displayedValuesTo = mutableListOf<String>().apply {
            add("по")
        }
        for (year in MIN_YEAR..maxYear) {
            displayedValuesFrom.add(year.toString())
            displayedValuesTo.add(year.toString())
        }

        pickerFrom.maxValue = displayedValuesFrom.lastIndex
        pickerFrom.minValue = 0
        pickerFrom.displayedValues = displayedValuesFrom.toTypedArray()
        pickerTo.displayedValues = displayedValuesTo.toTypedArray()
        pickerTo.minValue = 0
        pickerTo.maxValue = displayedValuesTo.lastIndex

        reset.setOnClickListener {
            pickerTo.value = 0
            pickerFrom.value = 0
        }

        apply.setOnClickListener {
            val start = displayedValuesFrom[pickerFrom.value]
            val end = displayedValuesTo[pickerTo.value]
            val range = when {
                start == "с" && end == "по" -> {
                    null
                }

                start == "с" -> {
                    "$MIN_YEAR-${end.toInt()}"
                }

                end == "по" -> {
                    "$start-$maxYear"
                }

                else -> {
                    "${start.toInt().coerceAtMost(end.toInt())}-${
                        start.toInt().coerceAtLeast(end.toInt())
                    }"
                }
            }
            onYearChosen.invoke(range)
            dismiss()
        }


        val builder = AlertDialog.Builder(requireContext())
            .apply {
                setView(view)
            }
        return builder.create()
    }


    companion object {
        private const val MIN_YEAR = 1888
    }
}