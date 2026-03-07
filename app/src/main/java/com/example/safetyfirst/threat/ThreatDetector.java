package com.example.safetyfirst.threat;

import java.util.HashSet;
import java.util.Set;

public class ThreatDetector {

    public enum Decision {
        ALLOW,
        WARN,
        BLOCK
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

        if (domain == null || domain.isEmpty()) {
            return Decision.BLOCK;
        }

        domain = domain.toLowerCase().trim();

        if (blacklist.contains(domain)) {
            return Decision.BLOCK;
        }

        String[] parts = domain.split("\\.");

        if (parts.length < 2) {
            return Decision.BLOCK;
        }

        int suspiciousCount = 0;

        for (String part : parts) {
            if (part.isEmpty()) {
                return Decision.BLOCK;
            }

            if (part.startsWith("-") || part.endsWith("-")) {
                return Decision.BLOCK;
            }

            if (part.length() > 63) {
                return Decision.BLOCK;
            }

            if (looksRandom(part)) {
                suspiciousCount++;
            }
        }

        String tld = parts[parts.length - 1];
        if (suspiciousTlds.contains(tld)) {
            suspiciousCount++;
        }

        if (domain.contains("xn--")) {
            suspiciousCount++;
        }

        if (containsPhishingKeyword(domain)) {
            suspiciousCount++;
        }

        if (countHyphens(domain) >= 2) {
            suspiciousCount++;
        }

        if (suspiciousCount >= 2) {
            return Decision.BLOCK;
        }

        if (suspiciousCount == 1) {
            return Decision.WARN;
        }

        return Decision.ALLOW;
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