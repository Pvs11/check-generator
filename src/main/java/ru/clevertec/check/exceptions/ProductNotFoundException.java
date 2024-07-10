package main.java.ru.clevertec.check.exceptions;

public class ProductNotFoundException extends RuntimeException {
	public ProductNotFoundException(int productId) {
		super(String.format("Product with id = %s not found! Please check your input.", productId));

	}
}
