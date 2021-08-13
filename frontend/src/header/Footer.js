import {Container, List, Segment} from "semantic-ui-react";

const Footer = ({onLogout, loggedInPlayer}) => {

  return (
      <Segment inverted vertical style={{ margin: '1em 0em 0em', padding: '1em 0em' }}>
          <Container textAlign='center'>
              <List horizontal inverted divided link size='small'>
                  <List.Item as='a' href='https://discord.gg/Bq6p7yJHKd' target='_blank'>
                      Created by the Disczord community
                  </List.Item>
                  {loggedInPlayer &&
                  <List.Item as='a' onClick={() => onLogout()}>
                      Log out
                  </List.Item>
                  }
              </List>
          </Container>
      </Segment>
  );
};

export default Footer;