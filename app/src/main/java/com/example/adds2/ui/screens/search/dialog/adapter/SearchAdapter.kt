package com.example.adds2.ui.screens.search.dialog.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adds2.databinding.ViewHolderSearchBinding

class SearchAdapter : RecyclerView.Adapter<SearchViewHolder>() {

    var passDataToAddsFragment: ((String) -> Unit)? = null

    private val currentList = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ViewHolderSearchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchViewHolder(binding)
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val country = currentList[position]

        holder.binding.apply {
            tvCountry.text = country
            root.setOnClickListener {
//                Log.d(
//                    "passDataToAddsFragment",
//                    "passDataToAddsFragment = $passDataToAddsFragment, country = $country"
//                )
                passDataToAddsFragment?.invoke(country)
            }
        }
    }

    fun setList(newList: List<String>) {
        currentList.clear()
        currentList.addAll(newList)
        notifyDataSetChanged()
    }
}