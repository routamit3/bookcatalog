#!/usr/bin/env python3
import json
import os
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
from urllib.parse import urlencode
from urllib.request import urlopen

HOST = "0.0.0.0"
PORT = int(os.getenv("PORT", "8081"))
CITY = os.getenv("WEATHER_CITY", "New York")
OPENWEATHER_API_KEY = os.getenv("OPENWEATHER_API_KEY", "")
OPENWEATHER_URL = "https://api.openweathermap.org/data/2.5/weather"


def get_weather():
    if not OPENWEATHER_API_KEY:
        raise RuntimeError("OPENWEATHER_API_KEY is not configured")

    query = urlencode({
        "q": CITY,
        "appid": OPENWEATHER_API_KEY,
        "units": "metric",
    })
    with urlopen(f"{OPENWEATHER_URL}?{query}", timeout=10) as response:
        payload = json.load(response)

    return {
        "city": payload["name"],
        "temperature": payload["main"]["temp"],
        "description": payload["weather"][0]["main"],
        "humidity": payload["main"]["humidity"],
        "windSpeed": payload.get("wind", {}).get("speed", 0),
        "icon": payload["weather"][0].get("icon", "02d"),
    }


class WeatherHandler(BaseHTTPRequestHandler):
    def send_json(self, status, body):
        encoded = json.dumps(body).encode("utf-8")
        self.send_response(status)
        self.send_header("Content-Type", "application/json")
        self.send_header("Content-Length", str(len(encoded)))
        self.send_header("Access-Control-Allow-Origin", "*")
        self.end_headers()
        self.wfile.write(encoded)

    def do_GET(self):
        if self.path == "/health":
            self.send_json(200, {"status": "ok"})
            return
        if self.path != "/api/weather":
            self.send_json(404, {"error": "Not found"})
            return

        try:
            self.send_json(200, get_weather())
        except Exception as error:
            self.send_json(502, {"error": str(error)})

    def log_message(self, format_string, *args):
        print(f"{self.address_string()} - {format_string % args}")


if __name__ == "__main__":
    server = ThreadingHTTPServer((HOST, PORT), WeatherHandler)
    print(f"Weather API listening on http://{HOST}:{PORT}")
    server.serve_forever()
