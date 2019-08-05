package soheilkd.burninwiper

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity;
import com.android.billingclient.api.*

import kotlinx.android.synthetic.main.activity_donation.*
import kotlin.collections.ArrayList
import android.util.TypedValue
import soheilkd.BurninWiper.R


class DonationActivity : AppCompatActivity(), PurchasesUpdatedListener {
	override fun onPurchasesUpdated(billingResult: BillingResult?, purchases: MutableList<Purchase>?) {
		if (billingResult?.responseCode == BillingClient.BillingResponseCode.OK)
			Toast.makeText(this, "Thanks!", Toast.LENGTH_LONG).show()
		else
			println("DEBUG:${billingResult?.debugMessage}")
	}

	private lateinit var billingClient: BillingClient

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_donation)
		setupBillingClient()

		ReturnButton.setOnTouchListener { _, _ ->
			finish()
			true
		}

	}

	private var listOfSkuDetails: List<SkuDetails>? = null
	override fun onDestroy() {
		super.onDestroy()
		billingClient.endConnection()
	}

	private fun purchase(id: Int): Boolean {
		val billingFlow =
			BillingFlowParams.newBuilder()
				.setSkuDetails(listOfSkuDetails?.get(id))
				.build()
		return billingClient.launchBillingFlow(this, billingFlow).responseCode == BillingClient.BillingResponseCode.OK
	}

	private fun setupBillingClient() {
		billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build()

		billingClient.startConnection(object : BillingClientStateListener {
			@SuppressLint("SetTextI18n")
			override fun onBillingSetupFinished(billingResult: BillingResult) {
				if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
					val skuList = ArrayList<String>()
					skuList.add("soheilkd.burninwiper.donation1")
					skuList.add("soheilkd.burninwiper.donation2")
					skuList.add("soheilkd.burninwiper.donation3")
					skuList.add("soheilkd.burninwiper.donation4")
					skuList.add("soheilkd.burninwiper.donation5")
					skuList.add("soheilkd.burninwiper.donation6")
					val params = SkuDetailsParams.newBuilder()
					params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
					var i = 0
					billingClient.querySkuDetailsAsync(params.build()) { inBillingResult, skuDetailsList ->
						run {
							if (inBillingResult.responseCode != BillingClient.BillingResponseCode.OK){
								Toast.makeText(this@DonationActivity, "Connection To Billing Failed", Toast.LENGTH_LONG).show()
								finish()
							}
							listOfSkuDetails = skuDetailsList
							for (sku in skuDetailsList) {
								val index = i++
								MainLinearLayout.addView(
									Button(this@DonationActivity).also { button ->
										run {
											val r = resources
											var px = TypedValue.applyDimension(
												TypedValue.COMPLEX_UNIT_SP,
												64f,
												r.displayMetrics
											)

											button.layoutParams =
												LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, px.toInt())
											button.text = "${sku.priceCurrencyCode}${sku.price}"
											px = TypedValue.applyDimension(
												TypedValue.COMPLEX_UNIT_SP,
												8f,
												r.displayMetrics
											)
											button.textSize = px
											button.requestLayout()
											button.setOnTouchListener { _, _ ->
												purchase(index)
											}
										}
									}
								)
							}
						}
					}
				}
			}

			override fun onBillingServiceDisconnected() {
				// Try to restart the connection on the next request to
				// Google Play by calling the startConnection() method.
			}
		})
	}
}
