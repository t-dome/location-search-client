The server pre.dev.goeuro.de didn't respond at port 12345:

curl http://pre.dev.goeuro.de:12345/api/v1/suggest/position/en/name/Berlin
curl: (7) Failed connect to pre.dev.goeuro.de:12345; Connection refused

and at port 80 responds with 401 - Authorization required:

curl http://pre.dev.goeuro.de:80/api/v1/suggest/position/en/name/Berlin
<html>
<head><title>401 Authorization Required</title></head>
<body bgcolor="white">
<center><h1>401 Authorization Required</h1></center>
<hr><center>nginx</center>
</body>
</html>


I added a server (https://github.com/t-dome/location-search-server.git) which runs under Tomcat and returns mock data.