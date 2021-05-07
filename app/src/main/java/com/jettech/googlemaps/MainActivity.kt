package com.jettech.googlemaps

import android.os.Bundle
import android.text.Editable
import android.text.Selection.setSelection
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


class MainActivity : AppCompatActivity() {

    private var mMap: GoogleMap? = null
    private var placeAdapter: PlacesResultAdapter? = null
    private lateinit var mPlacesClient: PlacesClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //placesClient = Places.createClient(this@MainActivity)



        init()
//        recyclerView = findViewById<View>(R.id.places_recycler_view) as RecyclerView
//        (findViewById<View>(R.id.place_search) as EditText).addTextChangedListener(filterTextWatcher)
//
//        mAutoCompleteAdapter = PlacesAutoCompleteAdapter(this)
//        recyclerView!!.setLayoutManager(LinearLayoutManager(this))
//        mAutoCompleteAdapter!!.setClickListener(this@MainActivity)
//        recyclerView!!.setAdapter(mAutoCompleteAdapter)
//        mAutoCompleteAdapter!!.notifyDataSetChanged()

    }




    private fun init() {
        val et_search_bar = findViewById<EditText>(R.id.et_search_bar)
        val rv_place_results = findViewById<RecyclerView>(R.id.rv_place_results)
        Places.initialize(this@MainActivity, "AIzaSyDMPN-JvhHd3EWf9iGrVyJ3gnpxYgBEq6Y")
        mPlacesClient = Places.createClient(this@MainActivity)
        val apiKey = getString(R.string.places_api_key)
        if (!Places.isInitialized()) {
            Places.initialize(this, apiKey)
        }
        val placesClient = Places.createClient(this)
        placeAdapter = PlacesResultAdapter(this) { prediction ->
           val  pickedLocation = prediction
            et_search_bar.setText(prediction.getFullText(null).toString())
            rv_place_results.visibility = View.GONE
        }
        rv_place_results.layoutManager = LinearLayoutManager(this)
        rv_place_results.adapter = placeAdapter

        et_search_bar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(text: Editable?) {
                if (text.toString().isNotEmpty()) {
                    placeAdapter!!.filter.filter(text.toString())
                    if (rv_place_results.isGone) {
                        rv_place_results.visibility = View.VISIBLE
                    }
                } else {
                    if (rv_place_results.isVisible) {
                        rv_place_results.visibility = View.GONE
                    }
                }
            }
        })
    }


}



