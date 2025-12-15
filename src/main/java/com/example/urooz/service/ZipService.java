package com.example.urooz.service;

import com.example.urooz.model.PortfolioResponse;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ZipService {

    public byte[] createZipFile(PortfolioResponse portfolio) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (ZipOutputStream zos = new ZipOutputStream(baos)) {

            addToZip(zos, "index.html", portfolio.getHtml());

            addToZip(zos, "style.css", portfolio.getCss());

            addToZip(zos, "script.js", portfolio.getJs());

        }

        return baos.toByteArray();
    }

    private void addToZip(ZipOutputStream zos, String fileName, String content) throws IOException {
        if (content == null) content = "";
        ZipEntry entry = new ZipEntry(fileName);
        zos.putNextEntry(entry);
        zos.write(content.getBytes());
        zos.closeEntry();
    }
}