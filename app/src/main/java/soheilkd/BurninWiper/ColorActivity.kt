package soheilkd.BurninWiper

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceManager
import android.view.View
import kotlinx.android.synthetic.main.activity_color.*
import java.util.*
import kotlin.concurrent.timerTask

class ColorActivity : AppCompatActivity() {
	private var currentColors: Array<ColorDrawable> = arrayOf()
	private var colorThread = Thread(Runnable {
		while(true){
			for(color in currentColors){
				runOnUiThread { ColorFrameLayout.background = color }
				Thread.sleep(1250)
			}
		}
	})
	 private fun start(){
		window.setFlags(128, 128)
		applyPreferences()
		makeAppImmersive()
		 colorThread.start()
	}
	private fun makeAppImmersive(){
		ColorFrameLayout.systemUiVisibility =
			View.SYSTEM_UI_FLAG_LOW_PROFILE or
					View.SYSTEM_UI_FLAG_FULLSCREEN or
					View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
					View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
					View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
					View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
	}
	private var shutdownTimer = Timer()
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

	private fun getColorArray(id: Int): Array<ColorDrawable>{
		val white by lazy { ColorDrawable(Color.WHITE) }
		val red by lazy { ColorDrawable(Color.RED) }
		val green by lazy { ColorDrawable(Color.GREEN) }
		val blue by lazy { ColorDrawable(Color.BLUE) }
		val black by lazy { ColorDrawable(Color.BLACK) }
		val gray by lazy { ColorDrawable(Color.DKGRAY) }
		return when (id){
			1 -> arrayOf(white,red,green,blue,black)
			2 -> arrayOf(white,black)
			3 -> arrayOf(white)
			else -> arrayOf(gray)
		}
	}
	private fun setColors(id: Int) {
	currentColors = getColorArray(id)
	}
	private fun applyPreferences(){
		val prefs = PreferenceManager.getDefaultSharedPreferences(this)
		if (prefs.getBoolean("IsChecking", false)){
			val edit = prefs.edit()
			edit.putBoolean("IsChecking", false)
			edit.apply()
			setColors(4)
			return
		}
		setColors(prefs.getInt("ModeIndex", 0))
		setShutdownTimer(prefs.getInt("TimerIndex" , 0))
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setContentView(R.layout.activity_color)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)

		ColorFrameLayout.setOnTouchListener{ _, _ ->
			finish()
			true
		}
		start()
	}
/*
	private fun hide() {
		// Hide UI first
		supportActionBar?.hide()
		fullscreen_content_controls.visibility = View.GONE
		mVisible = false

		// Schedule a runnable to remove the status and navigation bar after a delay
		mHideHandler.removeCallbacks(mShowPart2Runnable)
		mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
	}*/
}
