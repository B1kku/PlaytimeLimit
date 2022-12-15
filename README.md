# PlaytimeLimit
Paper 1.18.2 plugin that allows server owners to limit player playtime.

In case anyone stumbles upon this and wants to try it, made it using Paper, I'm unsure of Spigot compatibility as it uses Kyori's adventure lib. 
Beware as this plugin uses player names instead of UUIDs for identification, don't expect to work flawlessly if a player changes their name.

## Config usage: 

Ammount of time in seconds the player is able to play before a reset.

> playTimeLimitSeconds: 3600

Ammount of time in seconds before the plugin counts the playtime again, setting this too low causes higher load on the server, setting it too high is exploitable.

> updateTimeSeconds: 20

Ammount of time in seconds before the plugin saves all data to disk, this is important in case the server crashes or shutsdown.

> backupTimeSeconds: 40

Ammount of days in between limit resets, in theory 1 also works for a daily reset.

> resetTimeDays: 7

Hour of the day the server will reset at, this might happen a bit later depending on backupTime. 

> resetTimeHour: 0

## Commands: 

Forces a reset of all playtimes.

> resetTime

Add or reduce playtime for a specific user, doesn't need to be online for this to work, but name must be exact.

> playtime <add/reduce> user time

### Aditional Notes:

The playtimes.yml file is not the intended way to change a player's playtime, but if you do so, make sure to restart the server after or it will do nothing.
