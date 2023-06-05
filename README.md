## Hyprland for Void Linux

This repository contains template files for building [Hyprland](https://github.com/hyprwm/Hyprland) using xbps-src.

### Usage

Follow the instructions at [void-packages](https://github.com/void-linux/void-packages) to set up xbps-src for building templates.

Clone this repository:

```
$ git clone https://github.com/Makrennel/hyprland-void.git
$ cd hyprland-void
```

Append shlibs to the end of your void-packages shlibs

```
$ cat common/shlibs >> ~/void-packages/common/shlibs
```

Copy srcpkgs to your void-packages srcpkgs directory

```
$ cp -r srcpkgs/* ~/void-packages/srcpkgs/
```

Build and install packages
```
$ cd ~/void-packages
$ ./xbps-src pkg hyprland
$ sudo xbps-install -R hostdir/binpkgs hyprland
```
