package main.java.ru.clevertec.check.exceptions;

public class UnrecognisedCardException extends RuntimeException {
	public UnrecognisedCardException() {
		super("Unrecognised card format provided for payment! Please, check your card!");
	}
}
