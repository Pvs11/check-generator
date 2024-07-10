package main.java.ru.clevertec.check.datainput;

import lombok.Getter;
import main.java.ru.clevertec.check.exceptions.NoCardProvidedException;
import main.java.ru.clevertec.check.model.shop.DiscountCard;
import main.java.ru.clevertec.check.exceptions.UnrecognisedCardException;
import main.java.ru.clevertec.check.model.bank.BankCard;
import main.java.ru.clevertec.check.model.bank.BankCreditCard;
import main.java.ru.clevertec.check.model.bank.BankDebitCard;
import main.java.ru.clevertec.check.model.shop.ECard;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static main.java.ru.clevertec.check.CheckRunner.cardStore;

@Getter
public class ConsoleInput implements Input {
	private String[] args;
	private final String productSeparator = "-";
	private final String cardSeparator = "=";
	private final Map<Integer, Integer> productIdAndQuantity = new HashMap<>();
	private final Map<String, String> discountCardNameAndNumber = new HashMap<>();
	private final Map<String, Double> bankCardNameAndBalance = new HashMap<>();

	public ConsoleInput(String[] args) {
		this.args = args;
	}

	@Override
	public Map<Integer, Integer> readProducts() {
		for (String argument : args) {
			List<String> strings = Arrays.asList(argument.split(productSeparator));
			if (strings.getFirst().matches("^[1-9]\\d*")) {
				int id = Integer.parseInt(strings.getFirst());
				int quantity = Integer.parseInt(strings.getLast());
				productIdAndQuantity.merge(id, quantity, Integer::sum);
			}
		}
		return productIdAndQuantity;
	}

	@Override
	public Map<String, String> readDiscountCard() {
		for (String s : args) {
			List<String> strings = Arrays.asList(s.split(cardSeparator));
			if (strings.getFirst().matches("\\w*discount\\w*[cC]ard")) {
				String cardName = strings.getFirst();
				String cardNumber = strings.getLast();
				discountCardNameAndNumber.put(cardName, cardNumber);
			}
		}
		return discountCardNameAndNumber;
	}

	@Override
	public Map<String, Double> readBankCard() {
		for (String s : args) {
			List<String> strings = Arrays.asList(s.split(cardSeparator));
			if (strings.getFirst().matches("\\w*balance\\w*[cC]ard")) {
				bankCardNameAndBalance.put(strings.getFirst(), Double.parseDouble(strings.getLast()));
			}
		}
		return bankCardNameAndBalance;
	}

	public DiscountCard getDiscountCard() {
		DiscountCard discountCard = null;
		if (isDiscountCardPresent()) {
			Map.Entry<String, String> discountCardFromConsole = discountCardNameAndNumber.entrySet().iterator().next();
			discountCard = cardStore.getCardByNumber(discountCardFromConsole.getValue());
		}
		return discountCard;
	}

	public BankCard getBankCard() {
		BankCard bankCard;
		if (isBankCardPresent()) {
			Map.Entry<String, Double> card = bankCardNameAndBalance.entrySet().iterator().next();
			if (card.getKey().equalsIgnoreCase(ECard.BALANCE_CREDIT_CARD.getCardName())) {
				bankCard = new BankCreditCard();
			} else if (card.getKey().equalsIgnoreCase(ECard.BALANCE_DEBIT_CARD.getCardName())) {
				bankCard = new BankDebitCard();
			} else throw new UnrecognisedCardException();
			bankCard.setBalance(card.getValue());
		} else throw new NoCardProvidedException();
		return bankCard;
	}

	public boolean isDiscountCardPresent() {
		return (!discountCardNameAndNumber.isEmpty());
	}

	public boolean isBankCardPresent() {
		return (!bankCardNameAndBalance.isEmpty());
	}
}
