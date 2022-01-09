ARCHIVE=submission.tar.gz

tar -czf $ARCHIVE  Client/src Client/include/ Client/bin/.gitkeep Client/makefile Server/src/ Server/pom.xml readme.txt
tar -tvf $ARCHIVE

