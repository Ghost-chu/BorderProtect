# BorderProtect

## What's This

In Vanilla by default, Player can use Nether Portal teleport to the another Portal that out of Border.  
The players that out of world border will receive damages and may accident dead out of border which mean -- nothing can
retrieve, your items and exps just gone.

This plugin will prevent players from teleporting to the another portal that out of border.

This plugin will listen the events listed below and teleport players to world spawn point once we detected player stuck
out of border:

* Nether Portal teleporting
* Player respawn out of world border
* Player join the server but the position already out of border
* Teleportation destination out of world border

Note: This plugin only check Survival mode and Adventure mode players, Creative and Spectator will be ignored because
they won't receive damages when out of border.

## Configuration

```yaml
message:
  warning-teleport: "&cYou're trying teleport outside of world boarder, that will cause you death immediately, the teleport has been canceled."
  warning-portal: "&cYou're trying going through portal but destination at out of world border, that will cause you death immediately, the teleport has been canceled."
  warning-join: "&cYou're trying to join the game but you're not inside of world border, that will cause you death immediately, You has been teleport to world spawn."
  warning-respawn: "&cYou're trying to respawn to a position out of world border, that will cause you death immediately, You has been teleport to world spawn."
management:
  teleport: true
  portal: true
  join: true
  respawn: true
  avoid-out-of-border-loaded-by-accident-chunk-save: true
```