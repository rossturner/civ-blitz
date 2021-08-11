import {Container, List, Segment} from "semantic-ui-react";

const Footer = ({onLogout}) => {

  return (
      <Segment inverted vertical style={{ margin: '1em 0em 0em', padding: '1em 0em' }}>
          <Container textAlign='center'>
              <List horizontal inverted divided link size='small'>
                  <List.Item as='a' onClick={() => onLogout()}>
                      Log out
                  </List.Item>
              </List>
          </Container>
      </Segment>
  );
};

export default Footer;