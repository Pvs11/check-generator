package main.java.ru.clevertec.check.dataoutput;


import main.java.ru.clevertec.check.model.shop.Payment;

public interface Uploader {
	void uploadToFile(String filename, Payment payment);
}
