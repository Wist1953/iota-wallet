package eukaryote.iotawallet;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.util.text.BasicTextEncryptor;

import jota.IotaAPI;
import jota.dto.response.GetNodeInfoResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppContext {

	private PropertiesConfiguration config;
	private String[] hosts;
	private ApiWrapper api = null;
	private String seed;

	public AppContext() {
		Configurations configs = new Configurations();
		File configfile = new File("wallet.cfg");

		if (!configfile.exists())
			try {
				FileUtils.copyURLToFile(ClassLoader.getSystemResource("default.cfg"), configfile);
			} catch (IOException e) {
				log.error("Something went wrong whilst trying to make default configuration file!", e);

				throw new Error();
			}

		try {
			config = configs.properties(configfile);
		} catch (ConfigurationException e) {
			log.error("Something went wrong loading the configuration file!", e);

			throw new Error();
		}
		
		log.info("Loaded config");

		File seedfile = new File(config.getString("seedfile"));

		SeedStore ss = new SeedStore(seedfile);

		this.seed = ss.getSeed();
		
		connectToHost();
	}

	public void connectToHost() {

		hosts = StringUtils.split(config.getString("lightnodes"), ';');
		String hoststr = null;
		while (api == null) {
			hoststr = hosts[RandomUtils.nextInt(0, hosts.length)];

			String host = StringUtils.split(hoststr, ":")[0];
			String port = StringUtils.split(hoststr, ":")[1];

			try {
				api = new ApiWrapper(new IotaAPI.Builder().protocol("http").host(host).port(port).build(), seed);
				GetNodeInfoResponse nodeInfo = api.getApi().getNodeInfo();

				// node not synced
				if (nodeInfo.getLatestMilestoneIndex() != nodeInfo.getLatestSolidSubtangleMilestoneIndex())
					continue;
			} catch (Exception e) {
				api = null;
			}
		}

		log.info("Connected to remote node ({})", hoststr);
	}

}
