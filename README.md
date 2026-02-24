# CTA Tracker

Spring Boot service that sends push notifications when a CTA train is arriving near your location. Designed to receive
GPS coordinates from an iOS Shortcut and notify via [ntfy.sh](https://ntfy.sh).

## How it works

1. An iOS Shortcut POSTs your GPS location to `/location`
2. The app finds CTA stations within 400 meters
3. If a train arrives within 10 minutes, a push notification is sent via ntfy.sh

## Prerequisites

- [CTA Train Tracker API key](https://www.transitchicago.com/developers/traintracker.aspx)
- [ntfy.sh](https://ntfy.sh) topic (free, no account required)
- Docker

## Running with Docker

```bash
docker build -t cta-tracker .

docker run -p 8080:8080 \
  -e CTA_API_KEY=your-api-key \
  -e NTFY_TOPIC_URL=https://ntfy.sh/your-topic \
  cta-tracker
```

## Running locally

```bash
./gradlew bootRun
```

Configure `src/main/resources/application.yml` with your API key and ntfy topic URL before running.

## API

### POST /location

```json
{
  "lat": "41.884500",
  "lon": "-87.630997"
}
```

## Configuration

| Property                     | Default | Description                                |
|------------------------------|---------|--------------------------------------------|
| `cta.api.key`                | —       | CTA Train Tracker API key                  |
| `cta.proximity-meters`       | `400`   | Radius to search for nearby stations       |
| `cta.arrival-window-minutes` | `10`    | Alert if train arrives within this window  |
| `ntfy.topic-url`             | —       | ntfy.sh topic URL to send notifications to |
