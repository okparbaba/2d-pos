package wla.googoodolls.specialcalculator.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_total.*
import wla.googoodolls.specialcalculator.R
import wla.googoodolls.specialcalculator.adapter.TotalListAdapter
import wla.googoodolls.specialcalculator.database.DatabaseClient
import wla.googoodolls.specialcalculator.database.repos.UserData
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
@Suppress("IMPLICIT_CAST_TO_ANY")
class TotalFragment : Fragment() {
    private lateinit var list:ArrayList<Pair<String, ArrayList<String>>>
    private lateinit var amList:ArrayList<Pair<String, ArrayList<String>>>
    private lateinit var pmList:ArrayList<Pair<String, ArrayList<String>>>
    private lateinit var adapter:TotalListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_total, container, false)
    }

    @SuppressLint("SimpleDateFormat", "SdCardPath")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list = ArrayList()
        amList = ArrayList()
        pmList = ArrayList()
        adapter = TotalListAdapter(list)
        rvHoteList?.layoutManager = LinearLayoutManager(activity)
        rvHoteList?.adapter = adapter
        val today: String
        today = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            current.format(formatter)
        } else {
            val sdf = SimpleDateFormat("dd-M-yyyy")
            sdf.format(Date())
        }
        getDataNum(today)
        progressBar?.visibility = VISIBLE
        Handler().postDelayed(
            {
                progressBar?.visibility = GONE
                if (spinner?.selectedItem=="မနက်"){
                    list.addAll(amList)
                    adapter.notifyDataSetChanged()
                }else if(spinner?.selectedItem=="ညနေ"){
                    list.addAll(pmList)
                    adapter.notifyDataSetChanged()
                }
            },1000
        )

        btToday?.setOnClickListener {
            list.clear()
            getDataNum(today)
        }
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
                getDataNum("$d-$m-$year")
            }, year, month, day)
            dpd.show()
        }
        btSend?.setOnClickListener {
            val file = File("/sdcard/2D")
            if (!file.exists()){
                file.mkdir()
            }
            try {
                val gpxfile = File(file,"$today.txt")
                val writer = FileWriter(gpxfile)
                writer.append(today)
                for (i in list){
                    var t = 0
                    for (i in i.second){
                        t += i.toInt()
                    }
                    writer.append("${i.first} - $t\n")
                }
                writer.flush()
                writer.close()
                Toast.makeText(activity, "Saved your text", Toast.LENGTH_LONG).show()
            }catch (e:Exception){}
            finally {
                val f = File("/sdcard/2D/$today.txt")
                if (f.exists()){
                    val intent = Intent(Intent.ACTION_SEND)
                    val uri = Uri.fromFile(f)
                    intent.data = uri
                    Intent.createChooser(intent,"Select to Send")
                }
            }
        }

        spinner?.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                list.clear()
                if (position==0){
                    list.addAll(amList)
                }else if(position==1){
                    list.addAll(pmList)
                }
                adapter.notifyDataSetChanged()
            }

        }
    }
    private fun getDataNum(date:String){
        for (i in 0..99){
            val n = when(i){
                in 0..9-> "0$i"
                else->i
            }
            getDataAm(date,n.toString())
        }

    }
    private fun getDataAm(date:String,num:String){
        list.clear()
        class GetUser: AsyncTask<Void, Void, List<UserData>>(){
            override fun doInBackground(vararg params: Void?): List<UserData> {
                val db = DatabaseClient.getInstance(activity!!).appDatabase
                //return db.userDao().getUserByDate("$date 23:59:59","09-04-2020 01:00:00")
                return db.userDataDao().getDataByDate("$date 23:59:59","$date 01:00:00",num)
            }

            override fun onPostExecute(result: List<UserData>?) {
                super.onPostExecute(result)
                result?.let {
                    val am = ArrayList<String>()
                    val pm = ArrayList<String>()
                    for (i in result){
                        if (i.ampm=="am"){
                            am.add(i.amount!!)
                        }else if (i.ampm=="pm"){
                            pm.add(i.amount!!)
                        }
                        Log.e("celll","${i.cell} ${i.amount}")
                    }
                    val d1 = Pair(num,am)
                    val d2 = Pair(num,pm)
                    amList.add(d1)
                    pmList.add(d2)
                }

            }

        }
        GetUser().execute()
    }



}