const fileInput = document.getElementById('resumeInput');
const dropArea = document.getElementById('dropArea');
const fileContent = document.getElementById('fileContent');
const generateBtn = document.getElementById('generateBtn');
const loadingDiv = document.getElementById('loading');
const successDiv = document.getElementById('success');
const errorDiv = document.getElementById('error');

// 1. Handle Click Upload
dropArea.addEventListener('click', () => fileInput.click());

fileInput.addEventListener('change', function() {
    handleFile(this.files[0]);
});

// 2. Handle Drag & Drop
dropArea.addEventListener('dragover', (e) => {
    e.preventDefault();
    dropArea.classList.add('dragover');
});

dropArea.addEventListener('dragleave', () => {
    dropArea.classList.remove('dragover');
});

dropArea.addEventListener('drop', (e) => {
    e.preventDefault();
    dropArea.classList.remove('dragover');
    const file = e.dataTransfer.files[0];
    fileInput.files = e.dataTransfer.files; // Sync with input
    handleFile(file);
});

// Update UI when file is selected
function handleFile(file) {
    if (file) {
        // Change icon to document
        let iconClass = file.name.endsWith('.pdf') ? 'fa-file-pdf' : 'fa-file-word';
        fileContent.innerHTML = `
            <i class="fa-regular ${iconClass}" style="font-size: 2.5rem; color: #3b82f6;"></i>
            <div class="file-info" style="margin-top: 10px; color: #38bdf8;">${file.name}</div>
            <div class="file-hint" style="color: #4ade80;">Ready to Generate</div>
        `;
        dropArea.style.borderColor = "#38bdf8";
        errorDiv.classList.add('hidden');
    }
}

// 3. Main Generate Function
async function generatePortfolio() {
    const file = fileInput.files[0];

    if (!file) {
        shakeElement(dropArea); // Visual cue if no file
        return;
    }

    // UI Updates
    generateBtn.disabled = true;
    generateBtn.innerHTML = '<i class="fa-solid fa-circle-notch fa-spin"></i> &nbsp; Processing...';
    loadingDiv.style.display = 'block';
    successDiv.style.display = 'none';
    errorDiv.classList.add('hidden');

    const formData = new FormData();
    formData.append("file", file);

    try {
        const response = await fetch('/api/portfolio/generate', {
            method: 'POST',
            body: formData
        });

        if (!response.ok) throw new Error("Server Error");

        // Download ZIP
        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `Portfolio_${file.name.split('.')[0]}.zip`;
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
        a.remove();

        // Success State
        loadingDiv.style.display = 'none';
        successDiv.style.display = 'block';
        generateBtn.innerHTML = '<i class="fa-solid fa-wand-magic-sparkles"></i> &nbsp; Generate Again';
        generateBtn.disabled = false;

    } catch (err) {
        console.error(err);
        loadingDiv.style.display = 'none';
        errorDiv.innerText = "❌ Failed to generate. Please try again.";
        errorDiv.classList.remove('hidden');
        generateBtn.innerHTML = '<i class="fa-solid fa-rotate-right"></i> &nbsp; Retry';
        generateBtn.disabled = false;
    }
}

// Helper: Shake Animation for Error
function shakeElement(element) {
    element.style.animation = "shake 0.5s";
    setTimeout(() => element.style.animation = "none", 500);
}

// Add shake keyframes dynamically
const styleSheet = document.createElement("style");
styleSheet.innerText = `
@keyframes shake {
  0% { transform: translateX(0); }
  25% { transform: translateX(-5px); }
  50% { transform: translateX(5px); }
  75% { transform: translateX(-5px); }
  100% { transform: translateX(0); }
}`;
document.head.appendChild(styleSheet);