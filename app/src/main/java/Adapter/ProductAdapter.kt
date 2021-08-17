package Adapter

import android.example.products.R
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.gridviewlayout.view.*

import models.MyProducts

class ProductAdapter(private val myProducts:ArrayList<MyProducts>, var listener: ClickListener): RecyclerView.Adapter<ProductAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val ivProduct=itemView.findViewById<ImageView>(R.id.ivProduct)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var itemView=MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.gridviewlayout,parent,false))
        itemView.ivProduct.setOnClickListener {
            listener.onItemClick(myProducts[itemView.absoluteAdapterPosition])
        }
        return itemView
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.tvBrandName.text=myProducts[position].myProductName
        holder.itemView.tvPrice.text=myProducts[position].myPrice
        holder.itemView.tvProductName.text=myProducts[position].myProductName
        holder.itemView.tvRating.text=myProducts[position].myRating
        Picasso.get().load(myProducts[position].myImage).into(holder.itemView.ivProduct)
    }

    override fun getItemCount(): Int {
       return myProducts.size
    }
}
interface ClickListener{
    fun onItemClick(products: MyProducts)
}