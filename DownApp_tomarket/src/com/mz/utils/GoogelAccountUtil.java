package com.mz.utils;

import java.lang.reflect.Method;

import android.content.ContentResolver;
import android.content.Context;

public class GoogelAccountUtil {
	public static String getPassword(Context context, String email,
			String password) {
		Class<?> clazz;
		try {
			clazz = Class
					.forName("com.google.android.gsf.loginservice.PasswordEncrypter");
			Method method = clazz.getDeclaredMethod("encryptPassword",
					ContentResolver.class, String.class, String.class);
			if (method != null) {
				Object o = method.invoke(null, context.getContentResolver(),email, password);
				return o.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}
}
