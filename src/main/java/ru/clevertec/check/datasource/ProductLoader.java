package main.java.ru.clevertec.check.datasource;

import main.java.ru.clevertec.check.model.shop.Product;

import java.io.File;

public interface ProductLoader extends Loader {
	void loadToStore(File file);

	boolean isProductAvailable(int productId, int requiredQuantity);

	void removeFromStore(int productId, int quantity);

	Product getProductById(int productId);
}
