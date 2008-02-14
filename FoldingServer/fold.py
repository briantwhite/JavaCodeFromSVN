import mpi, sys, os, string

if mpi.rank == 0:
	sequences = sys.argv[1:]
else:
	sequences = []

local_sequences = mpi.scatter(sequences)
result = []
for sequence in local_sequences:
	cmd = "java -Djava.awt.headless=true -cp FoldingServer.jar foldingServer.FoldingServer -c " + sequence
	for line in os.popen(cmd).readlines():
		if line.find(":") >= 0: 
			message = str(mpi.rank) + ";" + sequence +  ";" + line.rstrip("\n")
			result.append(message)

all = mpi.gather(result)

if mpi.rank == 0:
	for result in all:
		print result

