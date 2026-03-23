import struct
import socket
from scapy.all import *
import time
import socketserver
import threading
import select
from fcntl import ioctl

syn = b'E\x00\x00,\x00\x01\x00\x00@\x06\x00\xc4\xc0\x00\x02\x02"\xc2\x95Cx\x0c\x00P\xf4p\x98\x8b\x00\x00\x00\x00`\x02\xff\xff\x18\xc6\x00\x00\x02\x04\x05\xb4'

allcondata = {}
#maps connection peer IPs to a tuple of from (socket, sentpackets), where sentpackets is an array of tuples of form (packet, timestamp)

class MyTCPHandler(socketserver.BaseRequestHandler):
    """
    The request handler class for our server.

    It is instantiated once per connection to the server, and must
    override the handle() method to implement communication to the
    client.
    """

    def handle(self):
        print("got b0")
        self.started = False
        for i in range(15):
            print("trying: " + str(self.request.getpeername()))
            try:
                hello = self.request.recv(2)
                print(hello)
                if hello == b'\x51\x29':
                    self.started = True
                    print("got b1")
                    self.isOn = True
                    self.peername = self.request.getpeername()
                    self.lasttime = time.time()
                    allcondata[self.peername] = (self, [])
                    print("Start of handle: ")
                    print("len:" + str(len(allcondata)))
                    print("keys: " + str(allcondata.keys()))
                    while self.isOn:
                        try:
                            print("i'm On!")
                            time.sleep(0.01)
                            #if time.time() > self.lasttime + 30:
                            #    break
                            prefixpeek = self.request.recv(2, socket.MSG_DONTWAIT | socket.MSG_PEEK)
                            if len(prefixpeek) == 0:
                                self.isOn = False
                                print("no peek close")
                                break
                            if len(prefixpeek) == 2:
                                self.lasttime = time.time()
                                print("len: " + str(len(prefixpeek)))
                                datalength = int.from_bytes(self.request.recv(2))
                                print("prepeek: " + str(prefixpeek))
                                if datalength > 0:
                                    print("message from client, sending")
                                    while True:
                                        try:
                                            #rint("r data, " + str(datalength) + " bytes")
                                            data = self.request.recv(datalength)
                                            print("packet length: " + str(len(data)))
                                            #print("packet data: " +str(data))
                                            allcondata[self.peername][1].append((Ether()/IP(data), time.time()))
                                            #rint("c data")
                                            pkt = Ether()/IP(data)
                                            #rint("changing src")
                                            pkt[IP].src = "172.17.0.4"
                                            #rint("updating records")
                                            #pkt[TCP].options = b''
                                            if pkt.haslayer(TCP):
                                                pkt[TCP].sport = 9999
                                                pkt[TCP].chksum = None
                                            #print("displaying packet")
                                            pkt[IP].chksum = None
                                            pkt.show()
                                            #print("sending packet")
                                            sendp(pkt)
                                            break
                                        except BlockingIOError:
                                            print("blocked on payload read")
                                            time.sleep(0.05)
                        except BlockingIOError:
                            print("blocked on main read while running")
                            time.sleep(0.05)
                        except ConnectionResetError:
                            print("connection reset while running")
                            return
                else:
                    print("failed test: " + str(self.request.getpeername()))
                    break
            except BlockingIOError:
                print("blocked on main read while testing")
                time.sleep(1)
            except ConnectionResetError:
                print("connection reset while testing")
                return


    def finish(self):
        print("closing socket")
        self.request.close()
        if self.started:
            allcondata.pop(self.peername)
        print("after pop: "
        + "len: " + str(len(allcondata))
        + ", keys: " + str(allcondata.keys()))
        return super().finish()
            


def pkt_callback(pkt):
    if pkt.haslayer(IP)  and (((pkt[IP].src == "139.182.58.144" or pkt[IP].dst == "139.182.58.144") or (pkt.haslayer(TCP) and (pkt[TCP].sport == 22 or pkt[TCP].dport == 22)) or (pkt[IP].src == "168.63.129.16" or pkt[IP].dst == "168.63.129.16"))):
        return
    print(pkt)
    if pkt.haslayer(DNS) or pkt.haslayer(TCP) and (pkt[TCP].dport == 80 or pkt[TCP].sport == 80 or pkt[TCP].dport == 9100):
        #pkt.show()
        #print(str(bytes(pkt)))
        pass
    for connectionPeerName in tuple(allcondata.keys()):   
        connectionRequestHandler = allcondata[connectionPeerName][0]
        connectionData = allcondata[connectionPeerName][1]
        for sentpacketIndex in range(len(connectionData)):
            sentpacket = connectionData[sentpacketIndex][0]
            print("this: " + str(pkt[IP].src) + ", want: " + str(sentpacket[IP].dst))
            if pkt.answers(sentpacket) or pkt[IP].src == sentpacket[IP].dst:
                    print("reply going to: " + str(connectionPeerName))
                    connectionData[sentpacketIndex] = (sentpacket, time.time())
                    pkt[IP].dst = sentpacket[IP].src
                    if pkt.haslayer(TCP):
                        pkt[TCP].dport = sentpacket[TCP].sport
                        pkt[TCP].chksum = None
                    ipLayer = pkt.getlayer(IP)
                    packetBytes = bytes(ipLayer)
                    #print("send body length: " + str(len(packetBytes)))
                    lenAsBytes = len(packetBytes).to_bytes(2)
                    #print("send length bytes: " + str(lenAsBytes))
                    #print("whole message: " + str(lenAsBytes + packetBytes))
                    connectionRequestHandler.lasttime = time.time()
                    try:
                        connectionRequestHandler.request.sendall(lenAsBytes + packetBytes)
                    except (BrokenPipeError, ConnectionResetError):
                        connectionRequestHandler.isOn = False
                        print("(Disconnected?)")
                    return

if __name__ == "__main__":
    load_layer("http")
    HOST, PORT = "172.17.0.4", 9999
    print("got 1")
    sniffer = AsyncSniffer(prn=pkt_callback, store=1, filter="ip")
    sniffer.start()
    while True:
        try:
            server = socketserver.ThreadingTCPServer((HOST, PORT), MyTCPHandler)
            print("got 3")
            # Activate the server; this will keep running until you
            # interrupt the program with Ctrl-C
            serverThread = threading.Thread(target=server.serve_forever)
            serverThread.daemon = True
            serverThread.start()
            break
        except OSError:
            print("socket busy")
            time.sleep(3)

 
    #dns_req = IP(dst='8.8.8.8') / UDP(dport=53)/ DNS(rd=1, qd=DNSQR(qname='139.182.58.144.in-addr.arpa', qtype="PTR"))
    #answer = sr1(dns_req, verbose=0)
    #print(answer[DNS].summary())

    #httpstr = "4500003430d44000400600008bb63a908efa44ce1789005081b00202000000008002ffff9a3500000204ffd80103030801010402"
    #httpbytes = bytes.fromhex(httpstr)
    #httpbytes = b'E\x00\x00<\xa4\x9e@\x00@\x06-\x7f\xac\x11\x00\x04\x8e\xfb-\x8e\xa4j\x00Pav\x1f=\x00\x00\x00\x00\xa0\x02\xfa\xf0h\xcd\x00\x00\x02\x04\x05\xb4\x04\x02\x08\n\xa7=\xc3\xf6\x00\x00\x00\x00\x01\x03\x03\n'
    httpbytes = b'\x124Vx\x9a\xbc|\xed\x8d\xb94>\x08\x00E\x00\x00(\x00\x01\x00\x00@\x06\xfa\x11\xac\x11\x00\x04\x8e\xfaE\xaeVV\x00PI\x146b\x00\x00\x00\x00P\x02 \x009\x08\x00\x00'
    #print(conf.route)
    #mypkt = Ether(httpbytes)
    #mypkt[IP].src = HOST
    #mypkt[IP].dst = "104.18.26.120"
    #mypkt[TCP].seq=random.randrange(2**32)
    #mypkt[TCP].sport = 9999
    #mypkt.show()
    

    #a = TCP_client.tcplink(HTTP, "example.com", 80)
    #a.send(HTTP() / HTTPRequest())
    #b = a.recv()
    #b.show()
    #retpkt = srp1(mypkt)
    #retpkt.show()
    
    print("got 2")
    # Create the server, binding to eth0 (172.17.0.4) on port 9999
    
    sniffer.join()
