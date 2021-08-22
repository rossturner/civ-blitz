import {Button} from "semantic-ui-react";


const DownloadMatchModButton = ({match}) => {


    return (
        <Button as='a' href={'/api/mods/matches/'+match.matchId}>Download mod pack</Button>
    )
}

export default DownloadMatchModButton;