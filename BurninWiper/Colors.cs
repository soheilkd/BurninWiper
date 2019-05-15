using Android.Graphics;
using Android.Graphics.Drawables;

namespace BurninWiper
{
	public static class Colors
	{
		public static ColorDrawable[] ActiveArray { get; private set; }

		public static readonly ColorDrawable White = new ColorDrawable(Color.White);
		public static readonly ColorDrawable Red = new ColorDrawable(Color.Red);
		public static readonly ColorDrawable Green = new ColorDrawable(Color.Green);
		public static readonly ColorDrawable Blue = new ColorDrawable(Color.Blue);
		public static readonly ColorDrawable Black = new ColorDrawable(Color.Black);
		public static readonly ColorDrawable Gray = new ColorDrawable(Color.DarkGray);

		public static ColorDrawable[] GetProperArray(int radioButtonId)
		{
			switch (radioButtonId)
			{
				case 1:
					return new ColorDrawable[] { White, Red, Green, Blue, Black };
				case 2:
					return new ColorDrawable[] { White, Black };
				case 3:
					return new ColorDrawable[] { White };
				case 4:
					return new ColorDrawable[] { Gray };
				default:
					return new ColorDrawable[] { White, Red, Green, Blue, Black };
			}
		}
		public static void SetProperArray(int radioButtonId)
		{
			ActiveArray = GetProperArray(radioButtonId);
		}
	}
}