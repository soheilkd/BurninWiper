using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Plugin.InAppBilling;

namespace BurninWiper
{
	[Activity(Label = "DonationActivity")]
	public class DonationActivity : Activity
	{
		protected override void OnCreate(Bundle savedInstanceState)
		{
			base.OnCreate(savedInstanceState);
			SetContentView(Resource.Layout.donate_main);

			InitializeControls();
			Plugin.CurrentActivity.CrossCurrentActivity.Current.Activity = this;
		}
		protected override void OnActivityResult(int requestCode, Result resultCode, Intent data)
		{
			base.OnActivityResult(requestCode, resultCode, data);
			InAppBillingImplementation.HandleActivityResult(requestCode, resultCode, data);
		}
		private void InitializeControls()
		{
			var button1 = FindViewById<Button>(Resource.Id.DonateButton1);
			var button2 = FindViewById<Button>(Resource.Id.DonateButton2);
			var button3 = FindViewById<Button>(Resource.Id.DonateButton3);
			var button4 = FindViewById<Button>(Resource.Id.DonateButton4);
			var button5 = FindViewById<Button>(Resource.Id.DonateButton5);
			var button6 = FindViewById<Button>(Resource.Id.ReturnButton);

			button1.Click += (_, __) => Donation.AttempPurchase(0);
			button2.Click += (_, __) => Donation.AttempPurchase(1);
			button3.Click += (_, __) => Donation.AttempPurchase(2);
			button4.Click += (_, __) => Donation.AttempPurchase(3);
			button5.Click += (_, __) => Donation.AttempPurchase(4);
			button6.Click += (_, __) => Finish();
		}
	}
}