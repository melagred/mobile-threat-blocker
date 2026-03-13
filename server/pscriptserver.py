from flask import Flask, request
import csv

#flask web server
app = Flask(__name__)

#This stores our "bad domains" for fast searching
#Key: is the website domain
#Value: is the type of threat (e.g., "Malware")
blacklist = {}

def load_blacklist():
    # This reads the 'csv.txt' file and makes a blacklist so it knows whats banned

    global blacklist
    try:
        #opens the CSV file (has to be in the same folder as this script)
        with open('csv.txt', mode='r', encoding='utf-8') as f:
            #this csv.reader handles the commas and quotes in the file correctly
            reader = csv.reader(f)
            for row in reader:
                #row has enough columns
                if len(row) >= 7:
                    url = row[2]    #the full URL
                    status = row[3] #'online' or 'offline'?
                    threat = row[5] #reason it's blocked

                    #only online sites
                    if status == "online":
                        #just the domain name from the URL

                        domain = url.split("//")[-1].split("/")[0]
                        blacklist[domain] = threat

        print(f"Success! Server loaded {len(blacklist)} malicious domains.")

    except FileNotFoundError:
        print("Error: 'csv.txt' was not found. Make sure it's in the same folder as this script.")
    except Exception as e:
        print(f"An error occurred while loading the CSV: {e}")

#this defines the address ap will call
@app.route('/inspect', methods=['GET'])
def inspect():
    #checks if safe
    #domain name sent by the Android app
    domain = request.args.get('domain')

    if not domain:
        return "SAFE"

    #check if this domain is in our list of bad websites
    threat_type = blacklist.get(domain)

    if threat_type:
        #tells the Android app exactly what kind of threat it is
        return threat_type #returns e.g., "Malware" or "Phishing"


    return "SAFE"

#starts of script
if __name__ == '__main__':
    #load all the bad sites into memory
    load_blacklist()

    #start the web server to listen for requests from our app
    #'0.0.0.0' means it listens for connections over the internet
    #'port=80' port for web traffic
    app.run(host='0.0.0.0', port=80)
