package com.example.adds2.ui.screens.search.fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adds2.databinding.ViewHolderVpItemBinding

class ViewPagerAdapter(private val context: Context) : RecyclerView.Adapter<VpItemViewHolder>() {

    var imageClickLambda: (() -> Unit)? = null

    private val imageUrisList = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VpItemViewHolder {
        return VpItemViewHolder(
            ViewHolderVpItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = imageUrisList.size

    override fun onBindViewHolder(holder: VpItemViewHolder, position: Int) {
        val imageUriString = imageUrisList[position]
        holder.tvTitle.text = imageUriString
//        val imageUri = Uri.parse(imageUriString)
//        holder.ivImage.setImageURI(imageUri)
        Glide.with(context)
            .load(imageUriString)
            .into(holder.ivPictureContainer)

        holder.ivPictureContainer.setOnClickListener {
            imageClickLambda?.invoke()
        }
    }

    fun setList(newList: List<String>) {
        imageUrisList.clear()
        imageUrisList.addAll(newList)
        notifyDataSetChanged()
    }

    fun addUriToList(uri: String, index: Int) {
        imageUrisList[index] = uri
        notifyDataSetChanged()
    }

}