package com.readthym.book.ui.fav

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.readthym.book.R
import com.readthym.book.data.remote.reqres.ListFavoriteBookResponse.FavoriteData
import com.readthym.book.databinding.ItemBookFavBinding
import com.readthym.book.utils.UIHelper.loadImageFromURL

class FavoriteBookAdapter : RecyclerView.Adapter<FavoriteBookAdapter.AdapterViewHolder>() {

    val data = mutableListOf<FavoriteData>()
    lateinit var adapterInterface: ItemInterface

    fun setWithNewData(data: MutableList<FavoriteData>) {
        this.data.clear()
        this.data.addAll(data)
    }

    fun setupAdapterInterface(obj: ItemInterface) {
        this.adapterInterface = obj
    }

    inner class AdapterViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var binding: ItemBookFavBinding = ItemBookFavBinding.bind(itemView)

        fun onBind(model: FavoriteData) {
            val mContext = binding.root.context

            binding.ivFav.loadImageFromURL(
                mContext, model.book.photoPathFull
            )

            binding.descFavTop.text = model.book.title
            binding.descFavBottom.text = model.book.authorName

            binding.btnAddToFav.setOnClickListener {
                adapterInterface.onDelete(model)
            }

            binding.root.setOnClickListener {
                if (adapterInterface != null) {
                    adapterInterface.onclick(model)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_book_fav, parent, false)
        return AdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface ItemInterface {
        fun onclick(model: FavoriteData)
        fun onDelete(model: FavoriteData)
    }
}