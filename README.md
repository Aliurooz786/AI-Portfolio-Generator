# AI Portfolio Generator

[![Java](https://img.shields.io/badge/Java-17-orange.svg?style=flat&logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green.svg?style=flat&logo=spring)](https://spring.io/projects/spring-boot)
[![Gemini AI](https://img.shields.io/badge/AI-Google%20Gemini%202.5-blue.svg?style=flat&logo=google)](https://deepmind.google/technologies/gemini/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg?style=flat)](LICENSE)

> **Turn your Resume into a Deployed-Ready Portfolio Website in Seconds using Google Gemini 2.5.**

![AI Portfolio Generator Demo](path/to/demo_gif_or_screenshot.png) <!-- Replace with actual screenshot or GIF -->

## 📖 About

**AI Portfolio Generator** is a powerful Full-Stack Java Spring Boot application designed to automate the creation of personal portfolio websites. By leveraging the advanced capabilities of the **Google Gemini 2.5 Flash model** (via LangChain4j), this tool intelligently analyzes your resume (PDF/DOCX) and instantly generates a fully coded, responsive, and visually stunning personal website.

The generated portfolio features a high-end **"Glassmorphism" design** with Neon Aurora animations, ensuring you stand out to recruiters and clients. Secure, efficient, and blazing fast, this tool delivers a deployable ZIP file containing all necessary HTML, CSS, and JS assets in under 20 seconds.

## ✨ Key Features

- **🚀 AI-Powered Parsing:** Utilizes **Apache PDFBox** & **POI** to intelligently extract Name, Skills, Projects, and Experience with high accuracy.
- **🎨 Generative UI Engine:** Automatically writes modern CSS (Glassmorphism, Hover Effects, Responsive Grid) tailored to your resume's content.
- **🔐 Secure Architecture:** Sensitive API Keys are securely managed via `secret.properties` and are never hardcoded.
- **📦 Monolithic Efficiency:** Deploys as a single JAR file with the frontend embedded in Spring Boot Static resources.
- **⚡ Instant Output:** Generates a downloadable ZIP file containing your complete portfolio website in under **20 seconds**.

## 🔄 Architecture Flow

1.  **Upload:** User uploads a Resume (PDF or DOCX) via the web interface.
2.  **Parse:** The backend uses Apache PDFBox/POI to extract raw text and structured data.
3.  **AI Generate:** Google Gemini 2.5 analyzes the extracted data to generate professional content and design structures.
4.  **Build:** The application constructs the HTML, CSS, and JS files.
5.  **Zip Download:** The user downloads the final portfolio as a ready-to-deploy ZIP file.

## 🛠 Tech Stack

| Component | Technology |
| :--- | :--- |
| **Backend** | Java 17, Spring Boot 3.x |
| **AI / LLM** | Google Gemini 2.5 Flash, LangChain4j |
| **Utilities** | Apache PDFBox, Apache POI |
| **Frontend** | HTML5, CSS3 (Advanced Animations), Vanilla JavaScript |
| **Build Tool** | Maven |

## 🚀 Getting Started

Follow these instructions to get the project up and running on your local machine.

### Prerequisites

-   **Java 17** Development Kit (JDK)
-   **Maven** 3.8+
-   **Google Gemini API Key** (Get one from [Google AI Studio](https://aistudio.google.com/))

### Installation

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/Aliurooz786/AI-Portfolio-Generator.git
    cd AI-Portfolio-Generator
    ```

2.  **Configure API Keys:**
    Create a `secret.properties` file in `src/main/resources/` if it doesn't exist, and add your Gemini API Key:
    ```properties
    gemini.api.key=YOUR_ACTUAL_API_KEY_HERE
    ```
    > **Note:** `secret.properties` is added to `.gitignore` to prevent accidental commits of sensitive data.

3.  **Build the project:**
    ```bash
    mvn clean install
    ```

### Running the Application

Run the Spring Boot application using Maven:

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`.

## 📂 Project Structure

```text
AI-Portfolio-Generator/
├── src/
│   ├── main/
│   │   ├── java/com/example/urooz/  # Backend Logic (Controllers, Services)
│   │   └── resources/
│   │       ├── static/              # Frontend Assets (JS, CSS, Images)
│   │       ├── templates/           # HTML Templates
│   │       ├── application.properties
│   │       └── secret.properties    # API Keys (Not committed)
├── target/                          # Compiled artifacts
├── pom.xml                          # Maven dependencies
└── README.md                        # Project Documentation
```

## 🤝 Contributing

Contributions are what make the open-source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1.  Fork the Project
2.  Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3.  Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4.  Push to the Branch (`git push origin feature/AmazingFeature`)
5.  Open a Pull Request

## 👤 Author

**Ali Urooz**

-   LinkedIn: [Your LinkedIn Profile](https://www.linkedin.com/in/your-profile)
-   GitHub: [Aliurooz786](https://github.com/Aliurooz786)

---

<p align="center">Made with ❤️ by Uroj Ali</p>
