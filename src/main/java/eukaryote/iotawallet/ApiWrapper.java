package eukaryote.iotawallet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.ArrayUtils;

import eukaryote.iotawallet.gui.HistoryEntry;
import jota.IotaAPI;
import jota.dto.response.FindTransactionResponse;
import jota.dto.response.GetNewAddressResponse;
import jota.error.InvalidAddressException;
import jota.error.InvalidSecurityLevelException;
import jota.error.NoNodeInfoException;
import jota.model.Transaction;
import lombok.Getter;

public class ApiWrapper {
	@Getter
	IotaAPI api;
	private String seed;
	public final int security = 2;
	private AppContext ctx;
	private SimpleDateFormat datefmt;

	public ApiWrapper(AppContext ctx, IotaAPI api, String seed) {
		this.ctx = ctx;
		this.api = api;
		this.seed = seed;

		datefmt = new SimpleDateFormat(ctx.getConfig().getString("dateformat"));
	}

	public List<String> getAddresses() {
		GetNewAddressResponse addrs;
		try {
			// TODO: when api gets updated, re-enable checksums
			addrs = api.getNewAddress(seed, security, 0, false, ctx.getConfig().getInt("numaddrs"), true);
		} catch (InvalidSecurityLevelException e) {
			JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		} catch (InvalidAddressException e) {
			JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		return addrs.getAddresses();
	}

	public List<HistoryEntry> getHistory() {
		List<HistoryEntry> ret = new ArrayList<>();

		List<String> addresses = getAddresses();
		FindTransactionResponse ftba = api
				.findTransactionsByAddresses(addresses.toArray(ArrayUtils.EMPTY_STRING_ARRAY));

		List<Transaction> txobjs = api.getTransactionsObjects(ftba.getHashes());

		Collections.sort(txobjs, new Comparator<Transaction>() {

			@Override
			public int compare(Transaction arg0, Transaction arg1) {
				return Long.compare(arg0.getTimestamp(), arg1.getTimestamp());
			}

		});

		long balance = 0;
		String prevbundle = null;

		// convert tx array to hash array to get inclusion states
		String[] txs = new String[txobjs.size()];
		for (int i = 0; i < txobjs.size(); i++) {
			Transaction t = txobjs.get(i);
			txs[i] = t.getHash();
		}
		boolean[] states;
		try {
			states = api.getLatestInclusion(txs).getStates();
		} catch (NoNodeInfoException e) {
			JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		
		for (int i = 0; i < txobjs.size(); i++) {
			Transaction t = txobjs.get(i);
			
			if (states[i])
				balance += t.getValue();

			String datetime = datefmt.format(new Date(t.getTimestamp() * 1000));
			String tag = t.getTag().replaceFirst("9+$", "");

			if (!t.getBundle().equals(prevbundle)) {
				ret.add(new HistoryEntry(datetime, tag, t.getValue(), balance, states[i]));
			} else {
				HistoryEntry prevhist = ret.get(ret.size() - 1);
				prevhist.balance = balance;
				if (states[i])
					prevhist.amount += t.getValue();
			}

			prevbundle = t.getBundle();
		}

		return ret;
	}
}
