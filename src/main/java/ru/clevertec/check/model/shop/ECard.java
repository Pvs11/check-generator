package main.java.ru.clevertec.check.model.shop;

public enum ECard {
	DISCOUNT_CARD("discountCard"), BALANCE_CREDIT_CARD("balanceCreditCard"), BALANCE_DEBIT_CARD("balanceDebitCard");

	ECard(String cardName) {
		this.cardName = cardName;
	}

	private String cardName;

	public String getCardName() {
		return cardName;
	}
}
