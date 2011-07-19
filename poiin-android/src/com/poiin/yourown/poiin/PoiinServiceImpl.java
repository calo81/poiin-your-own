package com.poiin.yourown.poiin;

import com.poiin.yourown.network.RestClientService;
import com.poiin.yourown.network.RestClientServiceImpl;

public class PoiinServiceImpl implements PoiinService {
	private RestClientService restClient = new RestClientServiceImpl();

	public void sendPoiin(final Poiin poiin) {
		new Thread(new Runnable() {
			public void run() {
				restClient.sendPoiin(poiin);
			}
		}).start();
	}

}
