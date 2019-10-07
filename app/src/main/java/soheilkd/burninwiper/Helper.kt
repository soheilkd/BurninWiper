package soheilkd.BurninWiper

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import java.util.*
import kotlin.concurrent.timerTask

class Helper {
	companion object {
		fun setTimer(index: Int, onFinish: () -> Unit): Timer {
			val timer = Timer()
			val timerTask = timerTask {
				onFinish()
			}

			fun minToMs(m: Int): Long {
				return m * 60L * 1000
			}
			when (index) {
				1 -> timer.schedule(timerTask, minToMs(5))
				2 -> timer.schedule(timerTask, minToMs(10))
				3 -> timer.schedule(timerTask, minToMs(20))
				4 -> timer.schedule(timerTask, minToMs(30))
				5 -> timer.schedule(timerTask, minToMs(60))
				6 -> timer.schedule(timerTask, minToMs(120))
				7 -> timer.schedule(timerTask, minToMs(180))
			}
			return timer
		}

		fun makeImmersive(window: Window, view: View) {
			window.setFlags(128, 128)
			view.systemUiVisibility =
				View.SYSTEM_UI_FLAG_LOW_PROFILE or
						View.SYSTEM_UI_FLAG_FULLSCREEN or
						View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
						View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
						View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
						View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
		}

		fun getColorArrayById(id: Int): Array<ColorDrawable> {
			return when (id) {
				R.id.main_radio1 -> getColorArray(0)
				R.id.main_radio2 -> getColorArray(1)
				R.id.main_radio3 -> getColorArray(2)
				else -> getColorArray(-1)
			}
		}

		fun getColorArray(index: Int): Array<ColorDrawable> {
			val white by lazy { ColorDrawable(Color.WHITE) }
			val red by lazy { ColorDrawable(Color.RED) }
			val green by lazy { ColorDrawable(Color.GREEN) }
			val blue by lazy { ColorDrawable(Color.BLUE) }
			val black by lazy { ColorDrawable(Color.BLACK) }
			val gray by lazy { ColorDrawable(Color.DKGRAY) }

			return when (index) {
				0 -> arrayOf(white, red, green, blue, black)
				1 -> arrayOf(white, black)
				2 -> arrayOf(white)
				else -> arrayOf(gray)
			}
		}
	}
}