package com.poiin.yourown.network;

import android.util.Log;

import com.poiin.yourown.poiin.Poiin;

public class RestClientServiceImpl implements RestClientService{

	public void sendPoiin(Poiin poiin) {
		Log.i("RestClient", poiin.toString());
	}

}
