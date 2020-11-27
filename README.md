# Patents
<img src="https://github.com/sergystepanov/patents/workflows/build/badge.svg" alt="build status"> [![codecov](https://codecov.io/gh/sergystepanov/patents/branch/master/graph/badge.svg?token=SfOPUKgxDg)](https://codecov.io/gh/sergystepanov/patents)

Requires:
- Java 14
- Chrome browser
- Nginx

Uses:
- Gradle 6
- Spring Boot 2
- JPA
- Flyway
- H2 (embedded)
- cdp4j
- jsoup
- Spock
- Apache POI
- JWT

### Configuration

Required configuration params (PARAM_PARAM):
- `app.security.token.key` -- a Base64-encoded HS512 key

### Run configuration
Linux

As a service:
`/etc/systemd/system/patents.service`:
```bash
[Unit]
Description=Patents managemant application
After=syslog.target

[Service]
User=[user]
Group=[group]
ExecStart=[path/to/executable.app.jar]
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
```

```
sudo systemctl start/stop/status patents
```


