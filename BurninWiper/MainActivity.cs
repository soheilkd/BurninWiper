using System;
using System.Threading;
using System.Threading.Tasks;
using Android.App;
using Android.Content;
using Android.Graphics;
using Android.Graphics.Drawables;
using Android.OS;
using Android.Runtime;
using Android.Support.Design.Widget;
using Android.Support.V7.App;
using Android.Support.V7.Widget;
using Android.Views;
using Android.Widget;
using Plugin.InAppBilling;
using Plugin.InAppBilling.Abstractions;

namespace BurninWiper
{
    [Activity(Label = "@string/app_name", Theme = "@style/AppTheme.NoActionBar", MainLauncher = true)]
    public class MainActivity : AppCompatActivity
    {
		static MainActivity()
		{
			LinkerPleaseInclude();
		}
		#region UI Controls
		private Spinner TimerSpinner;
		private RelativeLayout ColorRelativeLayout;
		private RadioButton RadioButton1;
		private RadioButton RadioButton2;
		private RadioButton RadioButton3;
		#endregion

		private readonly string[] DonationProductIds = new string[]
		{
			"soheilkd.burninwiper.buymecoffee",
			"soheilkd.burninwiper.buymehotchocolate",
			"soheilkd.burninwiper.buymechickenwrap",
			"soheilkd.burninwiper.buymemovieticket",
			"soheilkd.burninwiper.buymeshirt"
		};

		//This fix crashes caused by linker
		static bool falseflag = false;
		static void LinkerPleaseInclude()
		{
			if (falseflag)
			{
				var ignore = new FitWindowsLinearLayout(Application.Context);
			}
		}

		private Thread CurrentColorThread = new Thread(() => Console.WriteLine("CurrentColorThread Started"));
		
		#region Colors
		private readonly ColorDrawable WhiteColor = new ColorDrawable(Color.White);
		private readonly ColorDrawable RedColor = new ColorDrawable(Color.Red);
		private readonly ColorDrawable GreenColor = new ColorDrawable(Color.Green);
		private readonly ColorDrawable BlueColor = new ColorDrawable(Color.Blue);
		private readonly ColorDrawable BlackColor = new ColorDrawable(Color.Black);
		private readonly ColorDrawable GrayColor = new ColorDrawable(Color.DarkGray);
		private ColorDrawable[] Colors = new ColorDrawable[0];
		#endregion

		private void InitializeControls()
		{
			TimerSpinner = FindViewById<Spinner>(Resource.Id.TimerSpinner);
			ColorRelativeLayout = FindViewById<RelativeLayout>(Resource.Id.RelativeLayout1);
			RadioButton1 = FindViewById<RadioButton>(Resource.Id.RadioButton1);
			RadioButton2 = FindViewById<RadioButton>(Resource.Id.RadioButton2);
			RadioButton3 = FindViewById<RadioButton>(Resource.Id.RadioButton3);
			var startButton = FindViewById<Button>(Resource.Id.StartButton);
			var checkButton = FindViewById<Button>(Resource.Id.CheckButton);
			var contentMain = FindViewById<RelativeLayout>(Resource.Id.content_main);
			var donateButton = FindViewById<Button>(Resource.Id.DonateButton);
			LoadTimerItems();
			startButton.Click += StartButton_Click;
			checkButton.Click += CheckButton_Click;
			donateButton.Click += DonateButton_Click;

			ColorRelativeLayout.Click += MainGridView_Click;
		}
		
		private void DonateButton_Click(object sender, EventArgs e)
		{
			SetContentView(Resource.Layout.donate_main);
				InitializeDonationControls();
		}

		private void MakeAppImmersive()
		{
			//Numbers are representing SystemUiFlags enumeration (Immersive | FullScreen | HideNavigation);
			Window.DecorView.SystemUiVisibility = (StatusBarVisibility)(2048 | 4 | 2);
		}

		private async Task AttempPurchase(string productId)
		{
			try
			{
				var connected = await CrossInAppBilling.Current.ConnectAsync();

				if (!connected)
				{
					//Couldn't connect to billing, could be offline, alert user
					Toast.MakeText(this, "Check your connection, and try again.", ToastLength.Long).Show();
					return;
				}

				//try to purchase item
				var purchase = await CrossInAppBilling.Current.PurchaseAsync(productId, ItemType.InAppPurchase, "apppayload");
				if (purchase == null)
				{
					//Not purchased, alert the user
					Toast.MakeText(this, "Failed", ToastLength.Long).Show();
				}
				else
				{
					//Purchased, save this information
					var id = purchase.Id;
					var token = purchase.PurchaseToken;
					var state = purchase.State;
					Toast.MakeText(this, "Thanks!", ToastLength.Long).Show();
				}
			}
			catch (Exception ex)
			{
				//Something bad has occurred, alert user
				Toast.MakeText(this, ex.Message, ToastLength.Long).Show();
			}
			finally
			{
				//Disconnect, it is okay if we never connected
				await CrossInAppBilling.Current.DisconnectAsync();
			}
		}

		protected override void OnCreate(Bundle savedInstanceState)
		{
			base.OnCreate(savedInstanceState);
			SetContentView(Resource.Layout.activity_main);
			Plugin.CurrentActivity.CrossCurrentActivity.Current.Activity = this;
			Window.SetFlags(WindowManagerFlags.KeepScreenOn, WindowManagerFlags.KeepScreenOn);
			InitializeControls();
		}

		private void InitializeDonationControls()
		{
			var button1 = FindViewById<Button>(Resource.Id.DonateButton1);
			var button2 = FindViewById<Button>(Resource.Id.DonateButton2);
			var button3 = FindViewById<Button>(Resource.Id.DonateButton3);
			var button4 = FindViewById<Button>(Resource.Id.DonateButton4);
			var button5 = FindViewById<Button>(Resource.Id.DonateButton5);
			var backButton = FindViewById<Button>(Resource.Id.BackButton);

			button1.Click += async (_, __) => await AttempPurchase(DonationProductIds[0]);
			button2.Click += async (_, __) => await AttempPurchase(DonationProductIds[1]);
			button3.Click += async (_, __) => await AttempPurchase(DonationProductIds[2]);
			button4.Click += async (_, __) => await AttempPurchase(DonationProductIds[3]);
			button5.Click += async (_, __) => await AttempPurchase(DonationProductIds[4]);
			backButton.Click += (_, __) =>
			{
				SetContentView(Resource.Layout.content_main);
				InitializeControls();
			};
		}

		private void MainGridView_Click(object sender, EventArgs e)
		{
			ColorRelativeLayout.Visibility = ViewStates.Invisible;
			if (CurrentColorThread.IsAlive)
				CurrentColorThread.Abort();
		}

		private void CheckButton_Click(object sender, EventArgs e)
		{
			ColorRelativeLayout.Visibility = ViewStates.Visible;
			ColorRelativeLayout.Background = GrayColor;
		}

		private void ApplyProperColors()
		{
			if (RadioButton1.Checked) Colors = new ColorDrawable[] { WhiteColor, RedColor, GreenColor, BlueColor, BlackColor }; //Color Rotation
			else if (RadioButton2.Checked) Colors = new ColorDrawable[] { WhiteColor, BlackColor }; //Black and White
			else if (RadioButton3.Checked) Colors = new ColorDrawable[] { WhiteColor }; //White Only
			else throw new InvalidOperationException(); //Means sth is gone wrong
		}

		private void ApplyProperTimer()
		{
			long timerItem = TimerSpinner.SelectedItemId;
			if (timerItem != 0)
			{
				var timer = new System.Timers.Timer();
				timer.Elapsed += (_, __) =>
				{
					MainGridView_Click(this, null);
					timer.Stop();
				};
				int SecondsToMs(int s) => s * 1000;
				if (timerItem == 1) timer.Interval = SecondsToMs(30);
				else if (timerItem == 2) timer.Interval = SecondsToMs(60);
				else if (timerItem == 3) timer.Interval = SecondsToMs(120);
				else if (timerItem == 4) timer.Interval = SecondsToMs(300);
				else if (timerItem == 5) timer.Interval = SecondsToMs(900);
				else if (timerItem == 6) timer.Interval = SecondsToMs(1800);
				else if (timerItem == 7) timer.Interval = SecondsToMs(3600);
				else timer.Interval = int.MaxValue;
				timer.Start();
			}
		}

		private void StartColorRotation()
		{
			CurrentColorThread = new Thread(ColorRotation);
			CurrentColorThread.Start();
		}

		private void StartButton_Click(object sender, EventArgs e)
		{
			ColorRelativeLayout.Visibility = ViewStates.Visible;
			MakeAppImmersive();
			ApplyProperColors();
			ApplyProperTimer();
			StartColorRotation();
		}

		private void LoadTimerItems()
		{
			var adapter = ArrayAdapter.CreateFromResource(
					this, Resource.Array.timer_array, Android.Resource.Layout.SimpleSpinnerItem);

			adapter.SetDropDownViewResource(Android.Resource.Layout.SimpleSpinnerDropDownItem);
			TimerSpinner.Adapter = adapter;
		}

		protected override void OnActivityResult(int requestCode, Result resultCode, Intent data)
		{
			base.OnActivityResult(requestCode, resultCode, data);
			InAppBillingImplementation.HandleActivityResult(requestCode, resultCode, data);
		}

		private void ColorRotation()
		{
			while (true)
			{
				for (int i = 0; i < Colors.Length; i++)
				{
					RunOnUiThread(() => ColorRelativeLayout.Background = Colors[i]);
					Thread.Sleep(2000);
				}
			}
		}
	}
}