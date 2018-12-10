package com.booisajerk.tastedivetester.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.booisajerk.tastedivetester.R
import com.booisajerk.tastedivetester.model.Media

/**
 * Provide views to RecyclerView with data from media.
 *
 * Initialize the dataset of the Adapter.
 */
class MediaAdapter : RecyclerView.Adapter<MediaAdapter.MediaViewHolder>() {

    private var dataSource: List<Media>? = null

    fun setDataSource(dataSource: List<Media>) {
        this.dataSource = dataSource
        notifyDataSetChanged()
    }

    /**
     * Provide a reference to the views
     */
    class MediaViewHolder internal constructor (itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var mediaItem: Media? = null

        private val title: TextView by lazy {
            itemView.findViewById(R.id.mediaTitle) as TextView
        }

        private val type: TextView by lazy {
            itemView.findViewById(R.id.mediaType) as TextView
        }

        private val description: TextView by lazy {
            itemView.findViewById(R.id.mediaDescription) as TextView
        }

        fun bind(media: Media) {
            this.mediaItem = media

            title.text = media.name
            type.text = media.type
            description.text = media.description
        }
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

        val mediaItem: Media = dataSource!![position]
        viewHolder.bind(mediaItem)


    }

    // Return the size of your dataset (invoked by layout manager)
    override fun getItemCount(): Int {
        return dataSource?.size ?: 0
    }


    companion object {
        private const val TAG = "MediaAdapter"
    }
}