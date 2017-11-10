package eukaryote.iotawallet.gui;

public class HistoryEntry {
	public long timestamp;

	public String tag;

	public long amount;

	public long balance;

	public HistoryEntry(long timestamp, String tag, long amount, long balance) {
		this.timestamp = timestamp;
		this.tag = tag;
		this.amount = amount;
		this.balance = balance;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HistoryEntry other = (HistoryEntry) obj;
		if (amount != other.amount)
			return false;
		if (balance != other.balance)
			return false;
		if (tag == null) {
			if (other.tag != null)
				return false;
		} else if (!tag.equals(other.tag))
			return false;
		if (timestamp != other.timestamp)
			return false;
		return true;
	}
	
	public Object getObjectAt(int index) {
		switch (index) {
		case 0:
			return timestamp;
		case 1:
			return tag;
		case 2:
			return amount;
		case 3:
			return balance;
		default:
			return null;
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (amount ^ (amount >>> 32));
		result = prime * result + (int) (balance ^ (balance >>> 32));
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return "HistoryEntry [timestamp=" + timestamp + ", tag=" + tag + ", amount=" + amount + ", balance=" + balance
				+ "]";
	}
}
