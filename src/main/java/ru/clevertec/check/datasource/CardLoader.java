package main.java.ru.clevertec.check.datasource;

import main.java.ru.clevertec.check.model.shop.DiscountCard;

public interface CardLoader extends Loader {
	DiscountCard getCardByNumber(String number);
}
