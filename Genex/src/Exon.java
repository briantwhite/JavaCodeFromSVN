// these are the exons generated by the process() method of Gene// they contain this exons start and end as well as the start of the next exonpublic class Exon {    int exonStart;    int exonEnd;    int startOfNextExon; // if this is -1, it is the last exon.        public Exon(int starting, int ending, int next) {        exonStart = starting;        exonEnd = ending;        startOfNextExon = next;    }        public int getStart() {        return exonStart;    }        public int getEnd() {        return exonEnd;    }        public int getStartOfNext() {        return startOfNextExon;    }}