package com.example.project1_daytripplanner_montgomery


import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import android.util.Log


class DetailsAdapter(val places: List<places>) : RecyclerView.Adapter<DetailsAdapter.ViewHolder>() {

    // The adapter needs to render a new row and needs to know what XML file to use
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Layout inflation (read & parse XML file and return a reference to the root layout)
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_details, parent, false)
        return ViewHolder(view)
    }

    // The adapter has a row that's ready to be rendered and needs the content filled in
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentPlace = places[position]

        val intentCall : Intent = Intent(Intent.ACTION_DIAL)
        val intentLink : Intent = Intent(Intent.ACTION_VIEW)


        holder.call.isVisible = false

        holder.name.text = currentPlace.name
        holder.address.text = currentPlace.address
        holder.pricePoint.text = currentPlace.pricePoint

        holder.ratingBar.setRating(currentPlace.rating)

        // if the phone number
        if(currentPlace.phoneNumber != ""){
            holder.call.isVisible = true
            intentCall.data = Uri.parse("tel:${currentPlace.phoneNumber}")
        }


        holder.call.setOnClickListener{
            holder.call.context.startActivity(intentCall)

        }

        holder.link.setOnClickListener{
            intentLink.data = Uri.parse("${currentPlace.url}")
            holder.call.context.startActivity(intentLink)

        }


    }

    // Return the total number of rows you expect your list to have
    override fun getItemCount(): Int {
        Log.d("licitag", "size of items in list: ${places.size}")
        return places.size
    }

    // A ViewHolder represents the Views that comprise a single row in our list (e.g.
    // our row to display a Tweet contains three TextViews and one ImageView).
    //
    // The "itemView" passed into the constructor comes from onCreateViewHolder because our LayoutInflater
    // ultimately returns a reference to the root View in the row's inflated layout. From there, we can
    // call findViewById to search from that root View downwards to find the Views we card about.
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.name)

        val address: TextView = itemView.findViewById(R.id.address)

        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)

        val pricePoint: TextView = itemView.findViewById(R.id.pricePoint)

        val call : ImageButton = itemView.findViewById(R.id.call)

        val link : Button = itemView.findViewById(R.id.link)


    }
}