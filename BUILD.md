# LevelDB

## Windows Platform

1. Install [MSYS2](http://msys2.github.io/) for correspond architecture.

1. Update the system packages:

     ```
     pacman --needed -Sy bash pacman pacman-mirrors msys2-runtime
     ```
    
1. Close MSYS2, run it again from Start menu and update the rest:

     ```
     pacman -Su
     ```
    
1. Install packages for development:

     ```   
    ﻿ pacman -S base-devel git
     ```
    
1. Install toolchain for correspond architecture:

     For Windows x64:

     ``` 
     pacman -S mingw-w64-x86_64-toolchain
     ```
    
     For Windows x86:
   
     ``` 
     pacman -S mingw-w64-i686-toolchain
     ```
    
