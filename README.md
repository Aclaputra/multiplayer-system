# UDP System
Just testing some TCP and UDP system (only the udp in this repository), most likely used on multiplayer games rather than those 
Normal backend things, just use gRPC bruh, this is overkill for your services.

# tools
- Java Netty 4.1
- Java 1.8
- Java Swing (Game GUI)

# How to run
- Build the projects & the Jar
- Open 3 separated terminals
- First terminal run `java -jar <jarname>.jar server` as someone who host the server
- Second terminal run `java -jar <jarname>.jar client` as player 1
- Third terminal run `java -jar <jarname>.jar client` as player 2
- Write a Message on Second Terminal, see how It interacts each other to the Third terminal and the other way around.

# Notes
- UDP uses DatagramPacket while TCP use simpler way to send messages without packets.
- UDP only need one group Bootstrap while TCP need bossGroup and the workerGroup with ServerBootstrap the differences
between this two on Java Netty.
- GameLauncher able to run either Server or Client based on terminal Java Arguments. that's why we used Jars on terminal.