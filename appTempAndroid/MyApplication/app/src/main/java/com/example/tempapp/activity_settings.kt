package com.example.tempapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.widget.doAfterTextChanged
import com.example.tempapp.databinding.ActivitySettingsBinding
import com.google.firebase.database.*



class activity_settings : AppCompatActivity() {

    private val TAG = "MyActivity"
    private lateinit var ui: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        ui = ActivitySettingsBinding.inflate(layoutInflater)
        val view = ui.root
        setContentView(view)

        ui.settingsInput1.setText(sensor.name1)
        ui.settingsInput2.setText(sensor.name2)
        Log.d(TAG, sensor.name1.toString())
        Log.d(TAG, sensor.name2.toString())


        if (data.menuOpen){
            menuOpen()
        }

        //Listerners
        ui.menuLines.setOnClickListener {
            menuOpen()
        }
        ui.menuHide.setOnClickListener() {
            menuHidden()
        }

        ui.menuHome.setOnClickListener(){
            Log.d(TAG, "click home")
            val intent = Intent(this, MainActivity::class.java)
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
        //change room names
        ui.settingsInput1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int,count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int,before: Int, count: Int) {

                sensor.name1 = ui.settingsInput1.getText().toString();
                FirebaseDatabase.getInstance().getReference().child("Room1/Name").setValue(sensor.name1)
            }
        })
        ui.settingsInput2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int,count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int,before: Int, count: Int) {

                sensor.name2 = ui.settingsInput2.getText().toString();
                FirebaseDatabase.getInstance().getReference().child("Room2/Name").setValue(sensor.name2)
            }
        })

    }







    fun menuOpen(){
        data.menuOpen = true
        ui.smallMenu.setBackgroundResource(R.drawable.longmenu);
        showIcos()
    }
    fun menuHidden(){
        data.menuOpen = false
        ui.smallMenu.setBackgroundResource(R.drawable.smallmenu);
        hideIcos()
        ui.menuLines.setVisibility(View.VISIBLE)
    }
    fun hideIcos() {
        ui.menuHide.setVisibility(View.GONE);
        ui.menuSettings.setVisibility(View.GONE);
        ui.menuHome.setVisibility(View.GONE);
        ui.menuMessage.setVisibility(View.GONE);
        ui.menuGraph.setVisibility(View.GONE);
    }

    fun showIcos() {
        ui.menuLines.setVisibility(View.GONE);
        ui.menuHide.setVisibility(View.VISIBLE);
        ui.menuSettings.setVisibility(View.VISIBLE);
        ui.menuHome.setVisibility(View.VISIBLE);
        ui.menuMessage.setVisibility(View.VISIBLE);
        ui.menuGraph.setVisibility(View.VISIBLE);
    }
}