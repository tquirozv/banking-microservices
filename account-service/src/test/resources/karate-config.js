function fn() {
  var port = karate.properties['karate.server.port'];
  karate.log('Using port:', port);
  if (!port) {
      throw new Error('server port not set');
  }

  var config = {
    baseUrl: 'http://localhost:' + port
  };

  karate.log('Using baseUrl:', config.baseUrl);
  karate.configure('connectTimeout', 5000);
  karate.configure('readTimeout', 5000);
  karate.configure('ssl', true);

  return config;
}
