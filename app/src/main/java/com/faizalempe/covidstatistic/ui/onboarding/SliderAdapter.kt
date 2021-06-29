package com.faizalempe.covidstatistic.ui.onboarding

import android.content.Context
import android.content.res.TypedArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.faizalempe.covidstatistic.R
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlinx.android.synthetic.main.item_onboarding.view.*

class SliderAdapter(
    private val context: Context,
    private val imgResources: TypedArray,
    private val titleResources: Array<String>,
    private val textResources: Array<String>
): SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapterViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_onboarding, parent, false)
        return SliderAdapterViewHolder(view)
    }

    override fun getCount(): Int {
        return textResources.size
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterViewHolder, position: Int) {
        viewHolder.bind(position)
    }

    inner class SliderAdapterViewHolder(private val view: View): SliderViewAdapter.ViewHolder(view) {

        fun bind(position: Int) {
            view.onboarding_img.setImageDrawable(ContextCompat.getDrawable(context, imgResources.getResourceId(position, 0)))
            view.onboarding_txt_title.text = titleResources[position]
            view.onboarding_txt_text.text = textResources[position]
        }
    }

}