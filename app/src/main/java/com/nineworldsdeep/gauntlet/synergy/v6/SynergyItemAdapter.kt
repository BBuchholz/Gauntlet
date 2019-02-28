package com.nineworldsdeep.gauntlet.synergy.v6
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nineworldsdeep.gauntlet.R

class SynergyItemAdapter internal constructor(context: Context) : RecyclerView.Adapter<SynergyItemAdapter.SynergyItemViewHolder>() {

    private val inflater: LayoutInflater
    private var itemsList: List<SynergyItem>? = null

    init {
        inflater = LayoutInflater.from(context)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): SynergyItemViewHolder {

        val itemView = inflater.inflate(R.layout.recyclerview_synergy_item, parent, false)
        return SynergyItemViewHolder(itemView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: SynergyItemViewHolder,
                                  position: Int) {

        if(itemsList != null){

            val synergyItem = itemsList!![position]
            holder.tvItemId.text = synergyItem.itemId
            holder.tvItemValue.text = synergyItem.itemValue
            holder.tvItemType.text = synergyItem.itemType

            if(synergyItem.itemLastModified != null) {

                holder.tvItemLastModified.text = synergyItem.itemLastModified

            }else{

                holder.tvItemLastModified.text = "[unmodified]"
            }

        }else{

            holder.tvItemId.text = "no item"
            holder.tvItemValue.text = "no item"
            holder.tvItemType.text = "no item"
            holder.tvItemLastModified.text = "no item"
        }
    }

    override fun getItemCount(): Int {

        return if (itemsList != null)
            itemsList!!.size
        else
            0
    }

    internal fun setItems(itemsList: List<SynergyItem>) {
        this.itemsList = itemsList
        notifyDataSetChanged()
    }


    inner class SynergyItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val tvItemId: TextView
        val tvItemValue: TextView
        val tvItemType: TextView
        val tvItemLastModified: TextView

        init{
            tvItemId = itemView.findViewById(R.id.tvItemId)
            tvItemValue = itemView.findViewById(R.id.tvItemValue)
            tvItemType = itemView.findViewById(R.id.tvItemType)
            tvItemLastModified = itemView.findViewById(R.id.tvItemLastModified)
        }
    }

}