package master.kotlin.a7minuteworkout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import master.kotlin.a7minuteworkout.databinding.ItemHistoryRowBinding

class HistoryItemAdapter(private val context: Context, private val items: ArrayList<String>) : RecyclerView.Adapter<HistoryItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemHistoryRowBinding = ItemHistoryRowBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(itemHistoryRowBinding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            tvItem.text = items[position]
            tvPosition.text = (position+1).toString()
            llHistoryMain.setBackgroundColor(
                    if(position%2==0)
                        ContextCompat.getColor(context, R.color.colorHighLighted)
                    else
                        ContextCompat.getColor(context, R.color.colorWhite)
            )
        }
    }

    inner class ViewHolder(val binding: ItemHistoryRowBinding) : RecyclerView.ViewHolder(binding.root){

    }
}