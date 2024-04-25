package com.example.digitalcurrencyappdca.features.CoinActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.digitalcurrencyappdca.R
import com.example.digitalcurrencyappdca.apiManager.*
import com.example.digitalcurrencyappdca.apiManager.model.CoinAboutItem
import com.example.digitalcurrencyappdca.apiManager.model.CoinsData
import com.example.digitalcurrencyappdca.databinding.ActivityCoinBinding
import ir.dunijet.dunipool.apiManager.model.ChartData
import java.net.URL

class CoinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCoinBinding
    private lateinit var dataThisCoin: CoinsData.Data
    lateinit var dataThisCoinAbout: CoinAboutItem
    val apiManager = ApiManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fromIntent = intent.getBundleExtra("bundle")!!
        dataThisCoin = fromIntent.getParcelable("bundle1")!!

        if (fromIntent.getParcelable<CoinAboutItem>("bundle2") != null){
            dataThisCoinAbout = fromIntent.getParcelable("bundle2")!!
        }
        else{
            dataThisCoinAbout = CoinAboutItem()
        }

        binding.layoutToolbar.toolbar.title = dataThisCoin.coinInfo.fullName
        initUi()


    }

    private fun initUi() {

        initChartUi()

        initStatisticsUi()

        initAboutUi()


    }

    @SuppressLint("SetTextI18n")
    private fun initAboutUi() {

        binding.layoutAbout.txtWebsite.text = dataThisCoinAbout.coinWebsite ?: "no-data"
        binding.layoutAbout.txtGithub.text = dataThisCoinAbout.coinGithub ?: "no-data"
        binding.layoutAbout.txtReddit.text = dataThisCoinAbout.coinReddit ?: "no-data"
        binding.layoutAbout.txtTwitter.text = "@" + dataThisCoinAbout.coinTwitter
        binding.layoutAbout.txtAboutCoin.text = dataThisCoinAbout.coinDesc ?: "no-data"

        binding.layoutAbout.txtWebsite.setOnClickListener {
            if(binding.layoutAbout.txtWebsite.text != "no-data" && binding.layoutAbout.txtWebsite.text != ""){
            openWebsiteDataCoin(dataThisCoinAbout.coinWebsite!!)
            }
        }
        binding.layoutAbout.txtGithub.setOnClickListener {
            if(binding.layoutAbout.txtGithub.text != "no-data" && binding.layoutAbout.txtGithub.text != "") {
                openWebsiteDataCoin(dataThisCoinAbout.coinGithub!!)
            }
        }
        binding.layoutAbout.txtReddit.setOnClickListener {
            if(binding.layoutAbout.txtReddit.text != "no-data" && binding.layoutAbout.txtReddit.text != "") {
                openWebsiteDataCoin(dataThisCoinAbout.coinReddit!!)
            }
        }
        binding.layoutAbout.txtTwitter.setOnClickListener {
            if(binding.layoutAbout.txtTwitter.text != "no-data" && binding.layoutAbout.txtTwitter.text != "" ) {
            openWebsiteDataCoin(BASE_URL_TWITTER + dataThisCoinAbout.coinTwitter!!)
            }
        }

    }

    private fun openWebsiteDataCoin(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    @SuppressLint("SetTextI18n")
    private fun initStatisticsUi() {

        binding.layoutStatistics.tvOpenAmount.text = dataThisCoin.dISPLAY.uSD.oPEN24HOUR
        binding.layoutStatistics.tvTodaysHighAmount.text = dataThisCoin.dISPLAY.uSD.hIGH24HOUR
        binding.layoutStatistics.tvTodayLowAmount.text = dataThisCoin.dISPLAY.uSD.lOW24HOUR
        binding.layoutStatistics.tvChangeTodayAmount.text = dataThisCoin.dISPLAY.uSD.cHANGE24HOUR
        binding.layoutStatistics.tvAlgorithm.text = dataThisCoin.coinInfo.algorithm
        binding.layoutStatistics.tvTotalVolume.text = dataThisCoin.dISPLAY.uSD.tOTALVOLUME24H
        binding.layoutStatistics.tvAvgMarketCapAmount.text = dataThisCoin.dISPLAY.uSD.mKTCAP
        binding.layoutStatistics.tvSupplyNumber.text = dataThisCoin.dISPLAY.uSD.sUPPLY
    }

    @SuppressLint("SetTextI18n")
    private fun initChartUi() {

        var period: String = HOUR
        requestAndShowChart(period)
        binding.layoutChart.radioGroupMain.setOnCheckedChangeListener { _, checkedId ->

            when (checkedId) {
                R.id.radio_12h -> {
                    period = HOUR
                }
                R.id.radio_1d -> {
                    period = HOURS24
                }
                R.id.radio_1w -> {
                    period = WEEK
                }
                R.id.radio_1m -> {
                    period = MONTH
                }
                R.id.radio_3m -> {
                    period = MONTH3
                }
                R.id.radio_1y -> {
                    period = YEAR
                }
                R.id.radio_all -> {
                    period = ALL
                }
            }
            requestAndShowChart(period)
        }

        binding.layoutChart.txtChartPrice.text = dataThisCoin.dISPLAY.uSD.pRICE
        if ( dataThisCoin.rAW.uSD.cHANGEPCT24HOUR.toString().length<5)
        {
            binding.layoutChart.txtChartChange2.text =" " + dataThisCoin.rAW.uSD.cHANGEPCT24HOUR.toString().substring(0,dataThisCoin.rAW.uSD.cHANGEPCT24HOUR.toString().length) +"%"
        }else
        {
            binding.layoutChart.txtChartChange2.text =" " + dataThisCoin.rAW.uSD.cHANGEPCT24HOUR.toString().substring(0,5) +"%"
        }

        binding.layoutChart.txtChartChange1.text =" " + dataThisCoin.dISPLAY.uSD.cHANGE24HOUR


        val taghir = dataThisCoin.rAW.uSD.cHANGEPCT24HOUR
        if (taghir > 0) {
            binding.layoutChart.txtChartChange2.setTextColor(
                ContextCompat.getColor(this, R.color.colorGain))
            binding.layoutChart.txtChartUpDown.setTextColor(ContextCompat.getColor(this, R.color.colorGain))

            binding.layoutChart.txtChartUpDown.text = "▲"

            binding.layoutChart.sparkViewMain.lineColor = ContextCompat.getColor(this, R.color.colorGain)

        } else if (taghir < 0) {
            binding.layoutChart.txtChartChange2.setTextColor(
                ContextCompat.getColor(this, R.color.colorLoss))
            binding.layoutChart.txtChartUpDown.setTextColor(ContextCompat.getColor(this, R.color.colorLoss))

            binding.layoutChart.txtChartUpDown.text = "▼"

            binding.layoutChart.sparkViewMain.lineColor = ContextCompat.getColor(this, R.color.colorLoss)

        }

        binding.layoutChart.sparkViewMain.setScrubListener {

            // show whole price
            if (it == null)
            {
                binding.layoutChart.txtChartPrice.text = dataThisCoin.dISPLAY.uSD.pRICE
            }
            else {
                // show price of this dot
                binding.layoutChart.txtChartPrice.text = (it as ChartData.Data).close.toString()
            }

        }


    }

    fun requestAndShowChart(period: String) {
        apiManager.getChartData(
            dataThisCoin.coinInfo.name,
            period,
            object : ApiManager.ApiCallback<Pair<List<ChartData.Data>, ChartData.Data?>> {
                override fun onSuccess(data: Pair<List<ChartData.Data>, ChartData.Data?>) {

                    val chartAdapter = ChartAdapter(data.first, data.second?.open.toString())
                    binding.layoutChart.sparkViewMain.adapter = chartAdapter
                }

                override fun onError(errorMessage: String) {
                    Toast.makeText(this@CoinActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }

            })
    }

}