#!/bin/bash
# One-time bootstrap of the Let's Encrypt certificate for home4paws.in.
# Run this ONCE on the server (after DNS points here and ports 80/443 are open):
#   bash init-letsencrypt.sh
set -e

domains=(home4paws.in www.home4paws.in)
email="chiraggupta0963@gmail.com"   # Let's Encrypt renewal-notice email
staging=0                        # set to 1 first to test (avoids Let's Encrypt rate limits)
data_path="./certbot"
cert_path="/etc/letsencrypt/live/home4paws.in"

mkdir -p "$data_path/conf/live/home4paws.in" "$data_path/www"

echo "### 1. Creating a temporary dummy certificate so nginx can start ..."
docker compose run --rm --entrypoint "\
  openssl req -x509 -nodes -newkey rsa:2048 -days 1 \
    -keyout '$cert_path/privkey.pem' -out '$cert_path/fullchain.pem' -subj '/CN=localhost'" certbot

echo "### 2. Starting nginx ..."
docker compose up --force-recreate -d nginx

echo "### 3. Removing the dummy certificate ..."
docker compose run --rm --entrypoint "\
  rm -Rf /etc/letsencrypt/live/home4paws.in && \
  rm -Rf /etc/letsencrypt/archive/home4paws.in && \
  rm -Rf /etc/letsencrypt/renewal/home4paws.in.conf" certbot

echo "### 4. Requesting the real Let's Encrypt certificate ..."
domain_args=""
for d in "${domains[@]}"; do domain_args="$domain_args -d $d"; done
staging_arg=""; [ "$staging" != "0" ] && staging_arg="--staging"

docker compose run --rm --entrypoint "\
  certbot certonly --webroot -w /var/www/certbot \
    $staging_arg $domain_args \
    --email $email --rsa-key-size 4096 --agree-tos --no-eff-email --force-renewal" certbot

echo "### 5. Reloading nginx with the real certificate ..."
docker compose exec nginx nginx -s reload

echo "### Done — https://home4paws.in should now be live and secure."
