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
        specMessages.add(SystemMessage.from("You are a resume analyzer. Convert resume text into a highly structured website specification using strict Markdown."));

        String userPromptSpec = """
             Extract and format the following resume into a clean, highly structured Markdown specification.
             Refine verbose bullet points into concise, web-friendly copy.
             Do not include conversational filler.
             
             Required Sections:
             - Name & Headline
             - About (Short summary)
             - Skills (Comma separated list)
             - Experience (Role, Company, Dates, 2-3 concise bullet points)
             - Projects (Title, Description, Technologies)
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
        codeMessages.add(SystemMessage.from("You are an expert Frontend Architect and UI/UX Designer."));

        String userPromptCode = """
                 Create a breathtaking, modern personal portfolio website based on the provided resume specification.
                 
                 **DESIGN SYSTEM & UI/UX:**
                 - **Style:** "Framer-style", highly polished, minimal, and premium.
                 - **Theme:** Deep Space Dark Mode (Background: #0A0A0A with subtle radial gradients using #1A1A2E and #16213E).
                 - **Typography:** Import and use 'Inter' or 'Outfit' from Google Fonts. High contrast for headings (pure white), muted grays (#A3A3A3) for paragraphs.
                 - **Glassmorphism:** For all cards, navbar, and containers, use: `background: rgba(255, 255, 255, 0.03); backdrop-filter: blur(16px); border: 1px solid rgba(255, 255, 255, 0.05); border-radius: 16px;`.
                 - **Accents:** Use a vibrant gradient (e.g., Cyan #00F2FE to Deep Blue #4FACFE) for primary buttons, hover states, and important text highlights.
            
                 **REQUIRED SECTIONS & LAYOUT:**
                 1. **Sticky Navbar:** Logo/Name on the left, navigation links on the right. Glassmorphism effect.
                 2. **Hero Section:** Large, bold animated headline. Short subtitle. Primary "Contact Me" button. Add a subtle floating animation to background elements.
                 3. **About & Skills:** Grid layout. Display skills as modern, pill-shaped tags with hover effects.
                 4. **Experience:** A vertical connected timeline layout. Include role, company, dates, and concise bullet points.
                 5. **Projects:** CSS Grid layout (responsive). Glassmorphism cards with title, description, and "View Project" links. Cards must scale up slightly on hover.
                 6. **Footer/Contact:** Centered layout with social links and email.
            
                 **TECHNICAL & RESPONSIVE REQUIREMENTS:**
                 - Use CSS Flexbox and Grid extensively.
                 - Include @media queries to ensure the site looks perfect on mobile devices (stack grids to 1 column, adjust padding).
                 - Use smooth scrolling (html { scroll-behavior: smooth; }).
                 
                 **JAVASCRIPT CAPABILITIES:**
                 - Implement an IntersectionObserver in the JS file to fade-in and slide-up elements as they scroll into view.
            
                 **CRITICAL PARSER INSTRUCTIONS:**
                 1. The HTML MUST include `<link rel="stylesheet" href="style.css">` in the `<head>`.
                 2. The HTML MUST include `<script src="script.js"></script>` at the end of the `<body>`.
                 3. Ensure the CSS class names used in HTML match exactly with the CSS code.
                 4. You MUST output EXACTLY three blocks of code, separated by the exact markers below.
                 5. DO NOT wrap the code in markdown code fences (no ```html, no ```css). Just raw code between the markers.
            
                 Output STRICTLY in this format:
                 --html--
                 <!DOCTYPE html>
                 <html>...</html>
                 --html--
                 --css--
                 :root { ... }
                 --css--
                 --js--
                 document.addEventListener(...)
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