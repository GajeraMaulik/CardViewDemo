package com.example.cardviewdemo.Progressbar


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.cardviewdemo.R
import com.google.android.material.progressindicator.LinearProgressIndicator


//import kotlin.androidx.synthetic.main.activity_progress_dialog.*

class ProgressDialogActivity : AppCompatActivity() {
    private lateinit var isdialog: AlertDialog
    private lateinit var isprogress: ProgressBar


    @SuppressLint("UseSwitchCompatOrMaterialCode", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress_dialog)
        val actionBar = supportActionBar
        actionBar!!.title = "Dialog Demo"
        actionBar.setDisplayHomeAsUpEnabled(true)

        val b1 = findViewById<Button>(R.id.button)
        val b2 = findViewById<Button>(R.id.button2)
        val progin=findViewById<LinearProgressIndicator>(R.id.progIn)
        val rb=findViewById<RatingBar>(R.id.ratingBar)
        val sb=findViewById<SeekBar>(R.id.sb_seekBar)
        val rb_textView=findViewById<TextView>(R.id.rb_textView)
        val sb_textView=findViewById<TextView>(R.id.sb_textView)
        val auto=findViewById<Switch>(R.id.auto)
        val on=findViewById<Switch>(R.id.on)
        val rg=findViewById<RadioGroup>(R.id.rg)
        val ad=findViewById<RadioButton>(R.id.ad)
        val ios=findViewById<RadioButton>(R.id.ios)





        Handler(Looper.getMainLooper()).postDelayed({
           b1.visibility=View.VISIBLE
           b2.visibility=View.VISIBLE
           rb.visibility=View.VISIBLE
            sb.visibility=View.VISIBLE
           rb_textView.visibility=View.VISIBLE
            sb_textView.visibility=View.VISIBLE
            auto.visibility=View.VISIBLE
            on.visibility=View.VISIBLE
           ad.visibility=View.VISIBLE
            ios.visibility=View.VISIBLE
            rg.visibility=View.VISIBLE
           progin.visibility=View.GONE }, 1000)

        b1.setOnClickListener(View.OnClickListener {
            val bulider = AlertDialog.Builder(this)
             val dialogView = this.layoutInflater.inflate(R.layout.loading_item, null)
            val message = dialogView.findViewById<TextView>(R.id.message)
            message.text = "Please Wait..."
            bulider.setView(dialogView)
            bulider.setCancelable(false)
            isdialog = bulider.create()
            isdialog.show()
            isdialog.setCancelable(false)
            Thread {
                try {
                    Thread.sleep(2000)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                isdialog.dismiss()
            }.start()

        })


        b2.setOnClickListener {
            var pd=ProgressDialog(this)
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            pd.setTitle("Download")
            pd.setCancelable(false)
           // pd.setDismissMessage("Downloading..")
            pd.show()

            Thread(Runnable {
                var count=0
                while (count<=100){
                    count +=10
                    pd.setProgress(count)
                    Thread.sleep(1000)
                }
                if(count>=100){
                    pd.dismiss()
                }
            }).start()

        }

        rb.setOnRatingBarChangeListener { ratingBar, f1, b ->
            rb_textView.text = "Rating = $f1"
        }

        sb.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                sb_textView.text = "Value = $p1"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        
        auto.setOnCheckedChangeListener { buttonView, b ->
            if (auto.isChecked){
                Toast.makeText(this,"AutoPlay Selected",Toast.LENGTH_LONG).show()
            }
        }

        on.setOnCheckedChangeListener { buttonView, b ->
            if (on.isChecked){
                Toast.makeText(this,"On Selected",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,"Off Selected",Toast.LENGTH_LONG).show()
            }
        }

         ad.setOnClickListener {
             if (ad.isChecked) {
                 Toast.makeText(this, "You are android Developer", Toast.LENGTH_SHORT).show()
             }

         }

        ios.setOnClickListener {
            if(ios.isChecked)
            {
                Toast.makeText(this,"You are ios Developer",Toast.LENGTH_SHORT).show()
            }
         }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}


