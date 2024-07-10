package main.java.ru.clevertec.check.model.shop;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class DiscountCard {
	private int id;
	private String number;
	private double discount;
}
