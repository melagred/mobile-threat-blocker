import struct
import socket
import scapy
import time
import socketserver
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
        allcondata[self.request.getpeername()] = (self.request, [])
        while True:
            datalength = self.request.recv(1, socket.MSG_DONTWAIT)
            if datalength > 0:
                data = self.request.recv(datalength)
                pkt = scapy.packet.Raw(data)
                allcondata[self.request.getpeername()].push((pkt, time.time()))
                scapy.sendrecv.send(pkt)
            
    def finish(self):
        allcondata.pop(self.request.getpeername())
        return super().finish()
            


def pkt_callback(pkt):
    for connectionPeerName in allcondata.keys():
        connectionSocket = allcondata[connectionPeerName][0]
        connectionData = allcondata[connectionPeerName][1]
        for sentpacketIndex in range(len(connectionData)):
            sentpacket = connectionData[sentpacketIndex]
            if pkt.answers(sentpacket):
                connectionData[sentpacketIndex] = (sentpacket, time.time())
                ipLayer = pkt.getlayer(scapy.layers.inet.IP)
                packetBytes = bytes(ipLayer)
                lenAsBytes = len(packetBytes).to_bytes(2)
                connectionSocket.send(lenAsBytes + packetBytes)

if __name__ == "__main__":
    HOST, PORT = "172.17.0.4", 9999
    scapy.sendrecv.sniff(iface="eth0", prn=pkt_callback, store=0)
    # Create the server, binding to eth0 (172.17.0.4) on port 9999
    with socketserver.TCPServer((HOST, PORT), MyTCPHandler) as server:
        # Activate the server; this will keep running until you
        # interrupt the program with Ctrl-C
        server.serve_forever()
