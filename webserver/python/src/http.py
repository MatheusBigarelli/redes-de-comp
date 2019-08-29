class HTTPRequest:
    CRLF = "\r\n"
    VERSION = "HTTP/1.0 "
    OK = "200 OK\r\n"
    NOT_FOUND = "404 Not found\r\n"
    MOVED = "304 Moved permanently\r\n"

    def __init__(self, request):
        self.request = request

    def parse(self, kwrdargs):
        lines = self.request.split(CRLF)
        