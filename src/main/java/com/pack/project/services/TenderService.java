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

    @Autowired
    public TenderService(TenderRepository tenderRepository) {
        this.tenderRepository = tenderRepository;
    }

    public void parseAndSaveTenders() {
        try {
            Document doc = Jsoup.connect("https://zakupki.okmot.kg/popp/view/order/list.xhtml").get();
            Element table = doc.select("table").get(1);

            if (table != null) {
                Elements rows = table.select("tr");

                // Пропустить первую строку с заголовками
                for (int i = 1; i < rows.size(); i++) {
                    Element row = rows.get(i);
                    Elements cells = row.select("td");

                    Tender tender = new Tender();
                    // Используйте .ownText() вместо .text(), чтобы получить только текст вне тегов <span>
                    tender.setNumber(cells.get(0).ownText());
                    tender.setCompanyName(cells.get(1).ownText());
                    tender.setPurchaseName(cells.get(3).select("span.nameTender").text()); // Предположим, что название закупки всегда внутри span с классом nameTender
                    tender.setPlannedAmount(cells.get(6).ownText());
                    tender.setDatePublished(cells.get(7).ownText());
                    tender.setBidsSubmissionDeadline(cells.get(8).ownText());

                    tenderRepository.save(tender);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
