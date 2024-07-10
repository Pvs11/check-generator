package main.java.ru.clevertec.check.dataoutput;

import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import main.java.ru.clevertec.check.exceptions.NotEnoughMoneyException;
import main.java.ru.clevertec.check.model.shop.Payment;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CSVUploader implements Uploader {
	@Override
	public void uploadToFile(String filename, Payment payment) {
		Path pathToFile = Paths.get(filename);
		try (CSVWriter writer = (CSVWriter) new CSVWriterBuilder(new FileWriter(pathToFile.toString()))
				.withQuoteChar(CSVWriter.NO_QUOTE_CHARACTER)
				.withSeparator(';').build()) {
			if (payment.isProcessed()) {
				writer.writeAll(payment.getOrder().getOrderInLines());
			} else {
				writer.writeAll(new NotEnoughMoneyException().getErrorAsStrings());
				throw new NotEnoughMoneyException();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
