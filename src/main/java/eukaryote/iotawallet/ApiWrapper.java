package eukaryote.iotawallet;

import java.util.ArrayList;
import java.util.List;

import eukaryote.iotawallet.gui.HistoryEntry;
import jota.IotaAPI;
import lombok.Getter;

public class ApiWrapper {
	@Getter
	IotaAPI api;
	private String seed;

	public ApiWrapper(IotaAPI api, String seed) {
		this.api = api;
		this.seed = seed;
	}
	
	public List<HistoryEntry> getHistory() {
		List<HistoryEntry> ret = new ArrayList<>();
		
		return ret;
	}
}
