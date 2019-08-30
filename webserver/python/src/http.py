import time
import os


class HTTPRequest:
    CRLF = "\r\n"
    VERSION = "HTTP/1.0 "
    OK = "200 OK\r\n"
    NOT_FOUND = "404 Not found\r\n"
    MOVED = "304 Moved permanently\r\n"


    def __init__(self, request):
        self.request = request

    def parse(self, **kwargs):
        lines = self.request.split(HTTPRequest.CRLF)
        self.requested_file = lines[0].split(' ')[1]
        if self.requested_file == '/':
            self.requested_file = 'index'
        if not '.html' in self.requested_file:
            self.requested_file += '.html'

        for line in lines[1:]:
            if 'Connection' in line:
                self.conn_type = line
        
        self.date = time.strftime("%a, %d %b %Y %H:%M:%S %Z", time.gmtime()) + '\r\n'

        
        self.path = 'www/' + self.requested_file
    
    def buildResponse(self):
        string = ''
        string = self.appendHeader(string)
        string = self.appendPayload(string)
        
        return string.encode()


    def appendHeader(self, string):

        with open(self.path, 'r') as html_file:
            l = len(html_file.read())
            html_file.close()

        string += "HTTP/1.0 200 OK\r\n"
        string += "Connection: close\r\n"
        string += "Server: Seu cu\r\n"
        string += 'Content-Length: ' + str(l) + '\r\n'
        string += 'Content-Type: text/html; charset=utf-8\r\n'
        string += 'Date: ' + self.date
        string += '\r\n'

        return string


    def appendPayload(self, string):
        if (os.path.exists(self.path)):
            with open(self.path, 'r') as html_file:
                for line in html_file.readlines():
                    string += line
                html_file.close()

        else:
            string += "404 Not Found"

        return string