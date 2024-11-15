# Cursed Kits

Cursed Kits is a paper plugin implementation of a kit system.
The plugin allows server administrators to create collection
of items (kits) that can then be redeemed by regular players.

### Features

- Kits can cost money.
- Kits can have cooldown.

### Commands

| Name               | Description              |
|--------------------|--------------------------|
| /kit <name>        | Redeem a kit.            |
| /kits              | View all kits available. |
| /createkit <name>  | Create a new kit.        |
| /deletekit <name>  | Delete a kit.            |
| /editkit <name>    | Edit a kit.              |
| /kitpreview <name> | Preview a kit.           |

### Permissions

| Name                | Description                     |
|---------------------|---------------------------------|
| kits.admin          | Grant all advanced permissions. |
| kits.command.<name> | Execute `/<name>`.              |
| kits.kit.<name>     | Redeem kit <name>.              |