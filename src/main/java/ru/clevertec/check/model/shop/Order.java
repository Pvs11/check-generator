package main.java.ru.clevertec.check.model.shop;

import lombok.Getter;
import lombok.Setter;
import main.java.ru.clevertec.check.datainput.ConsoleInput;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static main.java.ru.clevertec.check.CheckRunner.productStore;

@Setter
@Getter
public class Order {

	private LocalDate date;
	private LocalTime time;
	private double totalPrice;
	private double totalDiscount;
	private double totalWithDiscount;
	private Map<Product, Integer> cart = new HashMap<>();
	private DiscountCard discountCard;
	private int wholeSaleMinimum;
	private double wholeSaleDiscount;

	public Order(int wholeSaleMinimum, double wholeSaleDiscountPercentage) {
		this.wholeSaleMinimum = wholeSaleMinimum;
		this.wholeSaleDiscount = wholeSaleDiscountPercentage;
	}

	public void makeOrder(ConsoleInput inputData) {
		this.setTime(LocalTime.now());
		this.setDate(LocalDate.now());
		if (inputData.isDiscountCardPresent()) {
			this.setDiscountCard(inputData.getDiscountCard());
		} else this.setDiscountCard(new DiscountCard(0, "", 0));
		for (Map.Entry<Integer, Integer> entry : inputData.getProductIdAndQuantity().entrySet()) {
			int productId = entry.getKey();
			int requestedQuantity = entry.getValue();
			if (productStore.isProductAvailable(productId, requestedQuantity)) {
				this.addToCart(productStore.getProductById(productId), requestedQuantity);
				productStore.removeFromStore(productId, requestedQuantity);
			}
		}
		for (Map.Entry<Product, Integer> item : cart.entrySet()) {
			totalPrice += item.getKey().getPrice() * item.getValue();
			totalDiscount += Double.parseDouble(calculateDiscountPerProduct(item.getKey()));
		}
		totalWithDiscount = totalPrice - totalDiscount;
	}

	public List<String[]> getOrderInLines() {
		List<String[]> listOfLines = new ArrayList<>();
		listOfLines.add(new String[]{"Date", "Time"});
		listOfLines.add(new String[]{date.toString(), time.format(DateTimeFormatter.ofPattern("HH:mm:ss"))});
		listOfLines.add(new String[]{});
		listOfLines.add(new String[]{"QTN", "DESCRIPTION", "PRICE", "DISCOUNT", "TOTAL"});
		for (Product product : cart.keySet()) {
			listOfLines.add(new String[]{cart.get(product).toString(), product.getDescription(), (product.getPrice() + "$"),
					calculateDiscountPerProduct(product),
					String.format("%.2f", product.getPrice() * cart.get(product))});
		}
		listOfLines.add(new String[]{});
		if (discountCard.getId() != 0) {
			listOfLines.add(new String[]{"DISCOUNT CARD", "DISCOUNT PERCENTAGE"});
			listOfLines.add(new String[]{discountCard.getNumber(), discountCard.getDiscount() + "%"});
			listOfLines.add(new String[]{});
		}
		listOfLines.add(new String[]{"TOTAL PRICE", "TOTAL DISCOUNT", "TOTAL WITH DISCOUNT"});
		listOfLines.add(new String[]{String.format("%.2f", getTotalPrice()) + "$",
				String.format("%.2f", getTotalDiscount()) + "$",
				String.format("%.2f", getTotalPrice() - getTotalDiscount()) + "$"});
		return listOfLines;
	}

	public boolean isCartContainWholesale() {
		for (Product product : cart.keySet()) {
			if (isWholesaleApplied(product)) {
				return true;
			}
		}
		return false;
	}

	public String calculateDiscountForWholesale() {
		double totalDiscount = 0;
		for (Product product : cart.keySet()) {
			if (isWholesaleApplied(product)) {
			totalDiscount += product.getPrice() * cart.get(product) * wholeSaleDiscount / 100;
			}
		}
		return new DecimalFormat("0.##").format(totalDiscount);
	}

	private boolean isWholesaleApplied(Product productInCart) {
		int quantity = cart.get(productInCart);
		return quantity >= wholeSaleMinimum && productInCart.isWholesale();
	}

	private void addToCart(Product product, int quantity) {
		if (!cart.containsKey(product)) {
			cart.put(product, quantity);
		} else {
			int updatedQuantity = cart.get(product) + quantity;
			cart.put(product, updatedQuantity);
		}
	}

	private String calculateDiscountPerProduct(Product productInCart) {
		if (isWholesaleApplied(productInCart)) {
			return new DecimalFormat("0.##").format(productInCart.getPrice() * cart.get(productInCart) * wholeSaleDiscount / 100);
		} else if (discountCard.getId() != 0) {
			return new DecimalFormat("0.##").format(productInCart.getPrice() * cart.get(productInCart) * discountCard.getDiscount() / 100);
		} else return "0";
	}
}
