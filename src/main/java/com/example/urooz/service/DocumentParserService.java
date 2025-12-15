package com.example.urooz.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class DocumentParserService {

    public String parseDocument(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();

        System.out.println(">>> DEBUG: Starting parsing for file: " + fileName);

        if (fileName == null) throw new IllegalArgumentException("File name cannot be null");
        String lowerCaseName = fileName.toLowerCase();

        String rawText = "";
        if (lowerCaseName.endsWith(".pdf")) {
            rawText = extractTextFromPdf(file.getInputStream());
        } else if (lowerCaseName.endsWith(".docx")) {
            rawText = extractTextFromDocx(file.getInputStream());
        } else {
            throw new IllegalArgumentException("Unsupported file format.");
        }

        System.out.println(">>> DEBUG: RAW EXTRACTED TEXT START <<<");
        System.out.println(rawText);
        System.out.println(">>> DEBUG: RAW EXTRACTED TEXT END <<<");

        return rawText;
    }

    private String extractTextFromPdf(InputStream inputStream) throws IOException {
        try (PDDocument document = Loader.loadPDF(new RandomAccessReadBuffer(inputStream))) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            pdfStripper.setSortByPosition(true);
            return pdfStripper.getText(document).trim();
        }
    }

    private String extractTextFromDocx(InputStream inputStream) throws IOException {
        try (XWPFDocument document = new XWPFDocument(inputStream);
             XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
            return extractor.getText().trim();
        }
    }
}