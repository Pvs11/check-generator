package main.java.ru.clevertec.check.datasource;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import main.java.ru.clevertec.check.model.shop.DiscountCard;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CardStore extends Store implements CardLoader {
	private Map<Integer, DiscountCard> cardMap = new HashMap<>();

	public CardStore(String pathToFile) {
		this.loadToStore(new File(pathToFile));
	}

	@Override
	public void loadToStore(File file) {
		CSVParser parser = new CSVParserBuilder().withSeparator(',').withIgnoreQuotations(false).build();
		try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(file)).withSkipLines(1).withCSVParser(parser).build()) {
			String[] values = null;
			while ((values = csvReader.readNext()) != null) {
				DiscountCard discountCard = DiscountCard.builder()
						.id(Integer.parseInt(values[0]))
						.number(values[1])
						.discount(Integer.parseInt(values[2]))
						.build();
				cardMap.put(discountCard.getId(), discountCard);
			}
		} catch (IOException | CsvValidationException e) {
			throw new RuntimeException(e);
		}
	}

	public DiscountCard getCardByNumber(String number) {
		return cardMap.entrySet().stream().filter(entry -> entry.getValue().getNumber().equals(number)).findFirst().orElseThrow().getValue();
	}
}
