package main.java.ru.clevertec.check.exceptions;

public class NotEnoughProductException extends RuntimeException {
	public NotEnoughProductException(String msg) {
		super(msg);
	}
}
