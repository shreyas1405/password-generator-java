package com.shreyas.passwordgen.model;

public class GenerateRequest {
    private int length = 256;

    private boolean includeLowercase = true;
    private boolean includeUppercase = true;
    private boolean includeDigits = true;
    private boolean includeSymbols = true;
    private boolean includeSpace = false;

    private boolean includeGreek = false;
    private boolean includeCyrillic = false;
    private boolean includeArabic = false;
    private boolean includeDevanagari = false;
    private boolean includeJapaneseKana = false;

    private boolean excludeAmbiguous = false;
    private boolean requireEachSelectedSet = true;

    public int getLength() { return length; }
    public void setLength(int length) { this.length = length; }

    public boolean isIncludeLowercase() { return includeLowercase; }
    public void setIncludeLowercase(boolean includeLowercase) { this.includeLowercase = includeLowercase; }

    public boolean isIncludeUppercase() { return includeUppercase; }
    public void setIncludeUppercase(boolean includeUppercase) { this.includeUppercase = includeUppercase; }

    public boolean isIncludeDigits() { return includeDigits; }
    public void setIncludeDigits(boolean includeDigits) { this.includeDigits = includeDigits; }

    public boolean isIncludeSymbols() { return includeSymbols; }
    public void setIncludeSymbols(boolean includeSymbols) { this.includeSymbols = includeSymbols; }

    public boolean isIncludeSpace() { return includeSpace; }
    public void setIncludeSpace(boolean includeSpace) { this.includeSpace = includeSpace; }

    public boolean isIncludeGreek() { return includeGreek; }
    public void setIncludeGreek(boolean includeGreek) { this.includeGreek = includeGreek; }

    public boolean isIncludeCyrillic() { return includeCyrillic; }
    public void setIncludeCyrillic(boolean includeCyrillic) { this.includeCyrillic = includeCyrillic; }

    public boolean isIncludeArabic() { return includeArabic; }
    public void setIncludeArabic(boolean includeArabic) { this.includeArabic = includeArabic; }

    public boolean isIncludeDevanagari() { return includeDevanagari; }
    public void setIncludeDevanagari(boolean includeDevanagari) { this.includeDevanagari = includeDevanagari; }

    public boolean isIncludeJapaneseKana() { return includeJapaneseKana; }
    public void setIncludeJapaneseKana(boolean includeJapaneseKana) { this.includeJapaneseKana = includeJapaneseKana; }

    public boolean isExcludeAmbiguous() { return excludeAmbiguous; }
    public void setExcludeAmbiguous(boolean excludeAmbiguous) { this.excludeAmbiguous = excludeAmbiguous; }

    public boolean isRequireEachSelectedSet() { return requireEachSelectedSet; }
    public void setRequireEachSelectedSet(boolean requireEachSelectedSet) { this.requireEachSelectedSet = requireEachSelectedSet; }
}
