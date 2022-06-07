package com.readthym.book.ui.rythm_home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.readthym.book.R
import com.readthym.book.data.remote.reqres.BookData
import com.readthym.book.databinding.ItemCardBookHomeBinding
import com.readthym.book.utils.UIHelper.loadImageFromURL

class MainBookAdapter : RecyclerView.Adapter<MainBookAdapter.AdapterViewHolder>() {

    val data = mutableListOf<BookData>()
    lateinit var adapterInterface: ItemInterface

    fun setWithNewData(data: MutableList<BookData>) {
        this.data.clear()
        this.data.addAll(data)
    }

    fun setupAdapterInterface(obj: ItemInterface) {
        this.adapterInterface = obj
    }

    inner class AdapterViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var binding: ItemCardBookHomeBinding = ItemCardBookHomeBinding.bind(itemView)

        fun onBind(model: BookData) {
            val mContext = binding.root.context

            binding.thumbnails.loadImageFromURL(
                mContext,model.photoPathFull
            )

            binding.tvBookTitle.text=model.title
            binding.tvBookAuthor.text=model.authorName

            binding.root.setOnClickListener {
                if (adapterInterface != null) {
                    adapterInterface.onclick(model)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_card_book_home, parent, false)
        return AdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface ItemInterface {
        fun onclick(model: BookData)
    }
}