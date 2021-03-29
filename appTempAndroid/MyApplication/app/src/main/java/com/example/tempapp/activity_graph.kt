package com.example.tempapp

import android.content.Intent
import android.graphics.Color.red
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.tempapp.databinding.ActivityGraphBinding
import com.github.aachartmodel.aainfographics.aachartcreator.*
import java.util.*

class activity_graph : AppCompatActivity() {

    private val TAG = "MyActivity"
    private lateinit var ui: ActivityGraphBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)
        ui = ActivityGraphBinding.inflate(layoutInflater)
        val view = ui.root
        setContentView(view)

        if (data.menuOpen){
            menuOpened()
        }
        else{
            //hide Icos
            hideIcos()
        }


        Log.d(TAG, sensor.name1.toString())
        Log.d(TAG, sensor.name2.toString())
        //listener for small menu
        ui.menuLines.setOnClickListener{
            menuOpened()
        }
        ui.menuHide.setOnClickListener(){
            menuHidden()

        }
        //refArduino.setValue("OFF");
        ui.menuSettings.setOnClickListener(){
            Log.d(TAG, "click settings")
            val intent = Intent(this, activity_settings::class.java)
            changeView.saveOnChange(intent)
            startActivity(intent)
            this.overridePendingTransition(0, 0);
            finish()

        }
        ui.menuHome.setOnClickListener(){
            Log.d(TAG, "click home")
            val intent = Intent(this, MainActivity::class.java)
            changeView.saveOnChange(intent)
            startActivity(intent)
            this.overridePendingTransition(0, 0);
            finish()

        }
        val aaChartView = findViewById<AAChartView>(R.id.aa_chart_view)
        val aaChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Line)
            .title("title")
            .subtitle("subtitle")
            .backgroundColor("#4b2b7f")
            .dataLabelsEnabled(true)
            .categories(data.dateArray.toTypedArray())
            .series(arrayOf(
                AASeriesElement()
                    .name("Temperature/"+sensor.name1)
                    .data(data.tempArray.toTypedArray()),
                AASeriesElement()
                    .name("Humidity/"+sensor.name1)
                    .data(data.humArray.toTypedArray()),
                AASeriesElement()
                    .name("Temperature/"+sensor.name2)
                    .data(data.tempArray2.toTypedArray()),
                AASeriesElement()
                    .name("Humidity/"+sensor.name2)
                    .data(data.humArray2.toTypedArray())
            )
            )

        aaChartView.aa_drawChartWithChartModel(aaChartModel)
        Log.d(TAG, data.humArray.toString())
    }

    fun menuHidden(){
        data.menuOpen = false
        ui.smallMenu.setBackgroundResource(R.drawable.smallmenu);

        hideIcos()
        ui.menuLines.setVisibility(View.VISIBLE)
    }
    fun menuOpened(){
        showIcos()
        data.menuOpen = true
        ui.smallMenu.setBackgroundResource(R.drawable.longmenu);

    }

    fun showIcos(){
        ui.menuLines.setVisibility(View.GONE);
        ui.menuHide.setVisibility(View.VISIBLE);
        ui.menuSettings.setVisibility(View.VISIBLE);
        ui.menuHome.setVisibility(View.VISIBLE);
        ui.menuMessage.setVisibility(View.VISIBLE);
        ui.menuGraph.setVisibility(View.VISIBLE);
    }
    fun hideIcos(){
        ui.menuHide.setVisibility(View.GONE);
        ui.menuSettings.setVisibility(View.GONE);
        ui.menuHome.setVisibility(View.GONE);
        ui.menuMessage.setVisibility(View.GONE);
        ui.menuGraph.setVisibility(View.GONE);
    }
    }
interface AAChartViewCallBack {
    fun chartViewMoveOverEventMessage(aaChartView: AAChartView, messageModel: AAMoveOverEventMessageModel)
}