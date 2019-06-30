using Plugin.InAppBilling;
using Plugin.InAppBilling.Abstractions;
using System;
using System.Threading.Tasks;

namespace BurninWiper
{
	public static class Donation
	{
		public delegate void ToastNeededDelegate(string message);
		public static event ToastNeededDelegate ToastNeeded;

		private static readonly string[] DonationProductIds = new string[]
		{
			"soheilkd.burninwiper.buymecoffee",
			"soheilkd.burninwiper.buymehotchocolate",
			"soheilkd.burninwiper.buymechickenwrap",
			"soheilkd.burninwiper.buymemovieticket",
			"soheilkd.burninwiper.buymeshirt"
		};

		private static void SendToast(string message)
		{
			ToastNeeded?.Invoke(message);
		}

		public static void AttempPurchase(int productIndex)
		{
			_ = AttempPurchase(DonationProductIds[productIndex]);
		}

		public static async Task AttempPurchase(string productId)
		{
			try
			{
				var connected = await CrossInAppBilling.Current.ConnectAsync();

				if (!connected)
				{
					//Couldn't connect to billing, could be offline, alert user
					SendToast("Couldn't connect to billing service. Check your connection.");
					return;
				}

				//try to purchase item
				InAppBillingPurchase purchase = await CrossInAppBilling.Current.PurchaseAsync(productId, ItemType.InAppPurchase, "apppayload");
				if (purchase == null)
				{
					//Not purchased, alert the user
					SendToast("Failed");
				}
				else
				{
					//Purchased, save this information
					var id = purchase.Id;
					var token = purchase.PurchaseToken;
					PurchaseState state = purchase.State;
					SendToast("Thanks!");
				}
			}
			catch (Exception ex)
			{
				//Something bad has occurred, alert user
				SendToast(ex.Message);
			}
			finally
			{
				//Disconnect, it is okay if we never connected
				await CrossInAppBilling.Current.DisconnectAsync();
			}
		}
	}
}