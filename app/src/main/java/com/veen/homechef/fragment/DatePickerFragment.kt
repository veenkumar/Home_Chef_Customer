package com.veen.homechef.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.veen.homechef.Activity.profile.MyOrder
import java.util.*

class DatePickerFragment : DialogFragment(),
    DatePickerDialog.OnDateSetListener {
    private var date: StringBuilder? = null
    private var day: Int? = null
    private var setOnDateOfBirth: SetOnDateOfBirth? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        day = c.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(requireContext(), this, year, month, day!!-0)
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        val userAge: Calendar = GregorianCalendar(year, month, day)
        val minAdultAge: Calendar = GregorianCalendar()
        minAdultAge.add(Calendar.DAY_OF_MONTH, -1)
        if (minAdultAge.before(userAge)) {
            Toast.makeText(
                requireContext(),
                "Your Date Should Be Less Than Current Date!",
                Toast.LENGTH_LONG
            )
                .show()
        } else {
            date = StringBuilder().append(year)
                .append("-").append(month + 1).append("-").append(day)
                .append(" ")
            Log.d("DatePickerFragment", "" + date)
            setOnDateOfBirth?.setDateOfBirth(date!!)
        }
    }

    fun setOnDateOfBirth(setOnDateOfBirth: MyOrder) {
        this.setOnDateOfBirth = setOnDateOfBirth
    }

    interface SetOnDateOfBirth {
        fun setDateOfBirth(date: StringBuilder?)
    }
}