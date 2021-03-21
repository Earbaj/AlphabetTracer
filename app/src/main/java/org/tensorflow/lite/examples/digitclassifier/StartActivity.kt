package org.tensorflow.lite.examples.digitclassifier

import `in`.codeshuffle.typewriterview.TypeWriterView
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.*
import kotlinx.android.synthetic.main.activity_start.*
import kotlinx.android.synthetic.main.spinner_layout.*
import org.tensorflow.lite.examples.digitclassifier.all.activity.BActivity
import org.tensorflow.lite.examples.digitclassifier.all.activity.CActivity
import pl.droidsonroids.gif.GifImageView

class StartActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var mediaPlayer: MediaPlayer? = null
    var spinnr: Spinner? = null
    var button: ImageButton? = null
    var text: TextView? = null
    var res: String = ""
    var gifimageView: GifImageView? = null
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        gifimageView = GifImageView(this)
        gifimageView = findViewById(R.id.gif_img_v)
        spinnr = findViewById(R.id.spinner)
        button = findViewById(R.id.imageButton2)
        text = findViewById(R.id.textView3)
        text?.visibility = View.INVISIBLE
        spinnr?.visibility = View.INVISIBLE
        button?.visibility = View.INVISIBLE
        // Spinner Adapter
        ArrayAdapter.createFromResource(this,
            R.array.alphabet_array,
            android.R.layout.simple_list_item_1)?.also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
            spinnr?.adapter = adapter
            }
        gifimageView?.setImageResource(R.drawable.startnum)
        mediaPlayer = MediaPlayer.create(this, R.raw.backgroundmusic)
        mediaPlayer?.start()
        // Start up Animation
        gifimageView?.animate()?.apply {
            duration = 5000
        }?.withEndAction(){
//            val intent = Intent(this, MainActivity::class.java)
//            mediaPlayer?.pause()
//            startActivity(intent)
            //gifimageView?.visibility = View.GONE
            gifimageView?.alpha = 0.3F
            text?.visibility = View.VISIBLE
            spinnr?.visibility = View.VISIBLE
            button?.visibility = View.VISIBLE
        }?.start()


        //Spinner listener
        spinnr?.onItemSelectedListener = this

        //Button listener
        button?.setOnClickListener(){
            if(res == "Alphabet A-Z"){
                val intent = Intent(this, MainActivity::class.java)
                mediaPlayer?.pause()
                startActivity(intent)
                finish()
            }
        }
    }


    override fun onRestart() {
        mediaPlayer?.start()
        super.onRestart()
    }

    override fun onPause() {
        mediaPlayer?.pause()
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onStart() {
        mediaPlayer?.start()
        super.onStart()
    }

    override fun onDestroy() {
        mediaPlayer?.stop()
        super.onDestroy()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        res = p0?.getItemAtPosition(p2).toString()
    }
}