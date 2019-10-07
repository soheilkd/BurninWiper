package soheilkd.BurninWiper

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setSupportActionBar(toolbar)
		loadTimerItems()
		loadPreferences()

		main_startButton.setOnClickListener { run { start() } }
		main_checkButton.setOnClickListener { run { start(true) } }
		main_donateButton.setOnClickListener { run { openDonate() } }
	}

	override fun onDestroy(){
		savePreferences()
		super.onDestroy()
	}

	private fun savePreferences() {
		val prefs = PreferenceManager.getDefaultSharedPreferences(this)

		val editor = prefs.edit()
		editor.putInt("ModeId", main_radioGroup.checkedRadioButtonId)
		editor.putInt("TimerIndex", main_timeSpinner.selectedItemPosition)
		editor.apply()
	}

	private fun loadPreferences() {
		val prefs = PreferenceManager.getDefaultSharedPreferences(this)
		main_radioGroup.clearCheck()
		main_radioGroup.check(prefs.getInt("ModeId", R.id.main_radio1))
		main_timeSpinner.setSelection(prefs.getInt("TimerIndex", 0))
	}

	private fun loadTimerItems() {
		val timerItems =
			arrayOf(getString(R.string.ui_timerNone), "5m", "10m", "20m", "30m", "1h", "2h", "3h")
		val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, timerItems)
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
		main_timeSpinner.adapter = adapter
	}

	private fun start(isChecking: Boolean = false) {
		savePreferences()
		val intent =
			when (main_radioGroup.checkedRadioButtonId) {
				R.id.main_radio4 -> Intent(this, AnimActivity::class.java) //aka animation mode
				else -> Intent(this, ColorActivity::class.java) //aka other modes
			}
		intent.putExtra("TimerIndex", main_timeSpinner.selectedItemPosition)
		intent.putExtra("ColorMode", main_radioGroup.checkedRadioButtonId)
		intent.putExtra("IsChecking", isChecking)
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
		startActivity(intent)
	}

	private fun openDonate() {
		savePreferences()
		val intent = Intent(this, DonationActivity::class.java)
		startActivity(intent)
	}
}
