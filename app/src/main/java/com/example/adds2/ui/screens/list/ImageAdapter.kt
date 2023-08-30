package com.example.adds2.ui.screens.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adds2.databinding.ViewHolderImageBinding
import com.example.adds2.utils.TouchHelper


class ImageAdapter(private val context: Context) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    private val imageUrisList = ArrayList<String>()

    init {
        TouchHelper.onMoveLambda = { startPosition, targetPosition ->
            val temp = imageUrisList[targetPosition]
            imageUrisList[targetPosition] = imageUrisList[startPosition]
            imageUrisList[startPosition] = temp
            notifyItemMoved(startPosition, targetPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ViewHolderImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUriString = imageUrisList[position]
        holder.tvImageUri.text = imageUriString
//        val imageUri = Uri.parse(imageUriString)
//        holder.ivImage.setImageURI(imageUri)
        Glide.with(context)
            .load(imageUriString)
            .into(holder.ivImage)
    }

    override fun getItemCount(): Int = imageUrisList.size

    fun setList(list: List<String>) {
        imageUrisList.clear()
        imageUrisList.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: ViewHolderImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val ivImage: ImageView = binding.ivPictureContainer
        val tvImageUri: TextView = binding.tvImageUri
    }

}