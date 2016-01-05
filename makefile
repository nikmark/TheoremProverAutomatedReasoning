prepare:
	if [ ! -e build/ ]; then mkdir build/ ; fi
	rm -f build/*
	cp src/* build/
	cd build/ ; \
	java -jar ../javacc.jar ListCongruenceFormulaParser.jj ; \
	rm -f ListCongruenceFormulaParser.jj

compile:
	if [ ! -e bin/ ]; then mkdir bin/ ; fi
	rm -fr bin/*
	javac -d bin/ -Xlint build/*.java
	
clean:
	rm -fr build/ bin/ LCCSat.jar

build:	prepare compile

jar:	build
	cd bin/ ; \
	jar cmf ../MANIFEST.MF LCCSat.jar * ; \
	mv LCCSat.jar ../
	
