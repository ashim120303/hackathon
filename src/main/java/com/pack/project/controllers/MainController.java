package com.pack.project.controllers;

import com.pack.project.models.Tender;
import com.pack.project.repos.TenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {
    @Autowired
    private TenderRepository tenderRepository;

    @GetMapping("/")
    public String home(Model model) {
        List<Tender> tenders = tenderRepository.findAll();
        model.addAttribute("tenders", tenders);
        return "index";
    }
}
