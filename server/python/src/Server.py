import socket
import traceback

import Cryptography

BUFFER_SIZE = 20  # Normally 1024, but we want fast response


class Server:

    def __init__(self, ip: str, port: str):
        self.server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.server.bind((ip, port))
        self.server.listen(1)

        self.conn, addr = self.server.accept()
        print("Server Listening ...")

    def accept_new_connection(self):
        while self.conn is None:
            self.conn, addr = self.server.accept()

    def close(self):
        self.server.close()

    def send_message(self, message: str):
        message += "\r\n"
        byte_message = message.encode()
        self.conn.send(byte_message)

    def run(self):
        cryptography = Cryptography.Cryptography()
        try:
            while True:
                data = self.conn.recv(BUFFER_SIZE)
                str_data = data.decode().rstrip().strip()
                if not str_data:
                    self.conn = None
                    self.accept_new_connection()
                else:
                    print("Received:", str_data)
                    if "message" in str_data:
                        cypher = cryptography.create_cypher()
                        self.send_message(cypher)
                    elif "Result" in str_data:
                        message = str_data.split(" ", 1)[1]
                        answer = cryptography.check_scrambled_result(message)
                        self.send_message(answer)
                    elif "key" in str_data:
                        str_data = str_data.split(" ", 1)[1]
                        try:
                            int_data = int(str_data)
                        except (ValueError, TypeError):
                            self.send_message("Please Send A Valid Key")
                        answer = cryptography.check_caesar_result(int_data)
                        self.send_message(answer)
                    else:
                        self.close()
        except Exception:
            traceback.print_exc()
            self.close()
        self.close()
