import {Image} from "semantic-ui-react";
import React from "react";


const PlayerAvatar = ({player, size}) => {
    if (!player || !player.discordAvatar) {
        return null;
    } else {
        return <Image avatar size={ size ? size : 'medium'} src={'https://cdn.discordapp.com/avatars/'+
        (player.discordId || player.playerId)+'/'+player.discordAvatar+'.png'} />;
    }
}


export default PlayerAvatar;