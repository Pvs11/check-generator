package main.java.ru.clevertec.check.model.shop;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class Product {
	private int id;
	private String description;
	private double price;
	private int stockQuantity;
	boolean wholesale;

}
