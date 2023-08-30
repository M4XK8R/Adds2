package com.example.adds2.utils

import android.content.Context
import org.json.JSONObject
import java.io.IOException

object LocationHelper {


    fun searchLocations(
        context: Context,
        list: List<String>,
        searchText: String?
    ): List<String> {
//        val listOfCountries = getAllCountriesFromJson(context)
        val listOfSearchedLocations = ArrayList<String>()

        if (searchText == null) {
            listOfSearchedLocations.add("No result")
            return listOfSearchedLocations
        }

        for (locationName in list) {
            val validLocationName = locationName.lowercase()
            val validSearchText = searchText.lowercase()
            if (validLocationName.startsWith(validSearchText)) {
                listOfSearchedLocations.add(locationName)
            }
            if (listOfSearchedLocations.size == 0) {
                listOfSearchedLocations.add("No result")
            }
        }
        return listOfSearchedLocations.toList()
    }

    private fun getJsonObject(context: Context): JSONObject {
        val inputStream = context.assets.open("countriesToCities.json")
        val size = inputStream.available()
        val byteArray = ByteArray(size)
        inputStream.read(byteArray)
        val jsonFile = String(byteArray)
        return JSONObject(jsonFile)
    }

    fun getAllCitiesFromJson(context: Context, country: String): List<String> {
        val listOfCities = ArrayList<String>()

        try {
            val jsonObject = getJsonObject(context)
            val cityNames = jsonObject.getJSONArray(country)

            for (city in 0 until cityNames.length()) {
                listOfCities.add(cityNames.getString(city))
            }
        } catch (e: IOException) {
            createLog("getAllCitiesFromJson error: ${e.message}")
        }
        return listOfCities
    }

    fun getAllCountriesFromJson(context: Context): ArrayList<String> {
        val listOfCountries = ArrayList<String>()
        try {
            val jsonObject = getJsonObject(context)
            val countryNames = jsonObject.names()

            if (countryNames != null) {
                for (country in 0 until countryNames.length()) {
                    listOfCountries.add(countryNames.getString(country))
                }
            }
        } catch (e: IOException) {
            createLog("getAllCountriesFromJson error: ${e.message}")
        }
        return listOfCountries
    }

}






