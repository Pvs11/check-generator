package main.java.ru.clevertec.check.datainput;

import java.util.Map;

public interface Input {
	Map<Integer, Integer> readProducts();
	Map<String, String> readDiscountCard();
	Map<String, Double> readBankCard();
}
