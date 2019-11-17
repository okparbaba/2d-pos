package wla.googoodolls.specialcalculator.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.doAsync
import wla.googoodolls.specialcalculator.R
import wla.googoodolls.specialcalculator.database.DatabaseClient
import wla.googoodolls.specialcalculator.database.DatabaseClient.Companion.getInstance

/**
 * A simple [Fragment] subclass.
 */
class HoteListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hote_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doAsync {
            val db = getInstance(activity!!).appDatabase
            val list = db.userDao().getUserByDate("2019-11-17 21:24:41.837","2019-11-17 00:00:00")
            for (j in list) Log.e("data", "${j.date} : ${j.name}")
        }
//        doAsync {
//            val data = getInstance(activity!!).appDatabase
//                .userDataDao().getAllUsersData()
//            for (j in data) Log.e("data", "${j.user_id} : ${j.cell}")
//        }

    }


}
