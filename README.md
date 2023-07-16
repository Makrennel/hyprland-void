## Hyprland for Void Linux

This repository contains template files for building [Hyprland](https://github.com/hyprwm/Hyprland) using xbps-src.

### Usage

Follow the instructions at [void-packages](https://github.com/void-linux/void-packages) to set up xbps-src for building templates.

1) Clone this repository:

```
$ git clone https://github.com/Makrennel/hyprland-void.git
$ cd hyprland-void
```

2) Append shlibs to the end of your void-packages shlibs

```
$ cat common/shlibs >> ~/void-packages/common/shlibs
```

3) Copy srcpkgs to your void-packages srcpkgs directory

```
$ cp -r srcpkgs/* ~/void-packages/srcpkgs
```

4) Build and install packages
```
$ cd ~/void-packages
$ ./xbps-src pkg hyprland
$ sudo xbps-install -R hostdir/binpkgs hyprland
```

### Running

In order to run Hyprland you will need to install some additional packages which will depend on your setup, for example a [session and seat manager](https://docs.voidlinux.org/config/session-management.html) and [graphics drivers](https://docs.voidlinux.org/config/graphical-session/graphics-drivers/index.html).

### Updating

If Hyprland updates and this repository changes, you may want to perform a hard reset and clean of your cloned void-packages repository to ensure changes are correctly applied when repeating steps 2 and 3 after a git pull on both void-packages and hyprland-void. (BEWARE: This will also reset any changes you have made to any other packages locally - you will have to figure it out yourself in this case)
```
$ sudo xbps-install -Su # Update system normally first to avoid building every package needing update from source

$ cd ~/void-packages
$ git clean -fd
$ git reset --hard
$ git pull

$ cd ~/hyprland-void
$ git pull

$ cat common/shlibs >> ~/void-packages/common/shlibs # Repeat steps 2 and 3
$ cp -r srcpkgs/* ~/void-packages/srcpkgs

$ cd ~/void-packages
$ ./xbps-src update-sys
```

### Extra
This repository also includes other templates which may be of interest for:

- hyprpaper
- xdg-desktop-portal-hyprland

There is also a template for [hyprland-nvidia](https://wiki.hyprland.org/Nvidia) which includes the [patch used in the AUR](https://aur.archlinux.org/cgit/aur.git/tree/nvidia.patch?h=hyprland-nvidia), however this is unofficial and untested, and as far as I know non-functional. Please refer to [issue #1](https://github.com/Makrennel/hyprland-void/issues/1) and try to collaborate to figure it out if this interests you.
