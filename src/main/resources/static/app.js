const lengthInput = document.getElementById("length");
const lengthValue = document.getElementById("lengthValue");
const generateBtn = document.getElementById("generateBtn");
const copyBtn = document.getElementById("copyBtn");
const analyzeBtn = document.getElementById("analyzeBtn");

const passwordOutput = document.getElementById("passwordOutput");
const analyzeInput = document.getElementById("analyzeInput");

const strengthText = document.getElementById("strengthText");
const strengthFill = document.getElementById("strengthFill");

const entropyBits = document.getElementById("entropyBits");
const poolSize = document.getElementById("poolSize");
const selectedSets = document.getElementById("selectedSets");
const crackOnline = document.getElementById("crackOnline");
const crackOfflineFast = document.getElementById("crackOfflineFast");
const crackOfflineSlow = document.getElementById("crackOfflineSlow");
const noteText = document.getElementById("noteText");
const message = document.getElementById("message");

lengthInput.addEventListener("input", () => {
    lengthValue.textContent = lengthInput.value;
});

generateBtn.addEventListener("click", generatePassword);
analyzeBtn.addEventListener("click", analyzePassword);

copyBtn.addEventListener("click", async () => {
    if (!passwordOutput.value) {
        message.textContent = "Generate a password first.";
        return;
    }

    try {
        await navigator.clipboard.writeText(passwordOutput.value);
        message.textContent = "Password copied to clipboard.";
    } catch (e) {
        message.textContent = "Clipboard copy failed. Copy it manually.";
    }
});

async function generatePassword() {
    message.textContent = "";

    const payload = {
        length: parseInt(document.getElementById("length").value, 10),
        includeLowercase: document.getElementById("includeLowercase").checked,
        includeUppercase: document.getElementById("includeUppercase").checked,
        includeDigits: document.getElementById("includeDigits").checked,
        includeSymbols: document.getElementById("includeSymbols").checked,
        includeSpace: document.getElementById("includeSpace").checked,
        includeGreek: document.getElementById("includeGreek").checked,
        includeCyrillic: document.getElementById("includeCyrillic").checked,
        includeArabic: document.getElementById("includeArabic").checked,
        includeDevanagari: document.getElementById("includeDevanagari").checked,
        includeJapaneseKana: document.getElementById("includeJapaneseKana").checked,
        excludeAmbiguous: document.getElementById("excludeAmbiguous").checked,
        requireEachSelectedSet: document.getElementById("requireEachSelectedSet").checked
    };

    await callApi("/api/password/generate", payload, true);
}

async function analyzePassword() {
    message.textContent = "";

    const password = analyzeInput.value;
    if (!password.trim()) {
        message.textContent = "Enter a password to analyze.";
        return;
    }

    await callApi("/api/password/analyze", { password }, false);
}

async function callApi(url, payload, writeGeneratedPassword) {
    try {
        const response = await fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || "Request failed.");
        }

        const data = await response.json();

        if (writeGeneratedPassword) {
            passwordOutput.value = data.password;
        }

        renderResults(data);
    } catch (error) {
        message.textContent = error.message;
    }
}

function renderResults(data) {
    strengthText.textContent = data.strengthLevel;
    entropyBits.textContent = `${data.entropyBits} bits`;
    poolSize.textContent = `${data.poolSize}`;
    selectedSets.textContent = Array.isArray(data.selectedSets) ? data.selectedSets.join(", ") : "-";
    crackOnline.textContent = data.crackTimeOnline;
    crackOfflineFast.textContent = data.crackTimeOfflineFast;
    crackOfflineSlow.textContent = data.crackTimeOfflineSlow;
    noteText.textContent = data.note || "-";
    updateStrengthMeter(data.strengthLevel, data.entropyBits);
}

function updateStrengthMeter(level, entropy) {
    let width = "18%";
    let color = "#e74c3c";

    if (level === "Moderate") {
        width = "42%";
        color = "#f39c12";
    } else if (level === "Strong") {
        width = "72%";
        color = "#2f80ed";
    } else if (level === "Very Strong") {
        width = "100%";
        color = "#2ecc71";
    }

    if (entropy < 40) {
        width = "18%";
        color = "#e74c3c";
    }

    strengthFill.style.width = width;
    strengthFill.style.background = color;
}
