package soheilkd.BurninWiper

import android.os.Bundle
import android.os.SystemClock
import android.util.DisplayMetrics
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_anim.*
import pl.droidsonroids.gif.GifImageView
import kotlin.math.roundToInt

class AnimActivity : AppCompatActivity() {
	private val gifViews = arrayListOf<GifImageView>()
	private val gifs = arrayOf(
		R.drawable.gif_0,
		R.drawable.gif_1,
		R.drawable.gif_2,
		R.drawable.gif_3,
		R.drawable.gif_4,
		R.drawable.gif_5,
		R.drawable.gif_6,
		R.drawable.gif_7
	)
	private var animThread = Thread(Runnable {
		while (true) {
			for (gif in gifs) {
				for (view in gifViews) {
					runOnUiThread { view.setImageResource(gif) }
				}
				SystemClock.sleep(20000)
			}
		}
	})

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_anim)
		anim_grid.setOnClickListener { finish() }

		Helper.makeImmersive(window, anim_grid)
		if (intent.getBooleanExtra("IsChecking", false))
			return
		loadViews()
		animThread.start()
		Helper.setTimer(intent.getIntExtra("TimerIndex", 0)) { finish() }
		Toast.makeText(this, "Cautious: Device may heat up after a while", Toast.LENGTH_LONG).show()
	}

	override fun onPause() {
		finish()
		super.onPause()
	}

	private fun loadViews() {
		val metrics = DisplayMetrics()
		windowManager.defaultDisplay.getMetrics(metrics)

		anim_grid.columnCount = (metrics.widthPixels / metrics.density / 75).roundToInt() + 1
		anim_grid.rowCount = (metrics.heightPixels / metrics.density / 75).roundToInt() + 1
		val dps = (75 * metrics.scaledDensity + 0.5f).toInt()
		for (i in 0..anim_grid.columnCount * anim_grid.rowCount) {
			val view = GifImageView(this)
			view.layoutParams = ViewGroup.LayoutParams(dps, dps)
			view.setOnClickListener { finish() }
			gifViews.add(view)
		}
		for (view in gifViews) {
			anim_grid.addView(view)
		}
	}
}
