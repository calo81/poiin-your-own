package com.poiin.yourown.social.facebook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.R;
import com.poiin.yourown.ApplicationState;
import com.poiin.yourown.Main;

public class FacebookAuthentication extends Activity {

    private Facebook facebook = new Facebook("149212958485869");
    private ProgressDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facebook_login);
        facebook.authorize(this, new DialogListener() {
			
			public void onFacebookError(FacebookError e) {
				// TODO Auto-generated method stub
				
			}
			
			public void onError(DialogError e) {
				// TODO Auto-generated method stub
				
			}
			
			public void onComplete(Bundle values) {
				((ApplicationState)getApplication()).setFacebook(facebook);
				getMyFacebookProofileAndLoginToPoiin();				           
			}

			private void getMyFacebookProofileAndLoginToPoiin() {
				progressDialog = ProgressDialog.show(FacebookAuthentication.this, "Processing . . .", "Retrieving User Information ...", true, false);
				new Thread(new Runnable() {					
					public void run() {				
						((ApplicationState)getApplication()).setMe(getMyFacebookProfile());	
						handler.sendEmptyMessage(1);
					}
				}).start();
				
			}

			private String getMyFacebookProfile(){
				try {
					return facebook.request("me");
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			
			public void onCancel() {
				// TODO Auto-generated method stub
				
			}
		});
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebook.authorizeCallback(requestCode, resultCode, data);
    }
    
    private final Handler handler = new Handler() {

        @Override
        public void handleMessage(final Message msg) {
        	progressDialog.dismiss();
        	startActivity(new Intent(FacebookAuthentication.this, Main.class));
            finish();
        }
    };
}