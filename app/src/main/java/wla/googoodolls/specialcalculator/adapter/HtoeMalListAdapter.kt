package wla.googoodolls.specialcalculator.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.twod_htoemal_item.view.*
import wla.googoodolls.specialcalculator.R
import wla.googoodolls.specialcalculator.model.Htoemal

class HtoeMalListAdapter(
    val l: ArrayList<Htoemal>,
    val edit:(vh: ViewHolder, p:Int)->Unit,
    val delete:(vh: ViewHolder, p:Int)->Unit
) : RecyclerView.Adapter<HtoeMalListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.twod_htoemal_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = l.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.no.setText(l[position].no)
        holder.amount.setText(l[position].amount)
        holder.itemView.ibtEdit.setOnClickListener { edit(holder,position)}
        holder.itemView.ibtDelete.setOnClickListener { delete(holder,position) }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val no = v.no
        val amount = v.amount
    }

}