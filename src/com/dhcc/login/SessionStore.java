package com.dhcc.login;

import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpSession;

public class SessionStore {
	private Vector<Object> store = new Vector<Object>();

	private static SessionStore instance = null;

	private final Object lock = new Object();

	/**
	 * private constructor
	 */
	private SessionStore() {
	}

	/**
	 * invalidate session specified by employee id
	 * 
	 * @param employeeID
	 * @return
	 */
	public HttpSession getSession(String userId) {
		synchronized (lock) {
			Enumeration<Object> enumeration = store.elements();
			while (enumeration.hasMoreElements()) {
				HttpSession session = (HttpSession) enumeration.nextElement();
				if (session != null) {
					UserInfoBean bean = (UserInfoBean) session
							.getAttribute("userInfoBean");
					if (bean == null || bean.getEmployee() == null
							|| bean.getEmployee().getUserId() == null) {
						bean = null;
						session = null;
						continue;
					} else {
						if (bean.getEmployee().getUserId().equals(userId)) {
							return session;
						}
						bean = null;
						session = null;
					}
				}
			}
			return null;
		}
	}

	/**
	 * singleton method once you got this object, you should get a lock on this
	 * object before excute any method of it.
	 * 
	 * @return SessionStore
	 */
	public synchronized static SessionStore getInstance() {
		if (instance == null)
			instance = new SessionStore();
		return instance;
	}

	/**
	 * get stored list of user object
	 * 
	 * @return
	 */
	public Vector<Object> getStore() {
		synchronized (lock) {
			return store;
		}
	}

	/**
	 * wrapper for vector remove
	 * 
	 * @param obj
	 */
	public void remove(Object obj) {
		synchronized (lock) {
			if (obj != null) {
				store.remove(obj);
			}
			store.remove(null);
		}
	}

	/**
	 * wrapper for add of vector
	 * 
	 * @param obj
	 */
	public void add(Object obj) {
		synchronized (lock) {
			if (!store.contains(obj)) {
				store.add(obj);
			}
		}
	}

	/**
	 * judge whether there is a emoployee already
	 * 
	 * @param obj
	 * @return
	 */
	public boolean contains(String userId) {
		synchronized (lock) {
			Enumeration<Object> enumeration = store.elements();
			while (enumeration.hasMoreElements()) {
				HttpSession session = (HttpSession) enumeration.nextElement();
				if (session != null) {
					UserInfoBean bean = (UserInfoBean) session
							.getAttribute("userInfoBean");
					if (bean == null || bean.getEmployee() == null
							|| bean.getEmployee().getUserId() == null) {
						bean = null;
						session = null;
						continue;
					} else {
						if (bean.getEmployee().getUserId().equals(userId)) {
							bean = null;
							return true;
						}
						bean = null;
						session = null;
					}
				}
			}
			return false;
		}
	}
}