package com.poiin.yourown.storage;

public interface Data {
	
	LoginOption getLoginOption();
	void setLoginOption(LoginOption option);
	void storeTwitterDetails(String accessKey, String accessSecret);
	/**
	 * 
	 * @return Array containing the accesKey in the first possition and the accesSecret in the second
	 */
	String[] getTwitterDetails();
	enum LoginOption{
		FACEBOOK,TWITTER,UNSETTED;
	}
}
