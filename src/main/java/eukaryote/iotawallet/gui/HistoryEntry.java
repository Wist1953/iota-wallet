package eukaryote.iotawallet.gui;

import java.text.NumberFormat;

import javax.swing.ImageIcon;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class HistoryEntry {
	public String timestamp;

	public String tag;

	public long amount;

	public long balance;

	public boolean confirmed;

	private static final ImageIcon confirmedicon = new ImageIcon(ClassLoader.getSystemResource("checkmarkgreen.png"));

	public long epoch;

	public HistoryEntry(long epoch, String timestamp, String tag, long amount, long balance, boolean confirmed) {
		this.epoch = epoch;
		this.timestamp = timestamp;
		this.tag = tag;
		this.amount = amount;
		this.balance = balance;
		this.confirmed = confirmed;
	}

	public Object getObjectAt(int index) {
		switch (index) {
		case 0:
			if (confirmed)
				return confirmedicon;
			else
				return "";
		case 1:
			return timestamp;
		case 2:
			return tag;
		case 3:
			return NumberFormat.getInstance().format(amount);
		case 4:
			return NumberFormat.getInstance().format(balance);
		default:
			return null;
		}
	}
}
