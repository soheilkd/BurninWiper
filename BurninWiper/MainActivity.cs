using Android.App;
using Android.Content;
using Android.OS;
using Android.Preferences;
using Android.Support.Design.Widget;
using Android.Support.V7.App;
using Android.Support.V7.Widget;
using Android.Views;
using Android.Widget;
using Plugin.InAppBilling;
using System;
using static Android.Widget.AdapterView;

namespace BurninWiper
{
	[Activity(Label = "@string/app_name", Theme = "@style/AppTheme.NoActionBar", MainLauncher = true)]
	public class MainActivity : AppCompatActivity
	{
		public MainActivity()
		{
			if (falseflag)
			{
				var ignore = new FitWindowsLinearLayout(this);
				var ignore2 = new CoordinatorLayout(this);
			}
		}
		#region UI INITIALIZATIONS
		private Spinner TimerSpinner;
		private RadioGroup ModeGroup;
		private void InitializeControls()
		{
			TimerSpinner = FindViewById<Spinner>(Resource.Id.TimerSpinner);
			ModeGroup = FindViewById<RadioGroup>(Resource.Id.ModeRadioGroup);
			var startButton = FindViewById<Button>(Resource.Id.StartButton);
			var checkButton = FindViewById<Button>(Resource.Id.CheckButton);
			var contentMain = FindViewById<RelativeLayout>(Resource.Id.content_main);
			var donateButton = FindViewById<Button>(Resource.Id.DonateButton);
			LoadTimerItems();
			startButton.Click += StartButton_Click;
			checkButton.Click += CheckButton_Click;
			donateButton.Click += DonateButton_Click;

			LoadPreferences();

			TimerSpinner.ItemSelected += (_, __) =>
			 {
				 var spinnerTextView = TimerSpinner.GetChildAt(0) as TextView;
				 spinnerTextView?.SetTextSize(Android.Util.ComplexUnitType.Sp, 20);
				 spinnerTextView?.SetTextColor(Android.Content.Res.ColorStateList.ValueOf(Android.Graphics.Color.White));
			 };

		}

		static bool falseflag = false;
		protected override void OnCreate(Bundle savedInstanceState)
		{
			base.OnCreate(savedInstanceState);
			SetContentView(Resource.Layout.activity_main);
			InitializeControls();
		}
		private void LoadTimerItems()
		{
			var adapter = ArrayAdapter.CreateFromResource(
					this, Resource.Array.timer_array, Android.Resource.Layout.SimpleSpinnerItem);

			adapter.SetDropDownViewResource(Android.Resource.Layout.SimpleSpinnerDropDownItem);
			TimerSpinner.Adapter = adapter;
		}

		#endregion

		private void SavePreferences()
		{
			var prefs = PreferenceManager.GetDefaultSharedPreferences(this);
			
			var editor = prefs.Edit();
			editor.PutInt("ModeIndex", ModeGroup.CheckedRadioButtonId);
			editor.PutInt("TimerIndex", TimerSpinner.SelectedItemPosition);
			editor.Commit();
		}
		private void LoadPreferences()
		{
			var prefs = PreferenceManager.GetDefaultSharedPreferences(this);
			ModeGroup.ClearCheck();
			ModeGroup.Check(prefs.GetInt("ModeIndex", 0));
			TimerSpinner.SetSelection(prefs.GetInt("TimerIndex", 0));
		}

		private void DonateButton_Click(object sender, EventArgs e)
		{
			SavePreferences();
			var intent = new Intent(this, typeof(DonationActivity));
			StartActivity(intent);
		}


		private void CheckButton_Click(object sender, EventArgs e)
		{
			var prefs = PreferenceManager.GetDefaultSharedPreferences(this);
			var edit = prefs.Edit();
			edit.PutBoolean("IsChecking", true);
			edit.Commit();
			StartButton_Click(this, null);
		}


		private void StartButton_Click(object sender, EventArgs e)
		{
			SavePreferences();
			var intent = new Intent(this, typeof(ColorActivity));
			StartActivity(intent);
		}
	}
}