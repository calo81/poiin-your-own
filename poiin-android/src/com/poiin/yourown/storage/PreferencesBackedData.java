package com.poiin.yourown.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferencesBackedData implements Data {
	private SharedPreferences preferences;
	private static final String PREFERENCES_NAME = "poiin_preferences";
	private static final String LOGIN_OPTION_PREFERENCE = "LoginOption";
	private static final String TWITTER_ACCESS_KEY_PREFERENCE = "twitterAccessKey";
	private static final String TWITTER_ACCESS_SECRET_PREFERENCE = "twitterAccesSecret";

	public PreferencesBackedData(Context context) {
		preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
	}

	@Override
	public LoginOption getLoginOption() {
		String logOption = preferences.getString(LOGIN_OPTION_PREFERENCE, "UNSETTED");
		return LoginOption.valueOf(logOption);
	}

	@Override
	public void setLoginOption(LoginOption option) {
		Editor prefEditor = preferences.edit();
		prefEditor.putString(LOGIN_OPTION_PREFERENCE, option.toString());
		prefEditor.commit();
	}

	@Override
	public void storeTwitterDetails(String accessKey, String accessSecret) {
		Editor prefEditor = preferences.edit();
		prefEditor.putString(TWITTER_ACCESS_KEY_PREFERENCE, accessKey);
		prefEditor.putString(TWITTER_ACCESS_SECRET_PREFERENCE, accessSecret);
		prefEditor.commit();
	}

	@Override
	public String[] getTwitterDetails() {
		String[] twitterDetails = new String[2];
		twitterDetails[0] = preferences.getString(TWITTER_ACCESS_KEY_PREFERENCE,null);
		twitterDetails[1] = preferences.getString(TWITTER_ACCESS_SECRET_PREFERENCE,null);
		return twitterDetails[0]!=null?twitterDetails:null;
	}

}
