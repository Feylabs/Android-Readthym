package com.readthym.book.ui.manage_book

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.readthym.book.R
import com.readthym.book.data.remote.reqres.BookData
import com.readthym.book.databinding.ItemBookAdminBinding
import com.readthym.book.databinding.ItemCardBookHomeBinding
import com.readthym.book.utils.UIHelper.loadImageFromURL

class ManageBookAdapter : RecyclerView.Adapter<ManageBookAdapter.AdapterViewHolder>() {

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

        var binding: ItemBookAdminBinding = ItemBookAdminBinding.bind(itemView)

        fun onBind(model: BookData) {
            val mContext = binding.root.context

            binding.ivFav.loadImageFromURL(
                mContext,model.photoPathFull
            )

            binding.descFavTop.text=model.title
            binding.descFavBottom.text=model.authorName

            binding.btnDeleteBook.setOnClickListener {
                adapterInterface.onclick(model,"DELETE")
            }

            binding.btnEditBook.setOnClickListener {
                adapterInterface.onclick(model,"EDIT")
            }

            binding.root.setOnClickListener {
                if (adapterInterface != null) {
                    adapterInterface.onclick(model,"")
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_book_admin, parent, false)
        return AdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface ItemInterface {
        fun onclick(model: BookData,action:String)
    }
}