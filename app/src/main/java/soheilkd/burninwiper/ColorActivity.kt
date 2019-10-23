package soheilkd.BurninWiper

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_color.*

class ColorActivity : AppCompatActivity() {
	private var currentColors: Array<ColorDrawable> = arrayOf()
	private var colorThread = Thread(Runnable {
		while(true){
			for(color in currentColors){
				runOnUiThread { color_layout.background = color }
				SystemClock.sleep(1250)
			}
		}
	})

	private fun start(){
		Helper.makeImmersive(window, color_layout)
		applyPreferences()
		 colorThread.start()
	}

	//Check if application is launched by app shortcuts
	private fun checkFromShortcut(): Boolean {
		if (intent.action?.startsWith("soheilkd.BurninWiper.") == true) {
			currentColors = Helper.getColorArray(
				when (intent.action?.takeLast(4)) {
					"run1" -> 0
					"run2" -> 1
					"run3" -> 2
					else -> -1
				}
			)
			return true
		}
		return false
	}

	private fun applyPreferences(){
		if (intent.getBooleanExtra("IsChecking", false)) {
			currentColors = Helper.getColorArray(-1)
			return
		}
		if (!checkFromShortcut())
			currentColors = Helper.getColorArrayById(intent.getIntExtra("ColorMode", 0))

		Helper.setTimer(intent.getIntExtra("TimerIndex", 0)) { finish() }
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setContentView(R.layout.activity_color)

		color_layout.setOnTouchListener { _, _ ->
			finish()
			true
		}

		start()
	}

	override fun onPause() {
		finish()
		super.onPause()
	}
}
