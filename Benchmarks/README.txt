Author: Hafiz Ahmad Shahzad Aslam, a.shahzadaslam@gmail.com

--------------------------------------------------------------------------------
About this file
--------------------------------------------------------------------------------
 
This file pertains to KIARA_JAVA RMI test using maven. It gives brief introduction on how to run
RMI server and test it using maven features.

It consist of 3 different projects.

1. KIARARMIInterface: which acts as RMI interface and will help us at the time of creating stub.
2. KIARARMIServer: it is a server which implements KIARARMIInterface and creates stub. This project is
dependent upon KIARARMIInterface and also using junit framework as dependency.
3. KIARARMIClient: It is a RMI Client which is used to test the server by accessing through RMI interface
method.

--------------------------------------------------------------------------------
How to run
--------------------------------------------------------------------------------
1. From Command Line
First of all you need to run and install the KIARARMIInterface on your local repository.
which in case of Windows OS is HOME_DIRECTORY/.m2/repositories/
Go to the project directory ./KIARA_Java/KIARARMIInterface and inside this folder run

	mvn clean install (press enter)

maven will automatically install KIARARMIInterface on your local repository.

It will put the KIARARMIInterface1.0-SNAPSHOT.jar in the above mentioned directory (i.e. HOME_DIRECTORY/.m2/repositories/)

Then in 2nd step go to the project directory ./KIARA_Java/KIARARMIServer and run the command

	mvn clean test (press enter)

In response to the above command you will see something like this
	-------------------------------------------------------
	T E S T S
	-------------------------------------------------------
	Running cg.cs.kiararmiserver.RMIServerTest
	Apr 15, 2014 11:14:41 AM cg.cs.kiararmiserver.RMIServerTest setUpClass
	INFO: Setting up Server
	Server Started on port 9989.
	Apr 15, 2014 11:14:41 AM cg.cs.kiararmiserver.RMIServerTest testGetGreetings
	INFO: Connecting to server.
	Apr 15, 2014 11:14:41 AM cg.cs.kiararmiserver.RMIServerTest testGetGreetings
	INFO: Message received from server:
	Apr 15, 2014 11:14:41 AM cg.cs.kiararmiserver.RMIServerTest testGetGreetings
	INFO: Welcome Shahzad
	Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.259 sec

	Results :

	Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
	-----------------------------------------------------------

	which means you have successfully been able to run the project and the junit test cases.

2. From IDE
(i)		Import KIARARMIInterface project into your favourite IDE. and run the maven "clean and install" commands
from it.
(ii)	Import KIARARMIServer project into your favourite IDE and run the maven "clean and test" commands
from your IDE. It will run the junit test cases for you.
	"or" just run the file src/cg.cs.kiararmiserver.RMIServer.java and it will start the RMI server.
Then import the project KIARARMIClient into your IDE and run the src/cg.cs.kiararmiclient.RMIClient.java It will start
the client and send the message to the server and will display the result on IDE console.

--------------------------------------------------------------------------------
Important
--------------------------------------------------------------------------------

Here I have created and checked this project using NetBeans and IntelliJIDEA IDE's.
When you are using maven, configurations related to IDE's are not important. So you are free to use any IDE of your choice.
But please make sure don't commit any settings which are related to your specific development environment or IDE.