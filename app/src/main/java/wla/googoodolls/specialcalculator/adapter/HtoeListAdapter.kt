package wla.googoodolls.specialcalculator.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.htoe_list.view.*
import wla.googoodolls.specialcalculator.R
import wla.googoodolls.specialcalculator.database.repos.User
import wla.googoodolls.specialcalculator.database.repos.UserData

class HtoeListAdapter(val list:ArrayList<Pair<User,ArrayList<UserData>>>) :RecyclerView.Adapter<HtoeListAdapter.Holder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.htoe_list,parent,false))
    }

    override fun getItemCount() = list.size
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val (user,userData) = list[position]
        with(holder){
            name.text = user.name
            var nums = ""
            var t = 0
            for (i in userData) {
                nums+="${i.cell} - ${i.amount} \n"
                t+=i.amount!!.toInt()
            }
            num.text = nums
            total.text = t.toString()
            /*val blink = AlphaAnimation(0.0f,1.0f)
            blink.duration = 500
            blink.startOffset = 20
            blink.repeatMode = Animation.REVERSE
            blink.repeatCount = Animation.INFINITE
            debt.startAnimation(blink)*/
            if (user.debt_status!!){
                debt.visibility = VISIBLE
            } else debt.visibility = GONE
        }
    }
    class Holder(v:View) :RecyclerView.ViewHolder(v){
        val name = v.tvName
        val num = v.tvNumber
        val total = v.tvTotal
        val debt = v.tvDebtStatus
    }

}