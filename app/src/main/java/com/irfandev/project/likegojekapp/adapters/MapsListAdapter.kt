package com.irfandev.project.likegojekapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.irfandev.project.likegojekapp.R
import com.irfandev.project.likegojekapp.helpers.AppsHelper
import com.irfandev.project.likegojekapp.models.MapsModel


/**
 *   created by Irfan Assidiq
 *   email : assidiq.irfan@gmail.com
 **/
class MapsListAdapter : RecyclerView.Adapter<MapsListAdapter.MapsListViewHolder>{
    lateinit var ctx: Context
    lateinit var mapModels : ArrayList<MapsModel>
    constructor(){}
    constructor(ctx : Context, mapModels : ArrayList<MapsModel>){
        this.ctx = ctx
        this.mapModels = mapModels
    }
    class MapsListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var llayout : LinearLayout = itemView.findViewById(R.id.llayout)
        var tvMapName : TextView = itemView.findViewById(R.id.tvMapName)
        var tvMapDetail : TextView = itemView.findViewById(R.id.tvMapDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapsListViewHolder {
        val v = LayoutInflater.from(ctx).inflate(R.layout.item_listmap, parent, false)
        return MapsListViewHolder(v)
    }

    override fun getItemCount(): Int {
        return  mapModels.size
    }

    override fun onBindViewHolder(holder: MapsListViewHolder, position: Int) {
        val oneMap = mapModels.get(position)
        holder.tvMapName.text = oneMap.mapname
        holder.tvMapDetail.text = "${oneMap.mapdetail}\nlatitude = ${oneMap.maplatitude} longitude = ${oneMap.maplongitude}"
        holder.llayout.setOnClickListener {
            AppsHelper.showShortToast(ctx, "this is ${oneMap.mapname}")
        }
    }
}