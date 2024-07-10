package main.java.ru.clevertec.check.exceptions;

import java.util.List;

public class NotEnoughMoneyException extends RuntimeException {
	private String errorMessage = "NOT ENOUGH MONEY";
	public NotEnoughMoneyException() {
		super("Sorry, operation cancelled! Not enough money!");
	}

	public List<String[]> getErrorAsStrings() {
		return List.of(new String[]{"ERROR"}, new String[]{errorMessage});
	}
}
