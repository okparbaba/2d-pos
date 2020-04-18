package wla.googoodolls.specialcalculator.fragments


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.*
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_htoemal.*
import kotlinx.android.synthetic.main.twod_htoemal_item.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.toast
import wla.googoodolls.specialcalculator.MyBounceInterpolator
import wla.googoodolls.specialcalculator.R
import wla.googoodolls.specialcalculator.adapter.HtoeMalListAdapter
import wla.googoodolls.specialcalculator.database.DatabaseClient.Companion.getInstance
import wla.googoodolls.specialcalculator.database.repos.User
import wla.googoodolls.specialcalculator.database.repos.UserData
import wla.googoodolls.specialcalculator.model.Htoemal
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class HtoemalFragment : Fragment(), View.OnClickListener {
    private lateinit var list: ArrayList<Htoemal>
    private lateinit var manager: LinearLayoutManager
    private lateinit var adapter: HtoeMalListAdapter
    private var next = true
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.ibtAdd -> {
                didTapButton(ibtAdd)
                addSquare()
            }
            R.id.btConfirm -> {
                didTapButton(btConfirm)
                var str = ""
                var total = 0
                for (i in list) {
                    str += "${i.no} \t ${i.amount} ကျပ်\n"
                    total += i.amount!!.toInt()
                }
                AlertDialog.Builder(activity)
                    .setTitle("ဘောက်ချာပေးမှာသေချာသည်?")
                    .setMessage("ထိုးထားသည့်အကွက်များစစ်ပါ\nအကွက်\tပမာ\n$str\nစုစုပေါင်း\t $total ကျပ်")
                    .setIcon(R.drawable.success)
                    .setPositiveButton(android.R.string.yes) { _, _ ->
                        insertTo()
                        //Snackbar.make(view!!, "Printing", Snackbar.LENGTH_LONG).show()
                    }
                    .setNegativeButton(android.R.string.no, null)
                    .show()
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun insertTo() {
        val name = etName.text.toString()
        val debtStatus = chDebtStatus.isChecked
        if (TextUtils.isEmpty(name)) {
            etName.error = "နံမည်ရေးအုံး"
            return
        }
        val saveDate: String
        val ampm:String
        saveDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
            val formatter2 = DateTimeFormatter.ofPattern("HH:mm:ss")
            ampm = current.format(formatter2)
            current.format(formatter)
        } else {
            val sdf = SimpleDateFormat("dd-M-yyyy hh:mm:ss")
            val sdf2 = SimpleDateFormat("hh:mm:ss")
            ampm = sdf2.format(Date())
            sdf.format(Date())
        }

        class SaveUser : AsyncTask<Void, Void, Long>() {
            override fun doInBackground(vararg p0: Void?): Long {
                val user = User()
                user.name = name
                user.date = saveDate
                user.debt_status = debtStatus
                user.lottery_status = false
                if (ampm<"11:59:00"){
                    user.ampm = "am"
                }else user.ampm = "pm"
                val db = getInstance(activity!!).appDatabase

                return db.userDao().insertUser(user)

            }

            override fun onPostExecute(result: Long?) {
                super.onPostExecute(result)
                toast("Saved User")
                class SaveUserData : AsyncTask<Void, Void, Void>() {
                    var id: Int? = 0
                    override fun doInBackground(vararg p0: Void?): Void? {
                        val db = getInstance(activity!!).appDatabase

                        id = result?.toInt()
                        //Log.e("userId", id.toString())
                        var i = 0
                        while (i < list.size) {
                            val userData = UserData()
                            val (no, amount) = list[i]
                            userData.user_id = id
                            userData.cell = no
                            userData.amount = amount
                            userData.date = saveDate
                            if (ampm<"11:59:00"){
                                userData.ampm = "am"
                            }else userData.ampm = "pm"
                            doAsync {
                                db.userDataDao().insertUserData(userData)
                            }
                            i++
                        }
                        return null
                    }

                    override fun onPostExecute(result: Void?) {
                        super.onPostExecute(result)
                        toast("Saved UserData")
                        list.clear()
                        adapter.notifyDataSetChanged()
                    }
                }
                SaveUserData().execute()
            }
        }
        SaveUser().execute()
    }

    private fun addSquare() {
        val no = etNo.text.toString()
        val amount = etAmount.text.toString()
        val style = spStyle.selectedItemPosition
        if (style !in 4..6){
            if (TextUtils.isEmpty(no)) {
                etNo.error = "Fill"
                etNo.requestFocus()
                return
            }
        }


        if (TextUtils.isEmpty(amount)) {
            etAmount.error = "Fill"
            etAmount.requestFocus()
            return
        }
        when (style) {
            //simple
            0 -> {
                if (no[0] != no[1]){
                    val htoemal = Htoemal(no, amount)
                    val htoemal1 = Htoemal(no.reversed(),amount)
                    list.add(htoemal)
                    list.add(htoemal1)
                }else{
                    val htoemal = Htoemal(no,amount)
                    list.add(htoemal)
                }

            }
            //pat
            1 -> {
                val num = no[0]
                for (i in 0..9) {
                    val htoemal = Htoemal("$num$i", amount)
                    list.add(htoemal)
                }
                for (i in 0..9) {
                    if ((i.toString() == num.toString())) continue
                    val htoemal = Htoemal("$i$num", amount)
                    list.add(htoemal)
                }

            }
            //htate
            2 -> {
                val num = no[0]
                for (i in 0..9) {
                    val htoemal = Htoemal("$num$i", amount)
                    list.add(htoemal)
                }
            }
            //pate
            3 -> {
                val num = no[0]
                for (i in 0..9) {
                    val htoemal = Htoemal("$i$num", amount)
                    list.add(htoemal)
                }
            }
            //Natka
            4 -> {
                val htoemal0 = Htoemal("18", amount)
                val htoemal1 = Htoemal("70", amount)
                val htoemal2 = Htoemal("24", amount)
                val htoemal3 = Htoemal("35", amount)
                val htoemal4 = Htoemal("96", amount)
                list.addAll(listOf(htoemal0,htoemal1,htoemal2,htoemal3,htoemal4))
            }
            //Power
            5 -> {
                val htoemal0 = Htoemal("05", amount)
                val htoemal1 = Htoemal("16", amount)
                val htoemal2 = Htoemal("27", amount)
                val htoemal3 = Htoemal("38", amount)
                val htoemal4 = Htoemal("94", amount)
                list.addAll(listOf(htoemal0,htoemal1,htoemal2,htoemal3,htoemal4))
            }
            //Apuu
            6 -> {
                val htoemal0 = Htoemal("00", amount)
                val htoemal1 = Htoemal("11", amount)
                val htoemal2 = Htoemal("22", amount)
                val htoemal3 = Htoemal("33", amount)
                val htoemal5 = Htoemal("44", amount)
                val htoemal6 = Htoemal("55", amount)
                val htoemal7 = Htoemal("66", amount)
                val htoemal8 = Htoemal("77", amount)
                val htoemal9 = Htoemal("88", amount)
                val htoemal4 = Htoemal("99", amount)
                list.addAll(listOf(htoemal0,htoemal1,htoemal2,htoemal3,htoemal4,htoemal5,htoemal6,htoemal7,htoemal8,htoemal9))
            }
        }


        adapter.notifyDataSetChanged()
    }

    var marginTop = 90
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
            { vh, p ->
                didTapButton(vh.itemView.ibtEdit)
                if (next) {
                    vh.itemView.no.requestFocus()
                    vh.itemView.ibtEdit.setImageResource(R.drawable.success)
                    next = false
                } else {
                    AlertDialog.Builder(activity)
                        .setTitle("သိမ်းမယ်")
                        .setMessage("အကွက်ကိုသိမ်းမှာလား?")
                        .setIcon(R.drawable.success)
                        .setPositiveButton(android.R.string.yes) { _, _ ->
                            val htoemal = Htoemal(vh.no.text.toString(), vh.amount.text.toString())
                            list[p] = htoemal
                            vh.itemView.ibtEdit.setImageResource(R.drawable.edit)
                            adapter.notifyDataSetChanged()
                            etNo.requestFocus()
                            next = true

                        }
                        .setNegativeButton(android.R.string.no, null)
                        .show()
                }
            },
            { vh, p ->
                didTapButton(vh.itemView.ibtDelete)
                AlertDialog.Builder(activity)
                    .setTitle("ဖျက်မယ်")
                    .setMessage("အကွက်ကိုဖျက်မှာလား?")
                    .setIcon(R.drawable.delete)
                    .setPositiveButton(android.R.string.yes) { _, _ ->
                        list.remove(list[p])
                        adapter.notifyDataSetChanged()
                    }
                    .setNegativeButton(android.R.string.no, null)
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
