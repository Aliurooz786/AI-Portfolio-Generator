package com.example.urooz.controller;

import com.example.urooz.model.PortfolioResponse;
import com.example.urooz.service.AiGenerationService;
import com.example.urooz.service.DocumentParserService;
import com.example.urooz.service.ZipService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/portfolio")
@CrossOrigin(origins = "*")
public class PortfolioController {

    private final DocumentParserService parserService;
    private final AiGenerationService aiService;
    private final ZipService zipService;

    public PortfolioController(DocumentParserService parserService, AiGenerationService aiService, ZipService zipService) {
        this.parserService = parserService;
        this.aiService = aiService;
        this.zipService = zipService;
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generatePortfolio(@RequestParam("file") MultipartFile file) {
        try {
            String resumeText = parserService.parseDocument(file);

            PortfolioResponse portfolio = aiService.generatePortfolio(resumeText);

            byte[] zipData = zipService.createZipFile(portfolio);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"portfolio_website.zip\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(zipData);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}