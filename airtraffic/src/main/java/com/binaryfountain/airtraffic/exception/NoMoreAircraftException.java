package com.binaryfountain.airtraffic.exception;

public class NoMoreAircraftException extends RuntimeException {

	private static final long serialVersionUID = 5099329510816539849L;

	public NoMoreAircraftException() {
		super("Could not find any aircraft in the queue");
	}
}