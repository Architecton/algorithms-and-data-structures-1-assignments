class Balance {
		private int[] owes;
		private int[] owed;
		private int transactionCounter;
		
		public Balance(int[] owes, int[] owed, int transactionCounter) {
			this.owes = owes;
			this.owed = owed;
			this.transactionCounter = transactionCounter;
		}
		
		public int[] getOwes() {
			return this.owes;
		}
		
		public int[] getOwed() {
			return this.owed;
		}
		
		public int getTransactionCounter() {
			return this.transactionCounter;
		}

		public void incrementTransactionCounter() {
			this.transactionCounter++;
		}
		
		@Override
		public String toString() {
			String arr1 = "";
			String arr2 = "";
			for(int i = 0; i < this.owes.length; i++) {
				arr1 += this.owes[i] + " ";
			}
			for(int i = 0; i < this.owed.length; i++) {
				arr2 += this.owed[i] + " ";
			}
			return "([" + arr1 + "], [" + arr2 + "])";
		}
}