package com.example.safetyfirst.threat;

import java.util.HashSet;
import java.util.Set;

public class ThreatDetector {

    public enum Decision {
        ALLOW,
        WARN,
        BLOCK
    }

    public enum Category {
        SAFE,
        PHISHING,
        MALWARE,
        SUSPICIOUS_TLD,
        MALFORMED,
        SUSPICIOUS_PATTERN
    }

    private final Set<String> blacklist;
    private final Set<String> suspiciousTlds;
    private final Set<String> phishingKeywords;

    public ThreatDetector() {
        blacklist = new HashSet<>();
        blacklist.add("knownbad.com");
        blacklist.add("phishing-test.xyz");
        blacklist.add("malware-site.top");

        suspiciousTlds = new HashSet<>();
        suspiciousTlds.add("xyz");
        suspiciousTlds.add("top");
        suspiciousTlds.add("tk");
        suspiciousTlds.add("gq");

        phishingKeywords = new HashSet<>();
        phishingKeywords.add("login");
        phishingKeywords.add("verify");
        phishingKeywords.add("secure");
        phishingKeywords.add("account");
        phishingKeywords.add("update");
        phishingKeywords.add("support");
        phishingKeywords.add("billing");
        phishingKeywords.add("confirm");
    }

    public Decision analyzeDomain(String domain) {
        Category category = getCategory(domain);

        if (category == Category.MALFORMED || category == Category.MALWARE) {
            return Decision.BLOCK;
        }

        if (category == Category.PHISHING || category == Category.SUSPICIOUS_PATTERN) {
            return Decision.BLOCK;
        }

        if (category == Category.SUSPICIOUS_TLD) {
            return Decision.WARN;
        }

        return Decision.ALLOW;
    }

    public Category getCategory(String domain) {
        if (domain == null || domain.isEmpty()) {
            return Category.MALFORMED;
        }

        domain = domain.toLowerCase().trim();

        if (blacklist.contains(domain)) {
            if (domain.contains("malware")) {
                return Category.MALWARE;
            }
            if (domain.contains("phishing")) {
                return Category.PHISHING;
            }
            return Category.SUSPICIOUS_PATTERN;
        }

        String[] parts = domain.split("\\.");

        if (parts.length < 2) {
            return Category.MALFORMED;
        }

        for (String part : parts) {
            if (part.isEmpty()) {
                return Category.MALFORMED;
            }

            if (part.startsWith("-") || part.endsWith("-")) {
                return Category.MALFORMED;
            }

            if (part.length() > 63) {
                return Category.MALFORMED;
            }
        }

        if (containsPhishingKeyword(domain)) {
            return Category.PHISHING;
        }

        String tld = parts[parts.length - 1];
        if (suspiciousTlds.contains(tld)) {
            return Category.SUSPICIOUS_TLD;
        }

        if (domain.contains("xn--")) {
            return Category.SUSPICIOUS_PATTERN;
        }

        if (countHyphens(domain) >= 2) {
            return Category.SUSPICIOUS_PATTERN;
        }

        for (String part : parts) {
            if (looksRandom(part)) {
                return Category.SUSPICIOUS_PATTERN;
            }
        }

        return Category.SAFE;
    }

    public String getCategoryLabel(String domain) {
        Category category = getCategory(domain);

        switch (category) {
            case PHISHING:
                return "Phishing";
            case MALWARE:
                return "Malware";
            case SUSPICIOUS_TLD:
                return "Suspicious TLD";
            case MALFORMED:
                return "Malformed Domain";
            case SUSPICIOUS_PATTERN:
                return "Suspicious Pattern";
            case SAFE:
            default:
                return "Safe";
        }
    }

    private boolean containsPhishingKeyword(String domain) {
        for (String keyword : phishingKeywords) {
            if (domain.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    private int countHyphens(String domain) {
        int count = 0;

        for (int i = 0; i < domain.length(); i++) {
            if (domain.charAt(i) == '-') {
                count++;
            }
        }

        return count;
    }

    private boolean looksRandom(String label) {
        if (label.length() < 15) {
            return false;
        }

        int digitCount = 0;

        for (int i = 0; i < label.length(); i++) {
            if (Character.isDigit(label.charAt(i))) {
                digitCount++;
            }
        }

        return digitCount >= 4;
    }
}
