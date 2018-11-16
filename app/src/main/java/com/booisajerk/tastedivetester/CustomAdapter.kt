package com.booisajerk.tastedivetester

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.booisajerk.tastedivetester.TextHelpers.capitalizeFirstLetter
import com.booisajerk.tastedivetester.TextHelpers.trimNewLines

/**
 * Provide views to RecyclerView with data from movies.
 *
 * Initialize the dataset of the Adapter.
 */
class CustomAdapter(private val movies: ArrayList<Movie>) :
    RecyclerView.Adapter<CustomAdapter.MovieViewHolder>() {

    /**
     * Provide a reference to the views
     */
    class MovieViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.findViewById(R.id.movieTitle)
        val type: TextView = v.findViewById(R.id.mediaType)
        val description: TextView = v.findViewById(R.id.movieDescription)
    }

    // Create new views (invoked by layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MovieViewHolder {
        // Create a new view.
        val vCardView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_item, viewGroup, false)

        return MovieViewHolder(vCardView)
    }

    // Replace the contents of a view (invoked by layout manager)
    override fun onBindViewHolder(viewHolder: MovieViewHolder, position: Int) {
        Log.d(TAG, "Element $position set.")

        // Get element from dataset at this position and replace the contents of the view with that element
        viewHolder.title.text = movies[position].name
        viewHolder.type.text = capitalizeFirstLetter(movies[position].type)
        viewHolder.description.text = trimNewLines(movies[position].wTeaser)
    }

    // Return the size of your dataset (invoked by layout manager)
    override fun getItemCount() = movies.size



    companion object {
        private val TAG = "CustomAdapter"
    }
}