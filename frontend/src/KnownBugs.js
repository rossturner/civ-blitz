import {Container, Header} from "semantic-ui-react"

const KnownBugs = () => {
    
    return (
        <Container>
            <Container textAlign='center' style={{marginBottom: '1em'}}>
                <Header as='h2'>Known Bugs</Header>
            </Container>
            <Container text>
                <p>
                    As this mod generator continues to be in development, there are bound to be bugs and issues. 
                    Please report any bugs you might find to the bug reporting channel in
                    <a href='https://discord.gg/Bq6p7yJHKd' target='_blank' rel="noreferrer"> the discord server</a>
                </p>
                <p>
                    Below we've compiled a list of known bugs
                </p>
                <ul>
                    <li>Greater Wall does not provide science adjacency</li>
                    <li>Cannot use Georgia civ ability with Dramatic Ages mode</li>
                </ul>
            </Container>
        </Container>
    );
};

export default KnownBugs;