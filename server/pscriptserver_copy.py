from flask import Flask, request
import csv

# flask web server
app = Flask(__name__)

# This stores our "bad domains" for fast searching
# Key: is the website domain
# Value: is the type of threat (e.g., "Malware")
blacklist = {}

def load_blacklist():
    # This reads the 'csv.txt' file and makes a blacklist so it knows whats banned
    global blacklist
    try:
        # opens the CSV file (has to be in the same folder as this script)
        with open('csv.txt', mode='r', encoding='utf-8') as f:
            # this csv.reader handles the commas and quotes in the file correctly
            reader = csv.reader(f)
            for row in reader:
                # row has enough columns
                if len(row) >= 7:
                    url = row[2]       # the full URL
                    status = row[3]    # 'online' or 'offline'?
                    threat = row[5]    # reason it's blocked

                    # only online sites
                    if status == "online":
                        # just the domain name from the URL
                        domain = url.split("//")[-1].split("/")[0]
                        blacklist[domain] = threat

        print(f"Success! Server loaded {len(blacklist)} malicious domains.")

    except FileNotFoundError:
        print("Error: 'csv.txt' was not found. Make sure it's in the same folder as this script.")
    except Exception as e:
        print(f"An error occurred while loading the CSV: {e}")

def quick_analysis(domain: str) -> str:
    if not domain:
        return "SAFE"

    d = domain.lower().strip()

    suspicious_score = 0
    reasons = []
    # Hard coded list for now, probably move to list file later
    phishing_keywords = [
        "login", "verify", "secure", "account",
        "support", "billing", "confirm", "update"
    ]

    suspicious_tlds = [".xyz", ".top", ".tk", ".gq", ".ml", ".cf"]

    has_keyword = any(word in d for word in phishing_keywords)

    # Suspicious TLD
    if any(d.endswith(tld) for tld in suspicious_tlds):
        suspicious_score += 1
        reasons.append("SUSPICIOUS_TLD")

    # Punycode (strong signal)
    if "xn--" in d:
        suspicious_score += 2
        reasons.append("PUNYCODE")

    # Hyphen-heavy domains
    if d.count("-") >= 2:
        suspicious_score += 1
        reasons.append("HYPHEN_HEAVY")

    # Random-looking domain
    first_part = d.split(".")[0]
    digit_count = sum(ch.isdigit() for ch in first_part)
    if len(first_part) >= 15 and digit_count >= 4:
        suspicious_score += 1
        reasons.append("HIGH_ENTROPY")

    # Keywords ONLY matter when paired with suspicious structure
    if has_keyword and "SUSPICIOUS_TLD" in reasons:
        suspicious_score += 2
        reasons.append("PHISHING_KEYWORD")

    if has_keyword and "HYPHEN_HEAVY" in reasons:
        suspicious_score += 1
        if "PHISHING_KEYWORD" not in reasons:
            reasons.append("PHISHING_KEYWORD")

    # remove duplicates
    unique_reasons = []
    for r in reasons:
        if r not in unique_reasons:
            unique_reasons.append(r)

    if suspicious_score >= 3:
        return "PHISHING:" + ",".join(unique_reasons)

    if suspicious_score >= 1:
        return "SUSPICIOUS:" + ",".join(unique_reasons)

    return "SAFE"

# this defines the address app will call
@app.route('/inspect', methods=['GET'])
def inspect():
    # checks if safe
    # domain name sent by the Android app
    domain = request.args.get('domain')

    if not domain:
        return "SAFE"

    # first check if this domain is in our list of bad websites
    threat_type = blacklist.get(domain)

    if threat_type:
        # tells the Android app exactly what kind of threat it is
        return threat_type  # returns e.g., "Malware" or "Phishing"

    # if not in the list then run the analysis to detect the threats instead
    return quick_analysis(domain)

# starts script
if __name__ == '__main__':
    # load all the bad sites into memory
    load_blacklist()

    # start the web server to listen for requests from our app
    # '0.0.0.0' means it listens for connections over the internet
    # port 5000 for testing(I opened 5000 on the server to test this because 80 wasnt working for me -alexander)
    app.run(host='0.0.0.0', port=5000)
