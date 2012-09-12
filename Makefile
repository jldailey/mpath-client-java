CLASSPATH=.:lib/gson-2.2.2.jar

all: Makefile class/MpathDriver.class class/Request.class
	@(cd class && CLASSPATH=.:../lib/gson-2.2.2.jar java MpathDriver)

class/%.class: %.java Makefile
	mkdir -p class
	CLASSPATH="$(CLASSPATH)" javac -d class $<


clean:
	rm -f *.class
	rm -f class/*.class

.PHONY: all clean
