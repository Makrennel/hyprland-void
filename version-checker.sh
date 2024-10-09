#!/bin/bash

template_dir="srcpkgs"

repos=(
	"aquamarine"
	"hyprland"
	"hyprpaper"
	"hyprlang"
	"hyprlock"
	"hypridle"
	"hyprutils"
	"hyprcursor"
	"hyprwayland-scanner"
	"xdg-desktop-portal-hyprland"
	"hyprland-protocols"
)

log() {
	local RED='\033[0;31m'
	local GREEN='\033[0;32m'
	local YELLOW='\033[0;33m'
	local BLUE='\033[0;34m'
	local NC='\033[0m'

	case "$1" in
	success)
		echo -e "${GREEN}$2${NC}"
		;;
	info)
		echo -e "${BLUE}$2${NC}"
		;;
	warning)
		echo -e "${YELLOW}$2${NC}"
		;;
	error)
		echo -e "${RED}$2${NC}"
		;;
	*)
		echo "$2"
		;;
	esac
}

fetch_version() {
	repo=$1
	# latest_tag=$(curl -s "https://api.github.com/repos/hyprwm/$repo/releases/latest" | grep -Po '"tag_name": "\K.*?(?=")')
	latest_tag=$(curl -s "https://github.com/hyprwm/$repo/releases" | grep -Po 'href="[^"]*/releases/tag/\K[^"]+' | head -n 1)

	echo $latest_tag
}

main() {
	# for repo in "${repos[@]}"; do
	# 	fetch_latest_version "$repo"
	# done

	for package_dir in "$template_dir"/xdg-desktop-portal-hyprland/ "$template_dir"/hypr*/; do
		pkgname=$(basename "$package_dir")

		if [[ "$pkgname" == "hyprland-devel" ]]; then
			continue
		fi

		template_file="${package_dir}template"

		if [[ ! -f "$template_file" ]]; then
			echo "Template file not found for $pkgname"

			continue
		fi

		current_version="v$(grep -Po '^version=\K[0-9.]+(?=$)' "$template_file")"
		latest_version=$(fetch_version "$pkgname")

		echo "$pkgname ===> $current_version"

		if [[ -z "$latest_version" ]]; then
			log error "$pkgname ===> Failed to fetch the latest version from GitHub\n"

			continue
		elif [[ "$current_version" == "$latest_version" ]]; then
			log success "${GREEN}$pkgname is up-to-date (Version: $current_version)\n"
		else
			log info "$pkgname: $current_version, but latest version is $latest_version\n"
		fi
	done
}

main
