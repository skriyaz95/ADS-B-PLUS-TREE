JCC = javac
JAVA = java
JFLAGS = -g

default: bplustree.class

bplustree.class: bplustree.java
	$(JCC) $(JFLAGS) bplustree.java

bplustree: bplustree.class
	$(JAVA) bplustree input.txt

clean:
	$(RM) *.class
