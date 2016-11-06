# CV Tracking
Java implementation of the opencv library.  Trying to learn by messing around, so this is what it is!

These java classes use opencv (http://opencv.org/).

The end goal of this project is to create an application that will be able to accurately measure the trajectory of a basketball shot.  I've experimented with motion tracking, color tracking, and a little bit of face detection (because why not?!).

## Object Tracking
This is what I'm really after.  The following tutorial (originally written in C++) *really* helped me understand how opencv worked:
* https://www.youtube.com/watch?v=X6rPdRZzgjg

Currently trying to blend two different methods of object tracking, color tracking and motion tracking.

## Face Detection
I'm currently looking at some face-detection example code that I found from this video:
* https://www.youtube.com/watch?v=7bpwJtQ65oY

This video was very helpful, especially with seeing how to use opencv in Java.

## To Do

* work on the ball class, actually tracking a ball (end end goal)
* add superclass for trackers (including thresholds, contours, objs, etc.)
