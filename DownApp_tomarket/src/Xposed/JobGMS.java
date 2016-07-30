package Xposed;

import android.view.MotionEvent;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;

public class JobGMS extends JobAbstract {

	private static JobGMS instance;

	private JobGMS() {
		// TODO Auto-generated constructor stub
	}

	public static JobGMS getGMS() {
		if (instance == null) {
			synchronized (JobBaiDu.class) {
				if (instance == null) {
					instance = new JobGMS();
				}
			}
		}
		return instance;
	}
	@Override
	public void handleMethod(String packageName, ClassLoader classLoader) {
		HookMethod(View.class, "dispatchTouchEvent", packageName,
				MethodInt.DIS, MotionEvent.class);
	}
	@Override
	public void handleAftreMethod(String packageName, MethodHookParam param,
			int type) {
		// TODO Auto-generated method stub
		super.handleAftreMethod(packageName, param, type);
	}

}
