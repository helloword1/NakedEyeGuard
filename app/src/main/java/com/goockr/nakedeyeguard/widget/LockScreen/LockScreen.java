package com.goockr.nakedeyeguard.widget.LockScreen;

import android.app.Activity;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class LockScreen extends DeviceAdminReceiver {
	static final int RESULT_ENABLE = 1;

	public static class Controller extends Activity {

		DevicePolicyManager mDPM;
		ComponentName mDeviceAdminSample;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			

				//首先我们要获得android设备管理代理
			mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
			
			//LockScreen 继承自 DeviceAdminReceiver
			mDeviceAdminSample = new ComponentName(Controller.this,
					LockScreen.class);
			//得到当前设备管理器有没有激活
			boolean active = mDPM.isAdminActive(mDeviceAdminSample);
			if (!active) { 
				//如果没有激活的话，就去提示用户激活（第一次运行程序时）
				getAdmin();
			}else{
				//如果已经激活的话，就执行立即锁屏
				mDPM.lockNow();
				finish();
			}
			
		//	killMyself ，锁屏之后就立即kill掉我们的Activity，避免资源的浪费;
		//	android.os.Process.killProcess(android.os.Process.myPid());
		}

        public void getAdmin() {
            // Launch the activity to have the user enable our admin.
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                    mDeviceAdminSample);
            startActivityForResult(intent, RESULT_ENABLE);
        }
		
	}
}
