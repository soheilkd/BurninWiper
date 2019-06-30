using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using Android.App;
using Android.Content;
using Android.Graphics.Drawables;
using Android.OS;
using Android.Preferences;
using Android.Runtime;
using Android.Views;
using Android.Widget;

namespace BurninWiper
{
	[Activity(Label = "ColorActivity")]
	public class ColorActivity : Activity
	{
		private LinearLayout ColorLinearLayout;
		protected override void OnCreate(Bundle savedInstanceState)
		{
			base.OnCreate(savedInstanceState);
			SetContentView(Resource.Layout.color_main);

			shutdownTimer.Elapsed += (_, __) => Stop();
			ColorLinearLayout = FindViewById<LinearLayout>(Resource.Id.ColorLinearLayout);
			ColorLinearLayout.Click += (_, __) => Finish();
			Start();
		}

		//For turning off automatically
		private readonly System.Timers.Timer shutdownTimer = new System.Timers.Timer();
		private ColorDrawable[] CurrentColors;

		public int ShutdownTimerIndex
		{
			set
			{
				int MinutesToMs(int s) => s * 60 * 1000;
				if (value == 0) shutdownTimer.Interval = int.MaxValue;
				else if (value == 1) shutdownTimer.Interval = MinutesToMs(5);
				else if (value == 2) shutdownTimer.Interval = MinutesToMs(10);
				else if (value == 3) shutdownTimer.Interval = MinutesToMs(20);
				else if (value == 4) shutdownTimer.Interval = MinutesToMs(30);
				else if (value == 5) shutdownTimer.Interval = MinutesToMs(60);
				else if (value == 6) shutdownTimer.Interval = MinutesToMs(120);
				else if (value == 7) shutdownTimer.Interval = MinutesToMs(180);
				else shutdownTimer.Interval = int.MaxValue;
			}
		}
		public int ColorMode
		{
			set => CurrentColors = Colors.GetProperArray(value);
		}

		public Thread ColorChanger { get; private set; }

		private void ColorRotation()
		{
			while (true)
			{
				for (int i = 0; i < CurrentColors.Length; i++)
				{
					RunOnUiThread(() => ColorLinearLayout.Background = CurrentColors[i]);
					Thread.Sleep(1500);
				}
			}
		}

		private void MakeAppImmersive()
		{
			//Numbers are representing SystemUiFlags enumeration (Immersive | FullScreen | HideNavigation);
			Window.DecorView.SystemUiVisibility = (StatusBarVisibility)(2048 | 4 | 2);
		}
		public void Stop()
		{
			shutdownTimer.Stop();
			ColorChanger?.Abort();
			Finish();
		}


		public void Start()
		{
			Window.SetFlags(WindowManagerFlags.KeepScreenOn, WindowManagerFlags.KeepScreenOn);
			ApplyPreferences();
			MakeAppImmersive();
			StartThread();
			shutdownTimer.Start();
		}

		private void StartThread()
		{
			ColorChanger = new Thread(ColorRotation);
			ColorChanger.Start();
		}

		public void ShowGray()
		{
			MakeAppImmersive();
			ColorLinearLayout.Background = Colors.Gray;
		}
		private void ApplyPreferences()
		{
			var prefs = PreferenceManager.GetDefaultSharedPreferences(this);
			if (prefs.GetBoolean("IsChecking", false))
			{
				var edit = prefs.Edit();
				edit.PutBoolean("IsChecking", false);
				edit.Commit();
				ShutdownTimerIndex = 0;
				ColorMode = 4;
				return;
			}
			ColorMode = prefs.GetInt("ModeIndex", 0);
			ShutdownTimerIndex = prefs.GetInt("TimerIndex", 0);
		}
	}
}