package com.example.adds2.ui.screens.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.adds2.databinding.FragmentListBinding
import com.example.adds2.utils.TouchHelper

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private lateinit var adapterImage: ImageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterImage = ImageAdapter(requireActivity())
        binding.list.adapter = adapterImage

        val url1 =
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQmE01VrOBJ-MkQMtN2RPG3a1M3ip-2nWg7Ag&usqp=CAU"
        val url2 =
            "https://i.redd.it/1pmsjnk8f1g01.jpg"
        val url3 =
            "https://upload.wikimedia.org/wikipedia/commons/thumb/3/36/Two_red_dice_01.svg/1200px-Two_red_dice_01.svg.png"
        val url4 =
            "https://randomwordgenerator.com/img/picture-generator/57e0d4464b50a914f1dc8460962e33791c3ad6e04e50744172297cdd9345cc_640.jpg"

        val list = listOf(url1, url2, url3, url4)
        adapterImage.setList(list)

        val itemTouchHelper = TouchHelper().getItemTouchHelper()
        itemTouchHelper.attachToRecyclerView(binding.list)


    }

    companion object {
        @JvmStatic
        fun newInstance() = ListFragment()
    }

}