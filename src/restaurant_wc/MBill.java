package restaurant_wc;

public class MBill {
		double Payment;
		String Choice;
		int Amt;
		double Cost;
		public MBill(String choice, int amt, double cost){ 
			Choice = choice;
			Amt = amt;
			Cost = cost;
			Payment = amt * cost;
			
		}
	}
