package com.readthym.book.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.readthym.book.R
import com.readthym.book.data.remote.reqres.BookData
import com.readthym.book.databinding.*
import com.readthym.book.utils.UIHelper.loadImageFromURL

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchRvViewHolder>() {

    val data = mutableListOf<BookData>()
    lateinit var adapterInterface: SearchItemInterface

    fun setWithNewData(data: MutableList<BookData>) {
        this.data.clear()
        this.data.addAll(data)
    }

    fun setupAdapterInterface(obj: SearchItemInterface) {
        this.adapterInterface = obj
    }

    inner class SearchRvViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var binding: ItemCardSearchBinding = ItemCardSearchBinding.bind(itemView)

        fun onBind(model: BookData) {
            val mContext = binding.root.context

            binding.descFavTop.text=model.title.toString()
            binding.descFavBottom.text=model.description.toString()

            binding.ivFav.loadImageFromURL(mContext,model.photoPathFull)

            binding.root.setOnClickListener {
                if (adapterInterface != null)
                    adapterInterface.onclick(model)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchRvViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_card_search, parent, false)
        return SearchRvViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchRvViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface SearchItemInterface {
        fun onclick(model: BookData)
    }
}