readme.txt:

1) how to run our code:
	compilation:
		(from the Client directory:)
		make
	
		(from the Server directory:)
		mvn clean
		mvn package
	
	1.1) command line for each server:
		(from the Server directory:)
		(to run TPC:)
		java -jar target/bgs_tpc.jar <port>
		
		(to run Reactor:)
		java -jar target/bgs_reactor.jar <num_threads> <port>
	
	1.2) Examples for each command:
		
		register name1 pass123 12-12-2000
		register name2 pass123 12-12-2001

		login name1 pass1 1

		logout

		follow 0 name1
		follow 1 name1

		post the user @name1 listens to me

		pm name1 war is bad

		logstat

		stat name1 name2 

		block name1
		
2) We stored the filtered set of word inside HashSet in the static class FilteredWordsWrapper, which is inside the PM_Message class (bgu.spl.net.impl.messages.FilteredWordsWrapper)