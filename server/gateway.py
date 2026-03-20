import struct
import socket
import scapy
import time
import socketserver
from fcntl import ioctl

syn = b'E\x00\x00,\x00\x01\x00\x00@\x06\x00\xc4\xc0\x00\x02\x02"\xc2\x95Cx\x0c\x00P\xf4p\x98\x8b\x00\x00\x00\x00`\x02\xff\xff\x18\xc6\x00\x00\x02\x04\x05\xb4'

allcondata = {}


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
            

        allcondata.pop(self.request.getpeername())
            

        # self.request is the TCP socket connected to the client
       """
       print("opening tunnel")
        print("writing tunnel")
        self.data = self.request.recv(1024)
        print(self.data[-1])
        if self.data[-1] == 10:
            self.data = self.data[:-1]
        print(self.data.hex(sep=":"))
        tun.send(self.data)
        print("reading tunnel")
        reply = tun.recv(1024)
        print("done reading?")

        print(f"Received from {self.client_address[0]}:")
        print(self.data.decode("utf-8"))
        # just send back the same data, but upper-cased
 #       self.request.sendall(reply)
        # after we return, the socket will be closed.
    """

def pkt_callback(pkt):
    for userconnection in allcondata.copy():
        for sentpackettup in userconnection[1]:
            if pkt.answers(sendpacket[0]):
                sentpackettup = (sentpackettup[0], time.time())
                iplayer = pkt.getlayer(scapy.layers.inet.IP)
                packetbytes = bytes(iplayer)
                userconnection[0].send(len)

if __name__ == "__main__":
    HOST, PORT = "172.17.0.4", 9999
    scapy.sendrecv.sniff(iface:"eth0", prn=pkt_callback, store=0)
    # Create the server, binding to localhost on port 9999
    with socketserver.TCPServer((HOST, PORT), MyTCPHandler) as server:
        # Activate the server; this will keep running until you
        # interrupt the program with Ctrl-C
        server.serve_forever()
