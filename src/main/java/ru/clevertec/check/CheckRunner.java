package main.java.ru.clevertec.check;

import main.java.ru.clevertec.check.datainput.ConsoleInput;
import main.java.ru.clevertec.check.dataoutput.CSVUploader;
import main.java.ru.clevertec.check.dataoutput.Uploader;
import main.java.ru.clevertec.check.datasource.CardLoader;
import main.java.ru.clevertec.check.datasource.CardStore;
import main.java.ru.clevertec.check.datasource.ProductLoader;
import main.java.ru.clevertec.check.datasource.ProductStore;
import main.java.ru.clevertec.check.model.shop.Order;
import main.java.ru.clevertec.check.model.shop.Payment;

public class CheckRunner {
	public static final ProductLoader productStore = new ProductStore("src/main/resources/products.csv");
	public static final CardLoader cardStore = new CardStore("src/main/resources/discountCards.csv");

	public static void main(String[] args) {
		ConsoleInput input = new ConsoleInput(args);
		input.readProducts();
		input.readDiscountCard();
		input.readBankCard();

		Order order = new Order(5, 10);
		order.makeOrder(input);

		Payment payment = new Payment(order, input.getBankCard());
		payment.payForOrder();

		Uploader uploader = new CSVUploader();
		uploader.uploadToFile("result.csv", payment);

		payment.printReceipt();
	}
}