package com.hdj.downapp_market.service;

import com.hdj.downapp_market.activity.MainActivity;
import com.mz.bean.MARKET;
import com.mz.utils.EasyOperateClickUtil;
import com.mz.utils.ActivityUtils;
import com.mz.utils.MyFileUtil;
import com.mz.utils.GlobalConstants;
import com.mz.utils.Logger;
import com.mz.utils.SprefUtil;
import com.mz.utils.VpnUtil;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class HookBroadcast extends BroadcastReceiver {
	boolean market = true;

	@Override
	public void onReceive(final Context context, Intent intent) {
		boolean autoMarket = SprefUtil.getBool(context,
				SprefUtil.C_AUTO_MARKET, true);

		if (intent.getAction().equals(GlobalConstants.HDJ_GIT_BROADCAST)) {
			Logger.i("HookBroadcast===autoMarket----" + autoMarket);
			if (!autoMarket)
				return;
			checkGooglePlay(context);
			// 下载次数纪录
			int count = SprefUtil.getInt(context, SprefUtil.C_COUNT, 0);
			SprefUtil.putInt(context, SprefUtil.C_COUNT, count + 1);
			MainActivity.handleDownload(context);
			handleInstallJump(context);
		}
	}

	/**
	 * 循环检测是否是安装界面，若是则跳转到修改器
	 * 
	 * @param context
	 */
	private void handleInstallJump(final Context context) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (market) {
					String top = ActivityUtils.getLauncherTopApp(context);// getTopActivity(context);
					if (top.equals(GlobalConstants.INSTALL_ACTIVITY)) {
						EasyOperateClickUtil.setEasyTag(context,EasyOperateClickUtil.UNCLICK);
						int flag = -1;
						while ((flag = EasyOperateClickUtil.getEasyTag(context)) != EasyOperateClickUtil.UNCLICK) {
							EasyOperateClickUtil.setEasyTag(context,
									EasyOperateClickUtil.UNCLICK);
						}
						int waitTime = SprefUtil.getInt(context,SprefUtil.C_INSTALL_TIME, 0);
						Logger.i("HookBroadcast===InstallPage----market"+ market + "  waitTime  " + waitTime);
						try {
							Thread.sleep(waitTime * 1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (SprefUtil.getBool(context, SprefUtil.C_AUTO_HOOK,true)) {
							ActivityUtils.openHOOK(context,GlobalConstants.HDJ_GIT_PN);
							market = false;
						}
					}
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	/**
	 * 判断是否是GooglePlay
	 * 
	 * @param context
	 */
	private void checkGooglePlay(Context context) {

		checkCconnectVpn(context);

		Logger.i("------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		if (SprefUtil.getInt(context, SprefUtil.C_MARKET,MARKET.MARKET_BAIDU.value()) == MARKET.MARKET_GP.value()) {
			MyFileUtil.addAccountIndex();
			AccountManager accountManager = AccountManager.get(context);
			Account[] accounts = accountManager.getAccountsByType("com.google");
			Logger.i("ACCOUNT*********************************************************----------before Remove-------"
					+ accounts.length);
			for (int i = 0; i < accounts.length; i++) {
				Account account = accounts[i];
				accountManager.removeAccount(account, null, null);
				Logger.i("ACCOUNT*********************************************************------ Remove-------"
						+ account);
			}
			Account[] accounts2 = accountManager.getAccountsByType("com.google");
			Logger.i("ACCOUNT*********************************************************-----------after Remove-------"
					+ accounts2.length);
			// EasyOperateClickUtil.setIsAddAccountFlag(context,
			// EasyOperateClickUtil.ADD_ACCOUNT);
			// ActivityUtils.openOther(context, GlobalConstants.GP_PN);
			//如果自动注入
			if(EasyOperateClickUtil.getIsAutoInjectAccountFlag(context)==EasyOperateClickUtil.AUTO_INJECT_ACCOUNT){
				EasyOperateClickUtil.setIsAddAccountFlag(context,EasyOperateClickUtil.ADD_ACCOUNT);
			}

		}
		Logger.i("------------------------------------------------------------------------------------------------------------------------------------------------------------------");

	}

	/**
	 * 检测VPN
	 * 
	 * @param context
	 */
	private void checkCconnectVpn(Context context) {
		if (SprefUtil.getBool(context, SprefUtil.C_GOOGLE_VPN, false)) {
			if (!VpnUtil.isVpnUsed()) {
				ActivityUtils.openApkByDetail(context,
						GlobalConstants.PROXY_PN, GlobalConstants.PROXY_CN);
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// if (EasyOperateClickUtil.getAutoVpnFlag(context) ==
			// EasyOperateClickUtil.AUTO_VPN) {
			// // if (!VpnUtil.isVpnUsed()) { // 判断VPN是否连接
			// // Intent intent2 = new Intent();
			// // intent2.setAction("android.net.vpn.SETTINGS");
			// // intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// // context.startActivity(intent2);
			// // return;
			// // }
			// if(!VpnUtil.isVpnUsed()){
			// ActivityUtils.openApkByDetail(context, GlobalConstants.PROXY_PN,
			// GlobalConstants.PROXY_CN);
			// }
			// }
		}

	}

}
