package org.tensorflow.lite.examples.digitclassifier.all.activity

import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.divyanshu.draw.widget.DrawView
import org.tensorflow.lite.examples.digitclassifier.DigitClassifier
import org.tensorflow.lite.examples.digitclassifier.R
import pl.droidsonroids.gif.GifImageView

class DActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    var imageView: GifImageView? = null
    var gifImageView: GifImageView? = null
    var editText: EditText? = null
    var undowbutton: ImageButton? = null
    var nextButton: ImageButton? = null
    var playagain: ImageButton? = null
    var gotoButton: Button? = null
    var num: Int = 0;
    var color_string = "#8BC34A"
    private val TAG = "MainActivity"
    private var result: String = ""
    private var drawView: DrawView? = null
    private var digitClassifier = DigitClassifier(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_d)
        mediaPlayer = MediaPlayer.create(this, R.raw.d)
        imageView = GifImageView(this)
        gifImageView = GifImageView(this)
        gifImageView = findViewById(R.id.gif_img_v)
        imageView = findViewById(R.id.img_va)
        playagain = findViewById(R.id.btn_ply_again)
        imageView?.setImageResource(R.drawable.newd)
        gifImageView?.setImageResource(R.drawable.dtrace)
        playagain?.visibility = View.INVISIBLE
        // Setup view instances
        var myColor = Color.parseColor(color_string)
        drawView = findViewById(R.id.draw_view)
        drawView?.setStrokeWidth(70.0f)
        drawView?.setColor(Color.WHITE)
        drawView?.setBackgroundColor(myColor)
        //predictedTextView = findViewById(R.id.predicted_text)
        editText = findViewById(R.id.et_class_tx)
        undowbutton = findViewById(R.id.img_again)
        nextButton = findViewById(R.id.img_next)
        gotoButton = findViewById(R.id.btn_goto)
        // Setup clear drawing button

        // Setup classification trigger so that it classify after every stroke drew
        drawView?.setOnTouchListener { _, event ->
            // As we have interrupted DrawView's touch event,
            // we first need to pass touch events through to the instance for the drawing to show up
            drawView?.onTouchEvent(event)

            // Then if user finished a touch event, run classification
            if (event.action == MotionEvent.ACTION_UP) {
                num++
                if(num == 2){
                    classifyDrawing()
//          visibleApperance()
//          imageView?.animate()?.apply {
//            duration = 3000
//            rotationYBy(360f)
//          }?.start()
                }
            }

            true
        }

        undowbutton?.setOnClickListener(){
            invisibleApperance()
        }
        nextButton?.setOnClickListener(){
            val intent = Intent(this, EActivity::class.java)
            startActivity(intent)
            finish()
            mediaPlayer?.stop()
        }
        gotoButton?.setOnClickListener(){
            var res = editText?.text.toString()
            selectAlphabet(res)
        }
        playagain?.setOnClickListener(){
            mediaPlayer?.start()
        }
        // Setup digit classifier
        digitClassifier
            .initialize()
            .addOnFailureListener { e -> Log.e(TAG, "Error to setting up digit classifier.", e) }
    }

    fun visibleApperance(){
        imageView?.visibility = View.VISIBLE
        drawView?.visibility = View.INVISIBLE
        gifImageView?.visibility = View.INVISIBLE
        editText?.visibility = View.VISIBLE
        undowbutton?.visibility = View.VISIBLE
        gotoButton?.visibility = View.VISIBLE
        nextButton?.visibility = View.VISIBLE
        playagain?.visibility = View.VISIBLE
    }

    fun invisibleApperance(){
        drawView?.clearCanvas()
        imageView?.visibility = View.INVISIBLE
        drawView?.visibility = View.VISIBLE
        gifImageView?.visibility = View.VISIBLE
        editText?.visibility = View.INVISIBLE
        undowbutton?.visibility = View.INVISIBLE
        gotoButton?.visibility = View.INVISIBLE
        nextButton?.visibility = View.INVISIBLE
        playagain?.visibility = View.INVISIBLE
        num = 0;
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.clera_bt -> {
                drawView?.clearCanvas()
                //predictedTextView?.text = getString(R.string.tfe_dc_prediction_text_placeholder)
                num = 0
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        digitClassifier.close()
        super.onDestroy()
    }

    private fun classifyDrawing() {
        val bitmap = drawView?.getBitmap()

        if ((bitmap != null) && (digitClassifier.isInitialized)) {
            digitClassifier
                .classifyAsync(bitmap)
                .addOnSuccessListener {
                        resultText -> result = resultText
                    //predictedTextView?.text = result
                    if(result == "D"){
                        visibleApperance()
                        mediaPlayer?.start()
                    }
                }
                .addOnFailureListener { e ->
//          predictedTextView?.text = getString(
//            R.string.tfe_dc_classification_error_message,
//            e.localizedMessage
//          )
                    Log.e(TAG, "Error classifying drawing.", e)
                }
        }
        //predictedTextView?.text = result
    }

    fun selectAlphabet(name: String){
        if(name == "B"){
            val intent = Intent(this, BActivity::class.java)
            startActivity(intent)
            finish()
            mediaPlayer?.stop()
        }else if(name == "C"){
            val intent = Intent(this, CActivity::class.java)
            mediaPlayer?.pause()
            startActivity(intent)
            finish()
        }else if(name == "A"){
            val intent = Intent(this, DActivity::class.java)
            mediaPlayer?.pause()
            startActivity(intent)
            finish()
        }else if(name == "E"){
            val intent = Intent(this, EActivity::class.java)
            mediaPlayer?.pause()
            startActivity(intent)
            finish()
        }else if(name == "F"){
            val intent = Intent(this, FActivity::class.java)
            mediaPlayer?.pause()
            startActivity(intent)
            finish()
        }else if(name == "G"){
            val intent = Intent(this, GActivity::class.java)
            mediaPlayer?.pause()
            startActivity(intent)
            finish()
        }else if(name == "H"){
            val intent = Intent(this, HActivity::class.java)
            mediaPlayer?.pause()
            startActivity(intent)
            finish()
        }else if(name == "I"){
            val intent = Intent(this, IActivity::class.java)
            mediaPlayer?.pause()
            startActivity(intent)
            finish()
        }else if(name == "J"){
            val intent = Intent(this, JActivity::class.java)
            mediaPlayer?.pause()
            startActivity(intent)
            finish()
        }else if(name == "K"){
            val intent = Intent(this, KActivity::class.java)
            mediaPlayer?.pause()
            startActivity(intent)
            finish()
        }else if(name == "L"){
            val intent = Intent(this, LActivity::class.java)
            mediaPlayer?.pause()
            startActivity(intent)
            finish()
        }else if(name == "M"){
            val intent = Intent(this, MActivity::class.java)
            mediaPlayer?.pause()
            startActivity(intent)
            finish()
        }else if(name == "N"){
            val intent = Intent(this, NActivity::class.java)
            mediaPlayer?.pause()
            startActivity(intent)
            finish()
        }else if(name == "O"){
            val intent = Intent(this, OActivity::class.java)
            mediaPlayer?.pause()
            startActivity(intent)
            finish()
        }else if(name == "P"){
            val intent = Intent(this, PActivity::class.java)
            mediaPlayer?.pause()
            startActivity(intent)
            finish()
        }else if(name == "Q"){
            val intent = Intent(this, QActivity::class.java)
            mediaPlayer?.pause()
            startActivity(intent)
            finish()
        }else if(name == "R"){
            val intent = Intent(this, RActivity::class.java)
            mediaPlayer?.pause()
            startActivity(intent)
            finish()
        }else if(name == "S"){
            val intent = Intent(this, SActivity::class.java)
            mediaPlayer?.pause()
            startActivity(intent)
            finish()
        }else if(name == "T"){
            val intent = Intent(this, TActivity::class.java)
            mediaPlayer?.pause()
            startActivity(intent)
            finish()
        }else if(name == "U"){
            val intent = Intent(this, UActivity::class.java)
            mediaPlayer?.pause()
            startActivity(intent)
            finish()
        }else if(name == "V"){
            val intent = Intent(this, VActivity::class.java)
            mediaPlayer?.pause()
            startActivity(intent)
            finish()
        }else if(name == "W"){
            val intent = Intent(this, WActivity::class.java)
            mediaPlayer?.pause()
            startActivity(intent)
            finish()
        }else if(name == "X"){
            val intent = Intent(this, XActivity::class.java)
            mediaPlayer?.pause()
            startActivity(intent)
            finish()
        }else if(name == "Y"){
            val intent = Intent(this, YActivity::class.java)
            mediaPlayer?.pause()
            startActivity(intent)
            finish()
        }else if(name == "Z"){
            val intent = Intent(this, ZActivity::class.java)
            mediaPlayer?.pause()
            startActivity(intent)
            finish()
        }
    }

}
