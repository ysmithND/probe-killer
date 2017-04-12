# probe-killer
Hi!
So, I made this app specifically for Android tablets a couple of months ago because I wanted to learn more about the Android Lifecycle and also because I wanted to give people a way to understand what kind of wireless networks they were connecting to and how to make themselves harder targets.
Sure, your wireless carrier will let you know if/when the network is unencrypted, but that's about as far as their protection goes. This app is designed to:
1. Give you the stats on your current network connection--is it good/bad/ugly and will tell you if the network ever becomes "evil"
2. Lets you remove your old device connections. Attackers can use this information to stand up an access point of the same name, thereby encouraging your device to connect to it
3. Looks for known-bad and malicious Wi-Fi and will alert you if one is seen in the area--go ahead, be a hero in Starbucks
4. Gives you a device risk score so that way you can curtail some of those bad habits

There are plenty more enhancements that I had planned but never got to do including:
1. Allowing you to tag home & corporate networks as preferred. If these networks show up in a different location, you'd get a warning that someone may be trying to spoof your network
2. Better error handling and protection of battery power 
3. Tie-ins to the Pwnie Express Pulse API so that network owners could pre-emptively deny devices with a low risk score from privileged network access
4. Proactive probe removal for when you leave an area (say, an airport)
5. A Pulse news service
6. And in a galaxy far, far away: apps for iPhone, iPad and Mac/Windows/Linux desktop

There's still some clean up to do around the main body of work, which is why it's not on the Google Play Store yet, but hopefully I'll get to it in a couple of weekends. 

It's a wireless world. Be safe out there
