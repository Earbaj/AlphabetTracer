package org.tensorflow.lite.examples.digitclassifier

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import org.tensorflow.lite.Interpreter

class DigitClassifier(private val context: Context) {

  private  val TAG = "DigitClassifier"

  private  val MODEL_FILE = "model_alp.tflite"

  private  val FLOAT_TYPE_SIZE = 4
  private  val PIXEL_SIZE = 1

  private  val OUTPUT_CLASSES_COUNT = 26


  private var interpreter: Interpreter? = null
  var isInitialized = false
    private set

  /** Executor to run inference task in the background */
  private val executorService: ExecutorService = Executors.newCachedThreadPool()

  private var inputImageWidth: Int = 0 // will be inferred from TF Lite model
  private var inputImageHeight: Int = 0 // will be inferred from TF Lite model
  private var modelInputSize: Int = 0 // will be inferred from TF Lite model

  fun initialize(): Task<Void> {
    val task = TaskCompletionSource<Void>()
    executorService.execute {
      try {
        initializeInterpreter()
        task.setResult(null)
      } catch (e: IOException) {
        task.setException(e)
      }
    }
    return task.task
  }

  @Throws(IOException::class)
  private fun initializeInterpreter() {
    // Load the TF Lite model
    val assetManager = context.assets
    val model = loadModelFile(assetManager)

    // Initialize TF Lite Interpreter with NNAPI enabled
    val options = Interpreter.Options()
    options.setUseNNAPI(true)
    val interpreter = Interpreter(model, options)

    // Read input shape from model file
    val inputShape = interpreter.getInputTensor(0).shape()
    inputImageWidth = inputShape[1]
    inputImageHeight = inputShape[2]
    modelInputSize = FLOAT_TYPE_SIZE * inputImageWidth * inputImageHeight * PIXEL_SIZE

    // Finish interpreter initialization
    this.interpreter = interpreter
    isInitialized = true
    Log.d(TAG, "Initialized TFLite interpreter.")
  }

  @Throws(IOException::class)
  private fun loadModelFile(assetManager: AssetManager): ByteBuffer {
    val fileDescriptor = assetManager.openFd(MODEL_FILE)
    val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
    val fileChannel = inputStream.channel
    val startOffset = fileDescriptor.startOffset
    val declaredLength = fileDescriptor.declaredLength
    return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
  }

  private fun classify(bitmap: Bitmap): String {
    if (!isInitialized) {
      throw IllegalStateException("TF Lite Interpreter is not initialized yet.")
    }

    var startTime: Long
    var elapsedTime: Long

    // Preprocessing: resize the input
    startTime = System.nanoTime()
    val resizedImage = Bitmap.createScaledBitmap(bitmap, inputImageWidth, inputImageHeight, true)
    val byteBuffer = convertBitmapToByteBuffer(resizedImage)
    elapsedTime = (System.nanoTime() - startTime) / 1000000
    Log.d(TAG, "Preprocessing time = " + elapsedTime + "ms")

    startTime = System.nanoTime()
    val result = Array(1) { FloatArray(OUTPUT_CLASSES_COUNT) }
    interpreter?.run(byteBuffer, result)
    elapsedTime = (System.nanoTime() - startTime) / 1000000
    Log.d(TAG, "Inference time = " + elapsedTime + "ms")

    return getOutputString(result[0])
  }

  fun classifyAsync(bitmap: Bitmap): Task<String> {
    val task = TaskCompletionSource<String>()
    executorService.execute {
      val result = classify(bitmap)
      task.setResult(result)
    }
    return task.task
  }

  fun close() {
    executorService.execute {
      interpreter?.close()
      Log.d(TAG, "Closed TFLite interpreter.")
    }
  }

  private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
    val byteBuffer = ByteBuffer.allocateDirect(modelInputSize)
    byteBuffer.order(ByteOrder.nativeOrder())

    val pixels = IntArray(inputImageWidth * inputImageHeight)
    bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

    for (pixelValue in pixels) {
      val r = (pixelValue shr 16 and 0xFF)
      val g = (pixelValue shr 8 and 0xFF)
      val b = (pixelValue and 0xFF)

      // Convert RGB to grayscale and normalize pixel value to [0..1]
      val normalizedPixelValue = (r + g + b) / 3.0f / 255.0f
      byteBuffer.putFloat(normalizedPixelValue)
    }

    return byteBuffer
  }

  private fun getOutputString(output: FloatArray): String {
    val maxIndex = output.indices.maxBy { output[it] } ?: -1
    //return "Prediction Result: %d\nConfidence: %2f".format(maxIndex, output[maxIndex])
    if(maxIndex == 0){
      return "A"
    }else if(maxIndex == 1){
      return "B"
    }else if(maxIndex == 2){
      return "C"
    }else if(maxIndex == 3){
      return "D"
    }else if(maxIndex == 4){
      return "E"
    }else if(maxIndex == 5){
      return "F"
    }else if(maxIndex == 6){
      return "G"
    }else if(maxIndex == 7){
      return "H"
    }else if(maxIndex == 8){
      return "I"
    }else if(maxIndex == 9){
      return "J"
    }else if(maxIndex == 10){
      return "K"
    }else if(maxIndex == 11){
      return "L"
    }else if(maxIndex == 12){
      return "M"
    }else if(maxIndex == 13){
      return "N"
    }else if(maxIndex == 14){
      return "O"
    }else if(maxIndex == 15){
      return "P"
    }else if(maxIndex == 16){
      return "Q"
    }else if(maxIndex == 17){
      return "R"
    }else if(maxIndex == 18){
      return "S"
    }else if(maxIndex == 19){
      return "T"
    }else if(maxIndex == 20){
      return "U"
    }else if(maxIndex == 21){
      return "V"
    }else if(maxIndex == 22){
      return "W"
    }else if(maxIndex == 23){
      return "X"
    }else if(maxIndex == 24){
      return "Y"
    }else if(maxIndex == 25){
      return "Z"
    }
    return "Nothing Detected"
  }

//  companion object {
//    private const val TAG = "DigitClassifier"
//
//    private const val MODEL_FILE = "model_alp.tflite"
//
//    private const val FLOAT_TYPE_SIZE = 4
//    private const val PIXEL_SIZE = 1
//
//    private const val OUTPUT_CLASSES_COUNT = 26
//  }
}
