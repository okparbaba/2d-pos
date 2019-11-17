package wla.googoodolls.specialcalculator.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import wla.googoodolls.specialcalculator.R

/**
 * A simple [Fragment] subclass.
 */
class PoutListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pout_list, container, false)
    }
}