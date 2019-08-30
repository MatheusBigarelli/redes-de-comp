from .http import HTTPRequest
import socket

class WebServer():
    # Index of connection in client tuple
    CONNECTION = 0
    # Index of client address in client tuple
    ADDRESS = 1

    def __init__(self, port):
        super().__init__()
        self.host = ''
        self.port = port
        self.clients = []


    def handle_connection(self, client):
        request = client[WebServer.CONNECTION].recv(1024).decode()
        http_request = HTTPRequest(request)
        http_request.parse()
        response = http_request.buildResponse()
        client[WebServer.CONNECTION].send(response)
        client[WebServer.CONNECTION].close()
    
    def start(self):
        try:
            self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            self.socket.bind((self.host, self.port))
            self.socket.listen(5)

            connection, address = self.socket.accept()
            client = (connection, address)
            self.clients.append(client)
            self.handle_connection(client)
            

        except KeyboardInterrupt:
            print('Server stopping...')
            if (len(self.clients)):
                for client in self.clients:
                    client[WebServer.CONNECTION].close()
            if (self.socket):
                self.socket.close()

# if __name__ == "__main__":
#     port = 4444
#     serv = WebServer(port)

#     serv.start()