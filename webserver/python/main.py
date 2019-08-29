from socket import socket, SOCK_STREAM, AF_INET
import threading


def buildResponse():
    string = ''
    string = appendHeader(string)
    string = appendPayload(string)
    
    return string.encode()


def appendHeader(string):

    with open('www/index.html') as html_file:
        l = len(html_file.read())
        html_file.close()

    string += "HTTP/1.0 200 OK\r\n"
    string += "Connection: close\r\n"
    string += "Server: Seu cu\r\n"
    string += 'Content-Length: ' + str(l) + '\r\n'
    string += 'Content-Type: text/html; charset=utf-8\r\n'
    string += 'Date: Mon, 18 Jul 2016 16:06:00 GMT\r\n'
    string += '\r\n'

    return string


def appendPayload(string):
    with open('www/index.html', 'r') as html_file:
        for line in html_file.readlines():
            string += line
        html_file.close()


    return string
    







    
HOST = ''
PORT = 4444

socket = socket(AF_INET, SOCK_STREAM)
origin = (HOST, PORT)

socket.bind(origin)
socket.listen(1)
while True:
    try:
        connection, client = socket.accept()
        print("\n")
        request = connection.recv(1024).decode()
        print(request.split("\r\n"))

        response = buildResponse()
        connection.send(response)
        connection.close()
        break

    except KeyboardInterrupt:
        print('Interrompido')

