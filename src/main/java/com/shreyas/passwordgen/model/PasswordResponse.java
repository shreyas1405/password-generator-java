package com.shreyas.passwordgen.model;

import java.util.List;

public class PasswordResponse {
    private String password;
    private int length;
    private int poolSize;
    private double entropyBits;
    private String strengthLevel;
    private String crackTimeOnline;
    private String crackTimeOfflineFast;
    private String crackTimeOfflineSlow;
    private List<String> selectedSets;
    private String note;

    public PasswordResponse() {
    }

    public PasswordResponse(String password, int length, int poolSize, double entropyBits,
                            String strengthLevel, String crackTimeOnline,
                            String crackTimeOfflineFast, String crackTimeOfflineSlow,
                            List<String> selectedSets, String note) {
        this.password = password;
        this.length = length;
        this.poolSize = poolSize;
        this.entropyBits = entropyBits;
        this.strengthLevel = strengthLevel;
        this.crackTimeOnline = crackTimeOnline;
        this.crackTimeOfflineFast = crackTimeOfflineFast;
        this.crackTimeOfflineSlow = crackTimeOfflineSlow;
        this.selectedSets = selectedSets;
        this.note = note;
    }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getLength() { return length; }
    public void setLength(int length) { this.length = length; }

    public int getPoolSize() { return poolSize; }
    public void setPoolSize(int poolSize) { this.poolSize = poolSize; }

    public double getEntropyBits() { return entropyBits; }
    public void setEntropyBits(double entropyBits) { this.entropyBits = entropyBits; }

    public String getStrengthLevel() { return strengthLevel; }
    public void setStrengthLevel(String strengthLevel) { this.strengthLevel = strengthLevel; }

    public String getCrackTimeOnline() { return crackTimeOnline; }
    public void setCrackTimeOnline(String crackTimeOnline) { this.crackTimeOnline = crackTimeOnline; }

    public String getCrackTimeOfflineFast() { return crackTimeOfflineFast; }
    public void setCrackTimeOfflineFast(String crackTimeOfflineFast) { this.crackTimeOfflineFast = crackTimeOfflineFast; }

    public String getCrackTimeOfflineSlow() { return crackTimeOfflineSlow; }
    public void setCrackTimeOfflineSlow(String crackTimeOfflineSlow) { this.crackTimeOfflineSlow = crackTimeOfflineSlow; }

    public List<String> getSelectedSets() { return selectedSets; }
    public void setSelectedSets(List<String> selectedSets) { this.selectedSets = selectedSets; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
