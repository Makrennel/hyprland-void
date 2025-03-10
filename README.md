## Hyprland for Void Linux

This repository contains template files and binaries for building or installing [Hyprland](https://github.com/hyprwm/Hyprland) on Void Linux.

### Installation

The easiest way to install Hyprland on Void Linux is using the [binary repository](https://github.com/Makrennel/hyprland-void/tree/repository-x86_64-glibc) which is built automatically using [GitHub Actions](https://github.com/Makrennel/hyprland-void/blob/master/.github/workflows/build-latest.yml) whenever a new commit is pushed to this repository.

You can add this repository to xbps's repositories by creating a file such as `/etc/xbps.d/hyprland-void.conf` with the following text:

```
repository=https://raw.githubusercontent.com/Makrennel/hyprland-void/repository-x86_64-glibc
```

This can be done with the following command:
```
echo repository=https://raw.githubusercontent.com/Makrennel/hyprland-void/repository-x86_64-glibc | sudo tee /etc/xbps.d/hyprland-void.conf
```
Then you need to refresh your repositories and accept the repository's fingerprint:
```
sudo xbps-install -S
```

You should now be able search through all hypr related packages provided by this repository, and install packages as usual:

```
xbps-query -Rs hypr
sudo xbps-install -S hyprland xdg-desktop-portal-hyprland
```

Currently this repository provides binary packages for:

- x86_64-glibc
- x86_64-musl
- aarch64-glibc
- aarch64-musl

Change the end of the url in `/etc/xbps.d/hyprland-void.conf` as appropriate with the above options.

### Running

In order to run Hyprland you will need to install some additional packages which will depend on your setup, for example a [session and seat manager](https://docs.voidlinux.org/config/session-management.html) and [graphics drivers](https://docs.voidlinux.org/config/graphical-session/graphics-drivers/index.html). You may also have to add the user to the `_seatd` group. If you use an Nvidia GPU refer to the [Hyprland Wiki](https://wiki.hyprland.org/Nvidia), but keep in mind that Hyprland does not officially support Nvidia.

### Extra
There are packages in this repository which may be of interest for:

- hypridle
- hyprlock
- hyprpaper
- xdg-desktop-portal-hyprland

### Common Remarks
#### "Why is this so out of date"?

Upstream Void Linux packages are sometimes chronically out of date and Hyprland tends to follow the bleeding edge. I will not risk breaking someone's system by providing, for example, a core package like `GCC 14` ahead of whenever upstream is ready to do so.

#### "Will this be merged into upstream void-packages eventually?"

Unless Void Linux's maintainers decide to change their opinion on the matter, the answer is no. Void Linux is hostile towards Hyprland, its developers, and community, so if this is a problem you should probably consider using a different distribution.

### Manually Building

You may want to build these templates manually, for example if you have a specific configuration requirement that needs to be set at build time. Void-packages may sometimes have specific packages which are out of date from time to time that need to be updated beforehand in order to update Hyprland, which is why this repository is not simply forked off it. We need to copy the modifications from this repository on top of a fresh void-packages clone in order to build manually.

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
cp -r --remove-destination srcpkgs/* ../void-packages/srcpkgs
```

6) Build and install packages

```
cd ../void-packages
./xbps-src pkg hyprland
sudo xbps-install -R hostdir/binpkgs hyprland
```

### Contributing and Forking

Any contributions are greatly appreciated, but please bear in mind that the build actions run on `x86_64` with `glibc` and you should make sure that it cross-compiles with xbps-src's `-a` flag for both `musl` and `aarch64` - for example with `./xbps-src -a aarch64-musl pkg new-hypr-package`.

Please also try not to superfluously change things when pull requesting with this repository, and use your own name and email in the maintainer section of new templates: do not contribute on behalf of someone else if they are not involved with the pull request. Where possible, commit changes separately (rather than in huge lump commits) and describe the changes so contributions can be easily understood and cherry picked as needed.

If you would like to create your own fork of this repository and use the build action for your own packages, you must either create a private repository called `hyprland-void-private-pem` where you will store your signing keys and fetch them using a GitHub Private Access Token stored in your repository's secrets called `PEM_PAT`, or store the signing key directly in your secrets and modify [`scripts/sign-packages`](https://github.com/Makrennel/hyprland-void/blob/master/scripts/sign-packages) and the [build action](https://github.com/Makrennel/hyprland-void/blob/master/.github/workflows/build-latest.yml) appropriately. You cannot install packages from remote repositories without signing them, and *DO NOT* put the private signing key in your public repository.  You will also need to create a GitHub Personal Access Token so that the action can delete, create, and push the branches where the finished packages and repodata is stored.

For information on signing your repository, see the [Void Linux documentation](https://docs.voidlinux.org/xbps/repositories/signing.html) and `xbps-rindex`'s [man page](https://man.voidlinux.org/xbps-rindex.1).
