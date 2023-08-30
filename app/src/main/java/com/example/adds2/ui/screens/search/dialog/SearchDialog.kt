package com.example.adds2.ui.screens.search.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.adds2.databinding.DialogSearchBinding
import com.example.adds2.ui.screens.search.SharedViewModel
import com.example.adds2.ui.screens.search.dialog.adapter.SearchAdapter
import com.example.adds2.utils.LocationHelper

class SearchDialog(private val activity: FragmentActivity) : DialogFragment() {
    private val binding = DialogSearchBinding
        .inflate(LayoutInflater.from(activity))

    private lateinit var searchAdapter: SearchAdapter
    private lateinit var viewModel: SharedViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        val builder = AlertDialog.Builder(activity)
            .setView(binding.root)
            .setNegativeButton("Cancel") { _, _ ->
                dialog?.dismiss()
            }

        val dialog = builder.create()

//        val listOfCountries = LocationHelper.getAllCountriesFromJson(requireActivity())
        val listOfLocation = viewModel.listOfLocationLd.value ?: arrayListOf("Error")
        setUpAdapter(listOfLocation)
        bindSearchViewWithRecycler()

        return dialog
    }

    /**
     *      PRIVATE FUNCTIONS
     */
    private fun setUpAdapter(list: List<String>) {

        fun passDataToFragment() {
            searchAdapter.passDataToAddsFragment = { location ->
                val isCountry = viewModel.isCountryLd.value ?: false

                if (isCountry) {
                    viewModel.setValueToCountryLd(location)
                    if (viewModel.countryLd.value != null) {
                        viewModel.setValueToCityLd("City")
                    }
                }

                if (!isCountry && viewModel.countryLd.value != null) {
                    viewModel.setValueToCityLd(location)
                }
                dialog?.dismiss()
            }
        }

        searchAdapter = SearchAdapter()
        binding.recyclerView.adapter = searchAdapter
        searchAdapter.setList(list)
        passDataToFragment()
    }

    private fun bindSearchViewWithRecycler() {
        val onQueryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val listOfLocations = viewModel.listOfLocationLd.value ?: listOf("Error")

                val searchedLocations =
                    LocationHelper.searchLocations(activity, listOfLocations, newText)

                searchAdapter.setList(searchedLocations)
                return true
            }
        }
        binding.searchView.setOnQueryTextListener(onQueryTextListener)
    }

}










