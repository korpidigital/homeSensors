package com.example.tempapp

import android.content.Intent
import android.os.Bundle



public class Sensors {
    var t1: Long = 0
    var h1: Long = 0
    var t2: Long = 0
    var h2: Long = 0
    var t3: Long = 0
    var h3: Long = 0
    var measuredTime: String? ="00:00"
    var measuredDate: String? ="00-00-0000"
    var mode1 = "OFF"
    var mode2 = "OFF"
    var name1: String? = "Room1"
    var name2: String? = "Room2"

}
val sensor = Sensors()

public class dataArray{

    val humArray = arrayListOf<Long>()
    val tempArray = arrayListOf<Long>()
    val dateArray = arrayListOf<String>()
    val humArray2 = arrayListOf<Long>()
    val tempArray2 = arrayListOf<Long>()
    val dateArray2 = arrayListOf<String>()
    var menuOpen: Boolean = false

}
val data = dataArray()

public class changeActivity {
    fun saveOnChange(intent: Intent){
        intent.putExtra("menuOpen", true)
        /*
        intent.putExtra("temp1", sensor.t1)
        intent.putExtra("hum1", sensor.h1)
        intent.putExtra("time", sensor.measuredTime)
        intent.putExtra("date", sensor.measuredDate)
        intent.putExtra("name1", sensor.name1)
        intent.putExtra("name2", sensor.name2)*/

    }
    fun loadSaved(bundle: Bundle){
        /*
        sensor.t1 = bundle.getLong("temp1")
        sensor.h1 = bundle.getLong("hum1")
        sensor.measuredTime =  bundle.getString("time")
        sensor.measuredDate =  bundle.getString("date")
        sensor.name1 =  bundle.getString("name1")
        sensor.name1 =  bundle.getString("name2")*/


    }
}
val changeView = changeActivity()





