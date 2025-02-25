all: clean executable valgrind
no-valgrind: clean executable execute

executable: list.o aux_functions.o functions.o main.o
	gcc -Wall -o p3 list.o aux_functions.o functions.o main.o

%.o: %.c
	gcc -Wall -Wno-stringop-overflow -c -g $<

execute:
	./main

valgrind:
	mkdir valgrind
	valgrind --leak-check=full --show-leak-kinds=all --track-origins=yes --verbose ./main

clean:
	rm -f *.o *.txt main
	if [ -d "valgrind" ]; then rm -r valgrind; fi
