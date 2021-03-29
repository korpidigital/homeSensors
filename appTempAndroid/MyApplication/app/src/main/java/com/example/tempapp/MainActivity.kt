package com.example.tempapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.tempapp.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


class MainActivity : AppCompatActivity(){

    private val TAG = "MyActivity"

    private lateinit var ui: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityMainBinding.inflate(layoutInflater)
        val view = ui.root
        setContentView(view)


        // fetch reference database
        val mDatabase = FirebaseDatabase.getInstance()
        val ref = mDatabase.getReference()
        //val refArduino = mDatabase.getReference("Arduino")


        //listerner
        val valueListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                fetchFirebase(dataSnapshot)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        }





        if (data.menuOpen){
            menuOpened()
        }
        else{
            //hide Icos
            hideIcos()
        }
        // set on-click listener for ImageView UPDATE
        ref.addValueEventListener(valueListener)
        ui.btnUpdate.setOnClickListener {
            //set arduino state ON (arduino starts measurement)
            ref.child("Room1/Mode").setValue("ON");
            ref.child("Room2/Mode").setValue("ON");

            //refArduino.setValue("ON")
            ui.btnUpdate.setBackgroundResource(R.drawable.updatebtn)

        }


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
        ui.menuGraph.setOnClickListener(){
            Log.d(TAG, "click graph")
            val intent = Intent(this, activity_graph::class.java)
            changeView.saveOnChange(intent)
            startActivity(intent)
            this.overridePendingTransition(0, 0);
            finish()

        }
        ref.child("Room1/Mode").setValue("OFF");
        ref.child("Room2/Mode").setValue("OFF");


    }

    fun fetchFirebase(dataSnapshot: DataSnapshot){
        //sensor.t1 = dataSnapshot.child("Room1/Temp").getChildren().last().getValue() as Long
        //sensor.h1 =  dataSnapshot.child("Room1/Hum").getChildren().last().getValue() as Long
        data.tempArray.clear()
        data.humArray.clear()
        data.dateArray.clear()
        data.tempArray2.clear()
        data.humArray2.clear()
        data.dateArray2.clear()
        for (item_snapshot in dataSnapshot.child("Room1").children) {
            var key = item_snapshot.key.toString()
            var value = item_snapshot.value
            if (key.contains("Temp")){
                data.tempArray.add(value as Long)
            }
            if (key.contains("Hum")){
                data.humArray.add(value as Long)
                var strDate: String = key.replace("_Hum", "")
                data.dateArray.add(strDate)
            }
        }
        for (item_snapshot in dataSnapshot.child("Room2").children) {
            var key = item_snapshot.key.toString()
            var value = item_snapshot.value
            if (key.contains("Temp")){
                data.tempArray2.add(value as Long)
            }
            if (key.contains("Hum")){
                data.humArray2.add(value as Long)
                var strDate: String = key.replace("_Hum", "")
                data.dateArray2.add(strDate)
            }
        }
        Log.d(TAG, sensor.name1.toString())
        Log.d(TAG, sensor.name2.toString())



        var newTemp = data.tempArray.last()
        var newHum = data.humArray.last()
        var newTemp2 = data.tempArray2.last()
        var newHum2= data.humArray2.last()

        sensor.t1 = newTemp as Long
        sensor.h1 =  newHum as Long
        sensor.t2 = newTemp2 as Long
        sensor.h2 =  newHum2 as Long
        sensor.name1 = dataSnapshot.child("Room1/Name").getValue() as String
        sensor.name2 = dataSnapshot.child("Room2/Name").getValue() as String

        //I have to remove mode values from firebase so Arduino loop does not download it each second to check if its ON or OFF and use up my limited firebase bandwidth
        if(dataSnapshot.child("Room1/Mode").exists()){
            sensor.mode1 = dataSnapshot.child("Room1/Mode").getValue() as String
        }
        else{
            sensor.mode1 == "OFF"
        }
        if(dataSnapshot.child("Room2/Mode").exists()){
            sensor.mode2 = dataSnapshot.child("Room2/Mode").getValue() as String
        }
        else{
            sensor.mode2 == "OFF"
        }
        sensor.measuredTime = dataSnapshot.child("Time").getValue() as String
        sensor.measuredDate = dataSnapshot.child("Date").getValue() as String
        Log.d(TAG, "in value listener")
        Log.d(TAG, "mode1: "+ sensor.mode1)
        Log.d(TAG, "mode2: "+ sensor.mode2)
        if (sensor.mode1 == "ON" || sensor.mode2 == "ON"){
            ui.btnUpdate.setImageResource(R.drawable.connecting2)
        }
        else if (sensor.mode1 == "OFF" && sensor.mode2 == "OFF" ){
            ui.btnUpdate.setImageResource(R.drawable.updatebtn)
            FirebaseDatabase.getInstance().getReference().child("Room1/Mode").removeValue();
            FirebaseDatabase.getInstance().getReference().child("Room2/Mode").removeValue();
        }
        //Chanfe UI
        ui.temp1.text = sensor.t1.toString()+" °C"
        ui.hum1.text = sensor.h1.toString()+" %"
        ui.temp2.text = sensor.t2.toString()+" °C"
        ui.hum2.text = sensor.h2.toString()+" %"
        ui.txtroom1.text = sensor.name1.toString()
        ui.txtroom2.text = sensor.name2.toString()
        ui.measuredTime.text = "MEASURED\n"+sensor.measuredTime+"\n"+sensor.measuredDate


    }
    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart")
    }
    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume")
    }
    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause")
    }
    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop")
    }
    override fun onRestart() {
        super.onRestart()
        Log.i(TAG, "onRestart")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
        //onRestoreInstanceState never gets called
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i(TAG, "onRestoreInstanceState")
    }
    fun menuHidden(){
        data.menuOpen = false
        ui.smallMenu.setBackgroundResource(R.drawable.smallmenu);

        hideIcos()
        ui.menuLines.setVisibility(View.VISIBLE)
        ui.title1.setVisibility(View.VISIBLE)
    }
    fun menuOpened(){
        showIcos()
        data.menuOpen = true
        ui.smallMenu.setBackgroundResource(R.drawable.longmenu);

        ui.title1.setVisibility(View.INVISIBLE)


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






