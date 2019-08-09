package soheilkd.BurninWiper

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.preference.PreferenceManager
import android.view.View
import kotlinx.android.synthetic.main.activity_color.*
import java.util.*
import kotlin.concurrent.timerTask

class ColorActivity : AppCompatActivity() {
	private var currentColors: Array<ColorDrawable> = arrayOf()
	private var shutdownTimer = Timer()
	private var colorThread = Thread(Runnable {
		while(true){
			for(color in currentColors){
				runOnUiThread { ColorFrameLayout.background = color }
				SystemClock.sleep(1250)
			}
		}
	})

	private fun getColorArrayById(id: Int): Array<ColorDrawable>{
		return when (id){
			R.id.modeRadioButton1 -> getColorArray(0)
			R.id.modeRadioButton2 -> getColorArray(1)
			R.id.modeRadioButton3 -> getColorArray(2)
			else -> getColorArray(-1)
		}
	}
	private fun getColorArray(index: Int): Array<ColorDrawable>{
		val white by lazy { ColorDrawable(Color.WHITE) }
		val red by lazy { ColorDrawable(Color.RED) }
		val green by lazy { ColorDrawable(Color.GREEN) }
		val blue by lazy { ColorDrawable(Color.BLUE) }
		val black by lazy { ColorDrawable(Color.BLACK) }
		val gray by lazy { ColorDrawable(Color.DKGRAY) }

		return when (index){
			0 -> arrayOf(white, red, green, blue, black)
			1 -> arrayOf(white, black)
			2 -> arrayOf(white)
			else -> arrayOf(gray)
		}
	}

	private fun start(){
		makeAppImmersive()
		applyPreferences()
		 colorThread.start()
	}
	private fun makeAppImmersive(){
		window.setFlags(128, 128)
		ColorFrameLayout.systemUiVisibility =
			View.SYSTEM_UI_FLAG_LOW_PROFILE or
					View.SYSTEM_UI_FLAG_FULLSCREEN or
					View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
					View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
					View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
					View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
	}
	private fun applyPreferences(){
		if (intent.getBooleanExtra("burninwiper.ischecking", false)){
			currentColors = getColorArray(-1)
			return
		}
		//The comparisions to true are for casting the Boolean? expressions to Boolean
		if (intent.action?.startsWith("soheilkd.BurninWiper.") == true){
			if (intent.action?.endsWith("runfirstmode") == true)
				currentColors = getColorArray(0)
			else if (intent.action?.endsWith("runsecondmode") == true)
				currentColors = getColorArray(1)
			else
				currentColors = getColorArray(2)
		}
		if (currentColors.count() == 0)
			currentColors = getColorArrayById(intent.getIntExtra("burninwiper.colormode", 0))
		setShutdownTimer(intent.getIntExtra("burninwiper.timerindex", 0))
	}
	private fun setShutdownTimer(index: Int){
		val timerTask = timerTask {
			finish()
		}
		fun minToMs(m: Int): Long {return  m*60L*1000}
		when(index) {
			1 -> shutdownTimer.schedule(timerTask, minToMs(5))
			2 -> shutdownTimer.schedule(timerTask, minToMs(10))
			3 -> shutdownTimer.schedule(timerTask, minToMs(20))
			4 -> shutdownTimer.schedule(timerTask, minToMs(30))
			5 -> shutdownTimer.schedule(timerTask, minToMs(60))
			6 -> shutdownTimer.schedule(timerTask, minToMs(120))
			7 -> shutdownTimer.schedule(timerTask, minToMs(180))
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setContentView(R.layout.activity_color)

		ColorFrameLayout.setOnTouchListener{ _, _ ->
			finish()
			true
		}

		start()
	}
}
