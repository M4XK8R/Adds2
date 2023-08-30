package com.example.adds2.ui.screens.search.fragment

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.adds2.R
import com.example.adds2.databinding.FragmentAddsBinding
import com.example.adds2.ui.screens.list.ImageAdapter
import com.example.adds2.ui.screens.list.ListFragment
import com.example.adds2.ui.screens.search.SharedViewModel
import com.example.adds2.ui.screens.search.dialog.SearchDialog
import com.example.adds2.ui.screens.search.fragment.adapter.ViewPagerAdapter
import com.example.adds2.utils.DEFAULT_TAG
import com.example.adds2.utils.LocationHelper
import com.example.adds2.utils.createLog
import com.example.adds2.utils.makeToast

class AddsFragment : Fragment() {

    private lateinit var binding: FragmentAddsBinding
    private lateinit var searchDialogHelper: SearchDialog
    private lateinit var viewModel: SharedViewModel
    private lateinit var adapterVp: ViewPagerAdapter


    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
//                val inputStream = requireActivity().contentResolver.openInputStream(it)
//                val bitmap = BitmapFactory.decodeStream(inputStream)
//                binding.ivPictureContainer.setImageBitmap(bitmap)
//                binding.tvTitle.text = it.toString()

                val position = binding.viewPager.currentItem
                adapterVp.addUriToList(it.toString(), position)
                Log.d(DEFAULT_TAG, "$it")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        binding = FragmentAddsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpCountryName()
        setUpCityName()

        binding.tvCountry.setOnClickListener {
            viewModel.setValueToIsCountryLd(true)

            val listOfCountries = LocationHelper
                .getAllCountriesFromJson(requireActivity())

            viewModel.setValueToListOFLocationLd(listOfCountries)
            showDialog()
        }

        binding.tvCity.setOnClickListener {
            viewModel.setValueToIsCountryLd(false)

            val country = viewModel.countryLd.value
            if (country == null) {
                makeToast(requireActivity(), "YOU SHOULD CHOOSE COUNTRY FIRST")
                return@setOnClickListener
            } else {
                val listOfCities = LocationHelper
                    .getAllCitiesFromJson(requireActivity(), country)

                viewModel.setValueToListOFLocationLd(listOfCities)
                showDialog()
            }
        }

        binding.btnLaunchRecycler.setOnClickListener {
            launchListFragment()
        }

        adapterVp = ViewPagerAdapter(requireActivity())
        binding.viewPager.adapter = adapterVp

        val url1 =
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQmE01VrOBJ-MkQMtN2RPG3a1M3ip-2nWg7Ag&usqp=CAU"
        val url2 =
            "https://i.redd.it/1pmsjnk8f1g01.jpg"
        val url3 =
            "https://upload.wikimedia.org/wikipedia/commons/thumb/3/36/Two_red_dice_01.svg/1200px-Two_red_dice_01.svg.png"
        val url4 =
            "https://randomwordgenerator.com/img/picture-generator/57e0d4464b50a914f1dc8460962e33791c3ad6e04e50744172297cdd9345cc_640.jpg"

        val list = listOf(url1, url2, url3, url4)
        adapterVp.setList(list)

        //        binding.ivPictureContainer.setOnClickListener {
//            getContent.launch("image/*")
//        }

        adapterVp.imageClickLambda = {
            getContent.launch("image/*")
//            createLog("position = $position")
            createLog("adapterVp.imageClickLambda  = ${adapterVp.imageClickLambda}")
        }

    }

    private fun launchListFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, ListFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    private fun showDialog() {
        searchDialogHelper = SearchDialog(requireActivity())
        searchDialogHelper.show(requireActivity().supportFragmentManager, "TAG")
    }

    private fun setUpCityName() {
        viewModel.cityLd.observe(viewLifecycleOwner) { cityName ->
            binding.tvCity.text = cityName
        }
    }

    private fun setUpCountryName() {
        viewModel.countryLd.observe(viewLifecycleOwner) { countryName ->
            binding.tvCountry.text = countryName
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = AddsFragment()
    }

}