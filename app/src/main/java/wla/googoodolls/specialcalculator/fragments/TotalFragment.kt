package wla.googoodolls.specialcalculator.fragments

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import wla.googoodolls.specialcalculator.R
import wla.googoodolls.specialcalculator.adapter.TotalListAdapter
import wla.googoodolls.specialcalculator.database.DatabaseClient
import wla.googoodolls.specialcalculator.database.repos.User
import wla.googoodolls.specialcalculator.database.repos.UserData
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class TotalFragment : Fragment() {
    private lateinit var list:ArrayList<Pair<String, ArrayList<String>>>
    private lateinit var adapter:TotalListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_total, container, false)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list = ArrayList()
        adapter = TotalListAdapter(list)
        val today: String
        today = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            current.format(formatter)
        } else {
            val sdf = SimpleDateFormat("dd-M-yyyy")
            sdf.format(Date())
        }
        for (i in 0..99){
            val n = when(i){
                in 0..9-> "0$i"
                else->i
            }
            getData(today,"00")
        }


    }
    private fun getData(date:String,num:String){
        list.clear()
        class GetUser: AsyncTask<Void, Void, List<String>>(){
            override fun doInBackground(vararg params: Void?): List<String> {
                val db = DatabaseClient.getInstance(activity!!).appDatabase
                //return db.userDao().getUserByDate("$date 23:59:59","09-04-2020 01:00:00")
                return db.userDataDao().getDataByDate("$date 23:59:59","$date 01:00:00",num)
            }

            override fun onPostExecute(result: List<String>?) {
                super.onPostExecute(result)
                result?.let {
                    val d = Pair(num,result as ArrayList)
                    list.add(d)
                }

            }

        }
        GetUser().execute()
    }

}