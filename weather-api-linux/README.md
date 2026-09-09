# Linux Weather VM API

This service is the weather VM used by the Book Catalog app. It listens on port `8081` and exposes:

- `GET /health`
- `GET /api/weather`

The catalog app expects `/api/weather` to return JSON with `city`, `temperature`, `description`, `humidity`, `windSpeed`, and `icon` fields.

## Run on the Linux VM

```bash
sudo mkdir -p /opt/weather-api
sudo cp weather_api.py /opt/weather-api/
sudo chmod +x /opt/weather-api/weather_api.py
sudo useradd --system --no-create-home --shell /usr/sbin/nologin weatherapi || true
sudo tee /etc/weather-api.env >/dev/null <<'EOF'
OPENWEATHER_API_KEY=put-your-openweather-key-here
WEATHER_CITY=New York
EOF
sudo chmod 600 /etc/weather-api.env
sudo python3 /opt/weather-api/weather_api.py
```

Test locally on the VM:

```bash
curl http://127.0.0.1:8081/health
curl http://127.0.0.1:8081/api/weather
```

## Run with systemd

```bash
sudo cp weather-api.service /etc/systemd/system/weather-api.service
sudo systemctl daemon-reload
sudo systemctl enable --now weather-api
sudo systemctl status weather-api
```

## Azure networking

Create an inbound NSG rule allowing TCP `8081` from the catalog VM's private IP or subnet. Do not open port `8081` to the entire Internet. Also allow the Linux VM outbound HTTPS (`443`) so it can call OpenWeatherMap.

## Connect from the catalog VM

Open the catalog weather page and enter the Linux VM private IP, for example:

```text
10.0.1.4
```

The catalog app then calls:

```text
http://10.0.1.4:8081/api/weather
```

Set a different port in the catalog app with `weather.vm.port` in `application.properties`.
