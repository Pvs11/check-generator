package main.java.ru.clevertec.check.exceptions;

public class NoCardProvidedException extends RuntimeException {
	public NoCardProvidedException() {
		super("Payment couldn't be processed as no card provided!");
	}
}
