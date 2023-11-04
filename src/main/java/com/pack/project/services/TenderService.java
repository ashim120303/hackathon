package com.pack.project.services;

import com.pack.project.models.Tender;
import com.pack.project.repos.TenderRepository;
import jakarta.transaction.Transactional;
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

    @Autowired
    public TenderService(TenderRepository tenderRepository) {
        this.tenderRepository = tenderRepository;
    }

    @Transactional  // Убедитесь, что транзакции корректно управляются
    public void parseAndSaveTenders() throws IOException {
        Document doc = Jsoup.connect("https://zakupki.okmot.kg/popp/view/order/list.xhtml").get();

        // Предполагаем, что нужная информация находится во второй таблице
        Element table = doc.select("table").get(1);

        // Предполагаем, что заголовки таблицы не нужны, начинаем со второй строки
        Elements rows = table.select("tr:gt(0)");

        for (Element row : rows) {
            Elements cells = row.select("td");
            if (cells.size() > 6) { // Убедитесь, что есть достаточно столбцов
                Tender tender = new Tender();
                tender.setNumber(cells.get(0).text());
                tender.setCompanyName(cells.get(1).text());
                // предполагается, что procurementType и purchaseName находятся в ячейке с индексом 3
                tender.setProcurementType(cells.get(2).text());
                tender.setPurchaseName(cells.get(3).select("span.nameTender").text());
                // Учитываем, что Planned amount и Date published могут быть в других индексах, исправьте индексы, если это не так
                tender.setPlannedAmount(cells.get(4).text().replaceAll(",", "")); // Удалите запятые в числах, если они есть
                tender.setDatePublished(cells.get(5).text());
                tender.setBidsSubmissionDeadline(cells.get(6).text());

                tenderRepository.save(tender); // Сохраняем в базу данных
            }
        }
    }
}
