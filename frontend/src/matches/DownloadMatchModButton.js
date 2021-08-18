import {Button} from "semantic-ui-react";
import {Link} from "react-router-dom";


const DownloadMatchModButton = ({match}) => {


    return (
        <Button as={Link} to={'/api/mods/matches/'+match.matchId}>Download mod pack</Button>
    )
}

export default DownloadMatchModButton;