package com.kg.myapplication.ui.fragments.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kg.myapplication.R
import com.kg.myapplication.data.entity.ShopItems
import com.kg.myapplication.databinding.ItemOrderBinding

class OrderAdapter(private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<OrderAdapter.OrderVH>() {
    private var menuList: List<ShopItems> = mutableListOf()
    interface ItemClickListener {
        fun onItemClick(name: String, price: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderVH {
        return OrderVH(
            ItemOrderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(list: ArrayList<ShopItems>) {
        this.menuList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    override fun onBindViewHolder(holder: OrderVH, position: Int) {
        holder.bind(menuList[position], itemClickListener)
    }


    class OrderVH(
        binding: ItemOrderBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private val image = itemView.findViewById<ImageView>(R.id.img)
        private val tvName = itemView.findViewById<TextView>(R.id.tv_name)
        private val Price = itemView.findViewById<TextView>(R.id.tv_price)

        @SuppressLint("SetTextI18n")
        fun bind(item: ShopItems, onItemClick: ItemClickListener) {
            tvName.text = item.title
            Price.text = item.price
            Glide.with(itemView.context)
                .load(item.image)
                .placeholder(R.drawable.ic_launcher_background)
                .into(image)

            itemView.setOnClickListener {
                item.title?.let { it1 -> item.price?.let { it2 -> onItemClick.onItemClick(it1, it2) } }
            }
        }


    }
}