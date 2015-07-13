package com.hjp.globaltester.control.soap;

public abstract class AbstractProxy<T> {
	protected T handler;
	
	public AbstractProxy(T handler) {
		this.handler = handler;
	}
	
	public T getHandler() {
		return handler;
	}
}
