package com.hjp.globaltester.control.soap;

/**
 * This is the root of all SOAP proxy implementations. The type parameter T
 * should be the object providing the actual functionality.
 * 
 * @author mboonk
 *
 * @param <T>
 */
public abstract class AbstractProxy<T> {
	protected T handler;

	public AbstractProxy(T handler) {
		this.handler = handler;
	}

	public T getHandler() {
		return handler;
	}
}
