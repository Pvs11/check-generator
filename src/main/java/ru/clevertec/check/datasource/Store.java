package main.java.ru.clevertec.check.datasource;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
@Getter
public abstract class Store {
	private final Map<Integer, CardStore> cardMap = new HashMap<>();
}
