function fn() {
  var env = karate.env; // get java system property 'karate.env'
  var baseUrl = karate.properties['baseUrl'];

  if (!baseUrl) {
    baseUrl = 'http://localhost:8080'; // default if not set
  }

  var config = {
    baseUrl: baseUrl
  };

  karate.configure('connectTimeout', 5000);
  karate.configure('readTimeout', 5000);
  karate.configure('ssl', true);

  return config;
}
