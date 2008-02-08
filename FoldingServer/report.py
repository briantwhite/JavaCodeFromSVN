import mpi, sys, os, string
message = "Hi there from node " + str(mpi.rank) + " "
cmd = "java -Djava.awt.headless=true -cp FoldingServer.jar foldingServer.FoldingServer -c " + sys.argv[1]
for line in os.popen(cmd).readlines():
	message = message + " " + line.rstrip('\n')
print message

