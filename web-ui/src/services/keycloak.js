import Keycloak from 'keycloak-js';

const keycloak = new Keycloak({
  url: 'http://localhost:8180',
  realm: 'spacedrop',
  clientId: 'spacedrop-web',
});

export default keycloak;
