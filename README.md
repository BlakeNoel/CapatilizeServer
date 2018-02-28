This is a project I did for my Parallel & Cloud Computing class and one I am particularly proud of.

What was provided was a .java file which serves the purpose of being run by the client. The client will enter a string, and it will then send it to a server. Once the server obtains the string and capatilizes it, the client will then obtain the result and display it to the user.

It was up to me to make a server which would utilize parallism to serve the jobs of multiple clients. The server maintains a queue up to a limit, in order to maintain client count. Furthermore, the server also maintains count of currently processed clients in order to shrink or expand workers based on need.

In order to use the code here, make sure to have Java fully installed and up to date as needed.

The client will only need to have CapitalizeClient.java to be compiled into a .class file, done so with the following command in the terminal (Please ensure you're in the correct directory): 

javac CapitalizeClient.java

To run the client, the user only needs to ensure there is a server running (described below), and run the following command:

java CapitalizeClient

Compiling the server is very similar, except you need a few more .java files, so instead make sure you use the following command within the terminal (and once again, in the correct directory):

javac *.java

To run the server, which is required before the client can operate correctly, use the following command:

java CapitalizeServer

Using the program couldn't be simpler: Make sure you have a valid network connection between yourself and the server (for simple execution just use localhost), and proceed to start the server followed by any amount of clients.

The valid commands are ADD,x,y SUB,x,y MUL,x,y DIV,x,y or KILL, where x and y can be any valid double. The commands are case sensitive.

An example of a valid command could be: DIV,89.7,90.4

An example of an invalid command could be: add,23.4,21.1
