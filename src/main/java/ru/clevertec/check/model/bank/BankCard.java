package main.java.ru.clevertec.check.model.bank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public abstract class BankCard {
	 protected String cardNumber;
	 protected String cardHolderName;
	 protected double balance;
}
