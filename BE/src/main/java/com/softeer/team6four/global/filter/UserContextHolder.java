package com.softeer.team6four.global.filter;

public class UserContextHolder {
	private static final ThreadLocal<Long> userContext = new ThreadLocal<>();

	public static void set(Long userId) {
		userContext.set(userId);
	}

	public static Long get() {
		return userContext.get();
	}

	public static void clear() {
		userContext.remove();
	}
}
