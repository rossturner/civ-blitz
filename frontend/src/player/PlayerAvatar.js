import {Image, Popup} from "semantic-ui-react";
import React from "react";


const PlayerAvatar = ({player, size, floated}) => {
    if (!player) {
        return null;
    } else {
        let avatarUrl = 'https://cdn.discordapp.com/avatars/'+(player.discordId || player.playerId)+'/'+player.discordAvatar+'.png';
        if (!player.discordAvatar) {
            avatarUrl = 'https://cdn.discordapp.com/embed/avatars/'+(parseInt(player.discordId) % 5)+'.png';
        }
        const image = <Image floated={floated} avatar size={ size ? size : 'medium'} src={avatarUrl} />;
        return <Popup trigger={image} content={player.discordUsername} basic />;
    }
}


export default PlayerAvatar;