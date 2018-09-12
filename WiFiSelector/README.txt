
Rebecca Van Dyke
rvandyke@u.rochester.edu
CSC 214 Project 3
TA: Julian Weiss

I affirm that I did not give or receive any unauthorized help on this exam, and that all work is my own.

Note: I got an extension on this project to today (Sunday, 5/7) because my app has to be tested on a real device and it turns out you can really screw up your device if you don't know what you're doing with the WifiManager. I have removed all ability to change Wifi state using my app, which unfortunately means that I'm not able to deliver full functionality as per my proposal, but also means that it is quite safe to install on any android device. If a device is not connected to any wifi network at startup, my app nicely asks them to turn it on and launches the OS wifi management application.

I had changing networks based on AP working briefly but not terribly reliably, so I've removed that as well. Based on the UR Wifi networks' two stage authentication process, switching connections is much more complicated than I originally thought. I've left the bulk of this code in my application, but disabled the buttons that would use it. In its current state, it's more of an academic tool so that you can see and track which access points your device is using.

Due to the nature of my app, most behavior had to be inside an activity, but a large amount of scanning logic and all of the code used to define and store access points is in my "model" and "database" packages. My database schema is essentially just the fields that are used when referencing favorited access points across launches, i.e. nickname BSSID and notes but not signal strength. Classes used for handling SoundPool are in the sound package. I have two activities that display lists, one of all access points seen by the wifi hardware (with different views for favorited access points) and one that lists all favorited access points. From each of these, an access point can be added to favorites/edited if it is already favorited in a dialogfragment. I also have a help activity that gives some background on how wifi works and how to use the app. This page is still written as if everything worked as I'd originally intended. I also have an activity that allows the user to perform a task(downloading a predetermined image) and see how long it takes as a metric to evaluate their wifi performance. Finally, I have two activities that I used just to fulfill advanced requirements, "Pointless Map" and "Pointless Dog" in the menu options.

Advanced Requirements: Network connectivity(speed test), WifiManager, Google Maps, SoundPool, MediaPlayer

RUN INSTRUCTIONS
My app must be run on a real device operating on API 17 or higher. I'm including a .apk file in my top level project directory.
I have also included sample output for your perusal, including a video from when I had AP-switching working.