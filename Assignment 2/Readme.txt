Here i have implemented Group Chat application using Socket programming and Multi-threading concepts.

Here i have created two classes(Server & Client) and three threads(ClientThread,DataReadFromServerThread,DataWriteToServerThread).

To run each file, i have used following command in cmd after compiling all of them.

		-->  java -cp . ChatApp/Server 8888
		-->  java -cp . ChatApp/Client localhost 8888
		
	Here ChatApp is a package and Server, client are java class files containing main() method.
	"localhost" is local machine server name and "8888" is a port number.

Here when user joins the group chat, application displays the name of connected users.
So for first user it shows "No user connected" message. And for other users it displays the list of connected users.
This thing can be seen in the output.


Also i have attached screenshots of outputs for three users.
	