# VodafoneFirewallDisabler
automatically disables the firewall forever in current Vodafone Routers
- supports Windows and Linux
- will check every 20 minutes if firewall is enabled and disable it

## Prerequisites
- Chrome Version 101 installed on machine
- Java Runtime 8 or higher

## How to run
- Download the file *vdf.jar*
- in a terminal run
```
java -jar vdf.jar http://192.168.0.1 user password
```
where http://192.168.0.1 is the URL to your router
where user/password are the credentials to login to your router


