package main.java.ru.clevertec.check.datasource;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.java.ru.clevertec.check.exceptions.NotEnoughProductException;
import main.java.ru.clevertec.check.exceptions.ProductNotFoundException;
import main.java.ru.clevertec.check.model.shop.Product;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class ProductStore extends Store implements ProductLoader {

	private Map<Integer, Product> productMap = new HashMap<>();

	public ProductStore(String pathToFile) {
		this.loadToStore(new File(pathToFile));
	}

	@Override
	public void loadToStore(File file) {
		CSVParser parser = new CSVParserBuilder().withSeparator(',').withIgnoreQuotations(false).build();
		try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(file)).withSkipLines(1).withCSVParser(parser).build()) {
			String[] values = null;
			while ((values = csvReader.readNext()) != null) {
				Product product = Product.builder()
						.id(Integer.parseInt(values[0]))
						.description(values[1])
						.price(Double.parseDouble(values[2]))
						.stockQuantity(Integer.parseInt(values[3]))
						.wholesale(values[4].equals("+"))
						.build();
				productMap.put(product.getId(), product);
			}
		} catch (IOException | CsvValidationException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isProductAvailable(int productId, int quantity) {
		int stockQuantity = productMap.get(productId).getStockQuantity();
		if (!productMap.containsKey(productId)) {
			throw new ProductNotFoundException(productId);
		} else if (quantity > stockQuantity) {
			throw new NotEnoughProductException(String.format("There is not enough items of '%s', available: %s, requested: %s",
					getProductById(productId).getDescription(), stockQuantity, quantity));
		} else return true;
	}

	public Product getProductById(int productId) {
		return productMap.get(productId);
	}

	@Override
	public void removeFromStore(int productId, int quantity) {
		Product product = getProductById(productId);
		product.setStockQuantity(product.getStockQuantity() - quantity);
	}
}
