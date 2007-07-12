#!/usr/bin/perl

print "content-type: text/html \n\n";

opendir DIR, "/Library/WebServer/Documents/VGLProblems";
@contents = readdir DIR;
close DIR;

sort @contents;

foreach $name (@contents) {
    if ((-d "/Library/WebServer/Documents/VGLProblems/$name")
        && ($name ne ".")
        && ($name ne "..")) {
        print "$name\n";
    }
}

exit 1;