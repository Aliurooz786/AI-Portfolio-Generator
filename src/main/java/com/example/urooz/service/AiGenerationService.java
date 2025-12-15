package com.example.urooz.service;

import com.example.urooz.model.PortfolioResponse;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AiGenerationService {

    private static final Logger log = LoggerFactory.getLogger(AiGenerationService.class);
    private final ChatLanguageModel chatLanguageModel;

    public AiGenerationService(ChatLanguageModel chatLanguageModel) {
        this.chatLanguageModel = chatLanguageModel;
    }

    /**
     * Orchestrates the 2-step generation process:
     * 1. Analyze Resume -> Create Specification
     * 2. Use Specification -> Generate HTML/CSS/JS
     */
    public PortfolioResponse generatePortfolio(String resumeText) {

        // --- STEP 1: Specification Extraction ---
        log.info("Step 1: Starting resume analysis and specification extraction.");

        List<ChatMessage> specMessages = new ArrayList<>();
        specMessages.add(SystemMessage.from("You are a resume analyzer. Convert resume text into a structured website specification."));

        String userPromptSpec = """
             From the resume below, extract:
             - Name
             - About
             - Skills
             - Experience
             - Projects
             - Education
             - Contact Links (LinkedIn, GitHub, Email - Verify accuracy)
             - Achievements

             Resume:
             %s
             """;
        specMessages.add(UserMessage.from(String.format(userPromptSpec, resumeText)));

        Response<AiMessage> specResponse = chatLanguageModel.generate(specMessages);
        String websiteSpec = specResponse.content().text();

        log.debug("Generated Specification: {}", websiteSpec);
        log.info("Step 1 Complete: Specification extracted successfully.");

        // --- STEP 2: Frontend Code Generation ---
        log.info("Step 2: Generating frontend code (HTML/CSS/JS).");

        List<ChatMessage> codeMessages = new ArrayList<>();
        codeMessages.add(SystemMessage.from("You are an expert frontend developer."));

        String userPromptCode = """
             Create a portfolio website using the specification below.
             
             **Design Style:**
             Use a 'Premium Dark Theme'. 
             - Background: Deep Slate/Navy (#0f172a).
             - Text: White/Light Grey.
             - Accents: Use Cyan or Blue for buttons and links.
             - Cards: Use subtle glassmorphism or dark cards with borders.

             Output STRICTLY in this format:
             --html--
             HTML CODE
             --html--
             --css--
             CSS CODE
             --css--
             --js--
             JAVASCRIPT CODE
             --js--

             Specification:
             %s
             """;
        codeMessages.add(UserMessage.from(String.format(userPromptCode, websiteSpec)));

        Response<AiMessage> codeResponse = chatLanguageModel.generate(codeMessages);

        log.info("Step 2 Complete: Frontend code generated.");
        return parseResponse(codeResponse.content().text());
    }

    private PortfolioResponse parseResponse(String rawResponse) {
        String html = extractSection(rawResponse, "--html--");
        String css = extractSection(rawResponse, "--css--");
        String js = extractSection(rawResponse, "--js--");
        return new PortfolioResponse(html, css, js);
    }

    private String extractSection(String text, String marker) {
        try {
            String[] parts = text.split(marker);
            if (parts.length >= 2) {
                return parts[1].trim();
            }
        } catch (Exception e) {
            log.error("Failed to extract section using marker: {}", marker, e);
        }
        return "";
    }
}