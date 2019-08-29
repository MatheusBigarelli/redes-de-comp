import socket
from threading import Thread

class Server():

    def __init__(self, value):
        super().__init__()
        self.value = value

    def run(self):
        print(self.value)


if __name__ == "__main__":
    serv = Server()

    serv.start()