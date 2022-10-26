find $1 > find-results.txt 
grep ".txt" find-results.txt > grep-results.txt 
wc technical/biomed/*.txt grep-results.txt 