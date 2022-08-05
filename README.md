# JavaRemoteDesktop
## Description
JavaRemoteDesktop is a remote Desktop application that streams the desktop to a connected client.

## How it works
JavaRemoteDesktop uses JNA( Java Native Access) to access the Native Windows Apis to get an image of the desktop. This image is then sent to the client who is connected
on a web server that is running on Spring
