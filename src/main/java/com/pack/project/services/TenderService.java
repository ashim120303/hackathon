package com.pack.project.services;

import com.pack.project.models.Tender;
import com.pack.project.repos.TenderRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TenderService {

    private final TenderRepository tenderRepository;
    private static final String BASE_URL = "https://zakupki.okmot.kg/popp/view/order/list.xhtml?page=";

    @Autowired
    public TenderService(TenderRepository tenderRepository) {
        this.tenderRepository = tenderRepository;
    }

    public void parseAndSaveTenders() {
        int pageNumber = 1;
        while (true) {
            try {
                Document doc = Jsoup.connect(BASE_URL + pageNumber).get();
                Element table = doc.select("table").get(1);

                if (table == null) {
                    break; // Выходим из цикла, если таблица не найдена
                }

                Elements rows = table.select("tr");
                if (rows.size() <= 1) {
                    break; // Выходим из цикла, если нет данных для обработки
                }

                for (int i = 1; i < rows.size(); i++) {
                    Element row = rows.get(i);
                    Elements cells = row.select("td");

                    String number = cells.get(0).ownText().trim();

                    // Проверка, существует ли уже запись с таким number
                    if (!tenderRepository.existsByNumber(number)) {
                        Tender tender = new Tender();
                        tender.setNumber(number);
                        tender.setCompanyName(cells.get(1).ownText().trim());
                        tender.setPurchaseName(cells.get(3).select("span.nameTender").text().trim());
                        tender.setPlannedAmount(cells.get(6).ownText().trim());
                        tender.setDatePublished(cells.get(7).ownText().trim());
                        tender.setBidsSubmissionDeadline(cells.get(8).ownText().trim());

                        tenderRepository.save(tender);
                    }
                    // Если запись с таким number уже существует, она не будет сохранена
                }

                pageNumber++; // Переход к следующей странице

            } catch (IOException e) {
                e.printStackTrace();
                break; // В случае ошибки прерываем цикл
            }
        }
    }
}
