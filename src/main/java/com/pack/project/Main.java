package com.pack.project;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Main {
	public static void main(String[] args) {

		try {
			// Загружаем HTML страницу
			Document doc = Jsoup.connect("https://zakupki.okmot.kg/popp/view/order/list.xhtml").get();

			// Находим тег <table>, в данном случае мы предполагаем, что это первая таблица на странице
			Element table = doc.select("table").get(1);

			if (table != null) {
				// Находим все теги <tr> внутри таблицы
				Elements rows = table.select("tr");

				for (Element row : rows) {
					// Находим все теги <td> для каждой строки
					Elements cells = row.select("td");

					for (Element cell : cells) {
						// Здесь вы можете обработать содержимое каждой ячейки как вам нужно
						String text = cell.text();
						System.out.println(text);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		SpringApplication.run(Main.class, args);
	}
}
