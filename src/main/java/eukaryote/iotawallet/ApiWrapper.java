package eukaryote.iotawallet;

import java.util.ArrayList;
import java.util.List;

import eukaryote.iotawallet.gui.HistoryEntry;
import jota.IotaAPI;

public class ApiWrapper {
	IotaAPI api;

	public ApiWrapper(IotaAPI api) {
		this.api = api;
	}
	
	public List<HistoryEntry> getHistory() {
		List<HistoryEntry> ret = new ArrayList<>();
		
		return ret;
	}
}
