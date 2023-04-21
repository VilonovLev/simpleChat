import socket
import threading

ipaddress = '192.168.0.188'
port = 55555

# Choosing Nickname
nickname = input("Choose your nickname: ")

# Connecting To Server
client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect((ipaddress, port))

# Listening to Server and Sending Nickname
def receive():
    while True:
        try:
            # Receive Message From Server
            # If 'NICK' Send Nickname
            message = client.recv(1024).strip().decode('ascii')
            if message == 'NICK':
                client.send((nickname + '\n').encode('ascii'))
            else:
                print(message)
        except:
            # Close Connection When Error
            print("An error occured!")
            client.close()
            break


def write():
    while True:
        msg = input('')
        client.send((msg + '\n').encode('ascii'))


# Starting Threads For Listening And Writing
receive_thread = threading.Thread(target=receive)
receive_thread.start()

write_thread = threading.Thread(target=write)
write_thread.start()
