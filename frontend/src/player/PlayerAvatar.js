import {Image} from "semantic-ui-react";
import React from "react";


const PlayerAvatar = ({loggedInPlayer, size}) => {
    if (!loggedInPlayer || !loggedInPlayer.discordAvatar) {
        return null;
    } else {
        return <Image avatar size={ size ? size : 'medium'} src={'https://cdn.discordapp.com/avatars/'+loggedInPlayer.discordId+'/'+loggedInPlayer.discordAvatar+'.png'} />;
    }
}


export default PlayerAvatar;