package com.example.digitalcurrencyappdca.features.MarketActivity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.digitalcurrencyappdca.R
import com.example.digitalcurrencyappdca.apiManager.BASE_URL_IMAGE
import com.example.digitalcurrencyappdca.apiManager.model.CoinsData
import com.example.digitalcurrencyappdca.databinding.ItemRecyclerMarketBinding

class MarketAdapter(
    private val data: ArrayList<CoinsData.Data>,
    private val recyclerCallback: RecyclerCallback
) : RecyclerView.Adapter<MarketAdapter.MarketViewHolder>() {

    lateinit var binding: ItemRecyclerMarketBinding

    inner class MarketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bindViews(dataCoin: CoinsData.Data) {

            binding.txtCoinName.text = dataCoin.coinInfo.fullName
            binding.txtPrice.text = dataCoin.dISPLAY.uSD.pRICE

            val taghir = dataCoin.rAW.uSD.cHANGE24HOUR
            if (taghir > 0) {

                binding.txtTaghir.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.colorGain
                    )
                )

                if (taghir.toString().length < 4)
                {
                    binding.txtTaghir.text = taghir.toString().substring(0, taghir.toString().length) +"$"
                }else {
                    binding.txtTaghir.text = taghir.toString().substring(0, 4) + "$"
                }

            } else if (taghir < 0) {
                binding.txtTaghir.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.colorLoss
                    )
                )
                if (taghir.toString().length < 6)
                {
                    binding.txtTaghir.text = taghir.toString().substring(0, taghir.toString().length) + "$"
                }else {
                    binding.txtTaghir.text = taghir.toString().substring(0, 6) + "$"
                }

            } else {
                binding.txtTaghir.text = "0"
            }

            val marketCap = dataCoin.rAW.uSD.mKTCAP / 1000000000
            val indexDot = marketCap.toString().indexOf(".")
            binding.txtMarketCap.text = "$" + marketCap.toString().substring(0, indexDot + 3) + "B"


            Glide
                .with(itemView)
                .load(BASE_URL_IMAGE + dataCoin.coinInfo.imageUrl)
                .into(binding.imgItem)


            itemView.setOnClickListener {

                recyclerCallback.onCoinItemClicked(dataCoin)

            }

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemRecyclerMarketBinding.inflate(inflater, parent, false)

        return MarketViewHolder(binding.root)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {

        holder.bindViews(data[position])

    }

    interface RecyclerCallback {

        fun onCoinItemClicked(dataCoin: CoinsData.Data) {

        }

    }

}