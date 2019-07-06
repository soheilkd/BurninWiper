package soheilkd.BurninWiper

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setSupportActionBar(toolbar)
		loadTimerItems()
		loadPreferences()

		StartButton.setOnClickListener { run { start() } }
		CheckButton.setOnClickListener { run { check() } }
		DonateButton.setOnClickListener { run { openDonate() } }
	}
	private fun savePreferences() {
		val prefs = PreferenceManager.getDefaultSharedPreferences(this)

		val editor = prefs.edit()
		editor.putInt("ModeIndex", ModeRadioGroup.checkedRadioButtonId)
		editor.putInt("TimerIndex", TimerSpinner.selectedItemPosition)
		editor.apply()
	}

	private fun loadPreferences() {
		val prefs = PreferenceManager.getDefaultSharedPreferences(this)
		ModeRadioGroup.clearCheck()
		ModeRadioGroup.check(prefs.getInt("ModeIndex", 0))
		TimerSpinner.setSelection(prefs.getInt("TimerIndex", 0))
	}

	private fun loadTimerItems() {
		val timerItems = arrayOf("No Timer", "5m", "10m", "20m", "30m", "1h", "2h", "3h")
		val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, timerItems)
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
		TimerSpinner.adapter = adapter
	}

	private fun start() {
		savePreferences()
		val intent = Intent(this, ColorActivity::class.java)
		startActivity(intent)
	}

	private fun check() {
		val prefs = PreferenceManager.getDefaultSharedPreferences(this)
		val edit = prefs.edit()
		edit.putBoolean("IsChecking", true)
		edit.apply()
		start()
	}

	private fun openDonate() {
		savePreferences()
		val intent = Intent(this, DonationActivity::class.java)
		startActivity(intent)
	}

}
