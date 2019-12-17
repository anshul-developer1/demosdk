package com.aeropaymerchantsdk.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aeropaymerchantsdk.R
import kotlinx.android.synthetic.main.recycler_card_layout.view.*

class HomeCardRecyclerView(val payerName : ArrayList<String>, val context: Context) : RecyclerView.Adapter<CardViewHolder>() {

    override fun getItemCount(): Int {
        return payerName.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_card_layout, parent, false))
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder?.payerName?.text = payerName.get(position)
    }
}

class CardViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val payerName = view.userName
}