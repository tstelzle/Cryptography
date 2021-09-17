import traceback

import Server

TCP_IP = '192.168.178.60'
TCP_PORT = 3333

if __name__ == "__main__":
    server = Server.Server(TCP_IP, TCP_PORT)
    try:
        server.run()
    except (Exception, KeyboardInterrupt):
        traceback.print_exc()
        server.close()

