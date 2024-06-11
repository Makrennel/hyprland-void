## Hyprland for Void Linux

This repository contains template files for building [Hyprland](https://github.com/hyprwm/Hyprland) using xbps-src.

### Installation

The simplest way to install Hyprland on Void Linux is using the [release tarballs](https://github.com/Makrennel/hyprland-void/releases) which are built automatically in a container whenever a commit is pushed to this repository. Download the [latest release](https://github.com/Makrennel/hyprland-void/releases/latest) and untar it in a directory.

```
mkdir ~/.cache/hyprland-void/
cd ~/.cache/hyprland-void

# Replace the url with the latest tarball download's url
curl -L https://github.com/Makrennel/hyprland-void/releases/download/5/hyprland-2024-06-11.tar.gz > ./hyprland-void.tar.gz
tar -xvf hyprland-void.tar.gz -C ./packages
```

Install packages like so:

```
sudo xbps-install -R ~/.cache/hyprland-void/packages hyprland
```

When updating simply use the -u flag to update all currently installed packages:

```
sudo xbps-install -R ~/.cache/hyprland-void/packages -u
```

### Running

In order to run Hyprland you will need to install some additional packages which will depend on your setup, for example a [session and seat manager](https://docs.voidlinux.org/config/session-management.html) and [graphics drivers](https://docs.voidlinux.org/config/graphical-session/graphics-drivers/index.html).

#### Note for Nvidia Users

The `hyprland-nvidia` has been removed as it is no longer necessary as of [version 0.33.0](https://github.com/hyprwm/Hyprland/releases/tag/v0.33.0). Nvidia support is still unofficial; refer to the [manual](https://wiki.hyprland.org/hyprland-wiki/pages/Nvidia/), and bearing this in mind.

### Extra
There are packages in this repository which may be of interest for:

- hypridle
- hyprlock
- hyprpaper
- xdg-desktop-portal-hyprland


### Manually Building

1) You may want to start by making a directory where you can keep the relevant repositories

```
mkdir ~/repos
cd ~/repos
```

2) Set up a [void-packages](https://github.com/void-linux/void-packages) clone for building templates files

```
git clone https://github.com/void-linux/void-packages
cd void-packages
./xbps-src binary-bootstrap
cd ..
```

3) Clone this repository:

```
git clone https://github.com/Makrennel/hyprland-void.git
cd hyprland-void
```

4) Append shared libraries to the end of your void-packages shared libraries

```
cat common/shlibs >> ../void-packages/common/shlibs
```

5) Copy srcpkgs to your void-packages srcpkgs directory

```
cp -r srcpkgs/* ../void-packages/srcpkgs
```

6) Build and install packages

```
cd ../void-packages
./xbps-src pkg hyprland
sudo xbps-install -R hostdir/binpkgs hyprland
```

