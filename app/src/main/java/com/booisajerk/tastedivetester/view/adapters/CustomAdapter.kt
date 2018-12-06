package com.booisajerk.tastedivetester.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.booisajerk.tastedivetester.model.Media
import com.booisajerk.tastedivetester.R
import com.booisajerk.tastedivetester.shared.TextHelpers.capitalizeFirstLetter
import com.booisajerk.tastedivetester.shared.TextHelpers.trimNewLines

/**
 * Provide views to RecyclerView with data from media.
 *
 * Initialize the dataset of the Adapter.
 */
class CustomAdapter(private val media: ArrayList<Media>) :
    RecyclerView.Adapter<CustomAdapter.MediaViewHolder>() {

    /**
     * Provide a reference to the views
     */
    class MediaViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.findViewById(R.id.mediaTitle)
        val type: TextView = v.findViewById(R.id.mediaType)
        val description: TextView = v.findViewById(R.id.mediaDescription)
    }

    // Create new views (invoked by layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MediaViewHolder {
        // Create a new view.
        val vCardView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_item, viewGroup, false)

        return MediaViewHolder(vCardView)
    }

    // Replace the contents of a view (invoked by layout manager)
    override fun onBindViewHolder(viewHolder: MediaViewHolder, position: Int) {
        Log.d(TAG, "Element $position set.")

        // Get element from dataset at this position and replace the contents of the view with that element
        viewHolder.title.text = media[position].name
        viewHolder.type.text = media[position].type?.let { capitalizeFirstLetter(it) }
        viewHolder.description.text = media[position].description?.let { trimNewLines(it) }
    }

    // Return the size of your dataset (invoked by layout manager)
    override fun getItemCount() = media.size



    companion object {
        private const val TAG = "CustomAdapter"
    }
}