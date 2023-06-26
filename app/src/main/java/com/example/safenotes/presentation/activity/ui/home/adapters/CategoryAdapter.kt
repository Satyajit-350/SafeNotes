package com.example.safenotes.presentation.activity.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.safenotes.databinding.ItemCategoryBinding

class CategoryAdapter (private val categoryList: List<String>,private val onCategoryClicked: (String) -> Unit):
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder (private val binding: ItemCategoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(category: String){
            binding.categoryTv.text = category
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        category.let {
            holder.bind(it)
            holder.itemView.setOnClickListener{ _ ->
                onCategoryClicked(it)
            }
        }
    }
}