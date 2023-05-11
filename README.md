Avalanche is a school project. It's a mobile application and a set of microservices, aiming to digitize ski passes with NFC.

# How it works

Basically you have one ticket per station.

When you purchase a ticket extension for a station, we append what we call a validity to your ticket.
Now there must be two actors to validate someone's ticket. One must be the station, the other one the client. 

Basically, the station wants to validate that the client has a valid ticket. 
This was done by implementing our own HostApduService so our client's phone can act as a Card. 
As we didn't have any NFC reader at the time we had to add a little button so we could switch to NFC Reader mode and intéract with our client by sending APDU Commands. 

This way, the station is able to send a "ChallengeId" to the client hover NFC, so they both could communicate hover a GRPC Stream using a shared Channel, which will feed them with validation step status in real time.

# Notes

The validation was actually pretty tricky and is super messy, but guess what, it works and I wanted to cover and explore Channels, Cap, RabbitMQ and the APDU protocol.
