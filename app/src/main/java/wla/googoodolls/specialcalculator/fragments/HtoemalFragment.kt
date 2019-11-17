package wla.googoodolls.specialcalculator.fragments


import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.marginTop
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_htoemal.*
import kotlinx.android.synthetic.main.fragment_htoemal.ibtAdd
import kotlinx.android.synthetic.main.fragment_htoemal.view.*
import kotlinx.android.synthetic.main.twod_htoemal_item.view.*
import wla.googoodolls.specialcalculator.MyBounceInterpolator
import wla.googoodolls.specialcalculator.R
import wla.googoodolls.specialcalculator.adapter.HtoeMalListAdapter
import wla.googoodolls.specialcalculator.model.Htoemal

/**
 * A simple [Fragment] subclass.
 */
class HtoemalFragment : Fragment(), View.OnClickListener {
    private lateinit var list: ArrayList<Htoemal>
    private lateinit var manager:LinearLayoutManager
    private lateinit var adapter:HtoeMalListAdapter
    private var next = true
    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.ibtAdd->{
                didTapButton(ibtAdd)
                addSquare()
            }
            R.id.btConfirm->{
                didTapButton(btConfirm)
                var str = ""
                for (i in list){
                    str+="${i.no} \t ${i.amount} \n"
                }
                AlertDialog.Builder(activity)
                    .setTitle("ဘောက်ချာပေးမှာသေချာသည်?")
                    .setMessage("ထိုးထားသည့်အကွက်များစစ်ပါ\n အကွက်\t ပမာန\n$str")
                    .setIcon(R.drawable.success)
                    .setPositiveButton(android.R.string.yes){
                            _, _ ->
                        Snackbar.make(view!!,"Printing",Snackbar.LENGTH_LONG).show()
                    }
                    .setNegativeButton(android.R.string.no,null)
                    .show()
            }
        }
    }

    private fun addSquare() {
        val no = etNo.text.toString()
        val amount = etAmount.text.toString()

        if (TextUtils.isEmpty(no)){
            etNo.error = "Fill"
            etNo.requestFocus()
            return
        }

        if (TextUtils.isEmpty(amount)){
            etAmount.error = "Fill"
            etAmount.requestFocus()
            return
        }

        val htoemal = Htoemal(no,amount)
        list.add(htoemal)
        adapter.notifyDataSetChanged()
    }

    var marginTop  = 90
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_htoemal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ibtAdd.setOnClickListener(this)
        btConfirm.setOnClickListener(this)
        list = ArrayList()
        manager = LinearLayoutManager(activity)
        adapter = HtoeMalListAdapter(list,
            {
                vh, p ->
                didTapButton(vh.itemView.ibtEdit)
                if (next){
                    vh.itemView.no.requestFocus()
                    vh.itemView.ibtEdit.setImageResource(R.drawable.success)
                    next = false
                }else{
                    AlertDialog.Builder(activity)
                        .setTitle("သိမ်းမယ်")
                        .setMessage("အကွက်ကိုသိမ်းမှာလား?")
                        .setIcon(R.drawable.success)
                        .setPositiveButton(android.R.string.yes){
                                _, _ ->
                            val htoemal = Htoemal(vh.no.text.toString(),vh.amount.text.toString())
                            list[p] = htoemal
                            vh.itemView.ibtEdit.setImageResource(R.drawable.edit)
                            adapter.notifyDataSetChanged()
                            etNo.requestFocus()
                            next = true

                        }
                        .setNegativeButton(android.R.string.no,null)
                        .show()
                }
            },
            {
                vh, p ->
                didTapButton(vh.itemView.ibtDelete)
                AlertDialog.Builder(activity)
                    .setTitle("ဖျက်မယ်")
                    .setMessage("အကွက်ကိုဖျက်မှာလား?")
                    .setIcon(R.drawable.delete)
                    .setPositiveButton(android.R.string.yes){
                            _, _ ->
                        list.remove(list[p])
                        adapter.notifyDataSetChanged()
                    }
                    .setNegativeButton(android.R.string.no,null)
                    .show()

            })

        rv.layoutManager = manager
        rv.adapter = adapter
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
}
