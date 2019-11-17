package wla.googoodolls.specialcalculator.navfragment


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.fragment_two_d.*
import kotlinx.android.synthetic.main.toolbar.*
import wla.googoodolls.specialcalculator.MyBounceInterpolator

import wla.googoodolls.specialcalculator.R
import wla.googoodolls.specialcalculator.fragments.*

/**
 * A simple [Fragment] subclass.
 */
class TwoDFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btHoteList -> {
                didTapButton(btHoteList)
                loadFragment(HoteListFragment())
            }
            R.id.btHtwetList -> {
                didTapButton(btHtwetList)
                loadFragment(HtwetListFragment())
            }
            R.id.btPoutList -> {
                didTapButton(btPoutList)
                loadFragment(PoutListFragment())
            }
            R.id.btTotalList -> {
                didTapButton(btTotalList)
                loadFragment(TotalFragment())
            }
            R.id.btHtoemal -> {
                didTapButton(btHtoemal)
                loadFragment(HtoemalFragment())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_two_d, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvToolbar?.text = "2D"
        btHoteList.setOnClickListener(this)
        btHtwetList.setOnClickListener(this)
        btPoutList.setOnClickListener(this)
        btTotalList.setOnClickListener(this)
        btHtoemal.setOnClickListener(this)
    }
    fun didTapButton(view: View) {
        val myAnim = AnimationUtils.loadAnimation(activity, R.anim.bounce)
        // Use bounce interpolator with amplitude 0.2 and frequency 20
        val interpolator = MyBounceInterpolator(0.2, 20.0)
        myAnim.interpolator = interpolator

        view.startAnimation(myAnim)
        vibrate()
    }

    private fun vibrate() {
        val v = activity!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(50)
        }
    }
    private fun loadFragment(f:Fragment){
        activity!!.supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_up,R.anim.slide_down)
            .add(R.id.container,f)
            .addToBackStack(null)
            .commit()
    }

}
