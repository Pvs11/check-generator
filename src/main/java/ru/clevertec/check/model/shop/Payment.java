package main.java.ru.clevertec.check.model.shop;

import lombok.Getter;
import lombok.Setter;
import main.java.ru.clevertec.check.model.bank.BankCard;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Getter
@Setter
public class Payment {
	private BankCard bankCard;
	private Order order;
	private LocalDateTime processedAt;
	private boolean isProcessed = false;

	public Payment(Order order, BankCard bankCard) {
		this.bankCard = bankCard;
		this.order = order;
	}

	public void payForOrder() {
		double totalPrice = order.getTotalWithDiscount();
		double balance = bankCard.getBalance();
		if (totalPrice <= balance) {
			bankCard.setBalance(balance - totalPrice);
			isProcessed = true;
			processedAt = LocalDateTime.now();
		}
	}

	public void printReceipt() {
		StringBuilder sb = new StringBuilder();
		sb.append("Receipt\n")
				.append("order date/time: ").append(getProcessedAt().format(DateTimeFormatter.ofPattern("YYYY/mm/dd hh:mm")))
				.append("\n----------------\n")
				.append("products:\n");
		Map<Product, Integer> cart = order.getCart();
		for (Product product : cart.keySet()) {
			sb.append(product.getDescription()).append(" - ").append(cart.get(product)).append("*")
					.append(new DecimalFormat("0.##").format(product.getPrice())).append("$").append(" = ")
					.append(new DecimalFormat("0.##").format(product.getPrice()* cart.get(product)))
					.append("$").append("\n");
		}
		DiscountCard discountCard = order.getDiscountCard();
		if (discountCard.getId() != 0) {
			sb.append("\ndiscount card number provided: ").append(discountCard.getNumber()).append("\n")
					.append("discount: ").append(discountCard.getDiscount()).append("%\n");
		}
		if (order.isCartContainWholesale()) {
			sb.append("discount for wholesale: ").append(order.calculateDiscountForWholesale()).append("$").append("\n");
		}
		sb.append("total discount: ").append(new DecimalFormat("0.##").format(order.getTotalDiscount())).append("$").append("\n----------------\n")
						.append("total price: ").append(new DecimalFormat("0.##").format(order.getTotalPrice())).append("$").append("\n")
						.append("payed after discount: ").append(new DecimalFormat("0.##").format(order.getTotalWithDiscount())).append("$");
		System.out.println(sb);
	}
}
