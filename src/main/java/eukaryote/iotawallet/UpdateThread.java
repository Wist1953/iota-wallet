package eukaryote.iotawallet;

import java.util.TimerTask;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UpdateThread extends TimerTask {
	private AppContext ctx;

	public UpdateThread(AppContext ctx) {
		this.ctx = ctx;
	}
	
	@Override
	public void run() {
		ctx.getApplicationWindow().refreshHistory();
	}

}
