package wla.googoodolls.specialcalculator.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.total_list.view.*
import wla.googoodolls.specialcalculator.R

class TotalListAdapter(val list: ArrayList<Pair<String, ArrayList<String>>>) :
    RecyclerView.Adapter<TotalListAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.total_list,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.size
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val (number,tAmount) = list[position]
        with(holder) {
            num.text = number
            var t = 0
            for (i in tAmount){
                t += i.toInt()
            }
            amount.text = t.toString()
        }
    }

    class Holder(v: View) : RecyclerView.ViewHolder(v) {
        val num = v.tvNum
        val amount = v.tvAmount
    }
}