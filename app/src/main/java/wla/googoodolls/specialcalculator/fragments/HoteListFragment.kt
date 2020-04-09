package wla.googoodolls.specialcalculator.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_hote_list.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import wla.googoodolls.specialcalculator.R
import wla.googoodolls.specialcalculator.adapter.HtoeListAdapter
import wla.googoodolls.specialcalculator.database.DatabaseClient.Companion.getInstance
import wla.googoodolls.specialcalculator.database.repos.User
import wla.googoodolls.specialcalculator.database.repos.UserData
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class HoteListFragment : Fragment() {
    private lateinit var htoeListAdapter: HtoeListAdapter
    private lateinit var list:ArrayList<Pair<User,ArrayList<UserData>>>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hote_list, container, false)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list = ArrayList()
        htoeListAdapter = HtoeListAdapter(list)
        rvHoteList?.layoutManager = LinearLayoutManager(activity)
        rvHoteList?.adapter = htoeListAdapter
        val today: String
        today = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            current.format(formatter)
        } else {
            val sdf = SimpleDateFormat("dd-M-yyyy")
            sdf.format(Date())
        }

        getUser(today)
       // Log.e("datadata2",today)
        btByDate?.setOnClickListener {
            list.clear()
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(activity!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in textbox
                //lblDate.setText("" + dayOfMonth + " " + MONTHS[monthOfYear] + ", " + year)
                //getUser("$dayOfMonth-${monthOfYear+1}-$year")
                val d = when(dayOfMonth){
                    in 1..9-> "0$dayOfMonth"
                    else->"$dayOfMonth"
                }
                val m = when(monthOfYear){
                    in 0..9->"0${monthOfYear+1}"
                    else->"$monthOfYear"
                }
                //Log.e("datadata2","$d-$m-$year")
                getUser("$d-$m-$year")
            }, year, month, day)
            dpd.show()
        }
        btToday?.setOnClickListener {
            list.clear()
            getUser(today)
        }
    }
    private fun getUser(date:String){
        list.clear()
        class GetUser:AsyncTask<Void,Void,List<User>>(){
            override fun doInBackground(vararg params: Void?): List<User> {
                val db = getInstance(activity!!).appDatabase
                //return db.userDao().getUserByDate("$date 23:59:59","09-04-2020 01:00:00")
                return db.userDao().getUserByDate("$date 23:59:59","$date 01:00:00")
            }

            override fun onPostExecute(result: List<User>?) {
                super.onPostExecute(result)
                result?.let {
                    for (i in result as ArrayList){
                        getUserDatas(i)
                    }
                }

            }

        }
        GetUser().execute()
    }
    private fun getUserDatas(user:User){
        class GetUserDatas:AsyncTask<Void,Void,List<UserData>>(){
            override fun doInBackground(vararg params: Void?): List<UserData> {
                val db = getInstance(activity!!).appDatabase
                return db.userDataDao().getByUser(user.id)
            }

            override fun onPostExecute(result: List<UserData>?) {
                super.onPostExecute(result)
                result?.let {
                    val p = Pair(user,result as ArrayList)
                    list.add(p)
                    htoeListAdapter.notifyDataSetChanged()
                }

            }
        }
        GetUserDatas().execute()
    }
}
