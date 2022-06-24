<h1 align="center"> Squidly </h1> <br>

---

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Feedback](#feedback)
- [Building and Running](#Building-and-Running)

## Introduction

![Java Version](https://img.shields.io/badge/Java-17-important?style=flat-square&logo=java)
![GitHub issues](https://img.shields.io/github/issues/likqez/squidly-v2?style=flat-square)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](http://makeapullrequest.com)
[![Support Server](https://img.shields.io/discord/678733739504697375.svg?color=7289da&label=dotSpace%20Dev&logo=discord&style=flat-square)](https://discord.gg/mFfDMAEFWE)

Advanced Discord-Bot satisfying your competitive needs whilst playing [Paladins][pala]!

**Invite to your server [here](https://dotspace.dev/squidly/invite)**

## Features

A few of the things you will be able to do with Squidly:

- Retrieve & display data directly from Hi-Rez
- Works with every Platform (Switch, Steam, Epic ...)
- Visualize opposing teams and showing their stats
- Show Level, Rank, Playtime and more rich information

## Feedback

Feel free to send me feedback on [Twitter](https://twitter.com/likqez)
or [file an issue](https://github.com/likqez/Squidly-v2/issues/new). Feature requests are always welcome.  
Also feel free to submit a PR!  https://makeapullrequest.com/

If there's anything you'd like to chat about, please feel free to join our [Discord][spacedc]!

## Building and Running

#### Building

&rarr; Execute ``gradle build shadowJar`` on the root project to create the executable fat jar.

#### Running

- Save your discord bot token in envirnoment variable ``squidly_bot_token``.
- Save the hirez dev. credentials in the following env. variables:  
  Your Hirez DevId into: ``squidly_devid``
  and your authentication key into: ``squidly_authkey``.

&rarr; Finally grab the jar archive from the latest [release][gh-releases] or from ``discord/build/libs/``
and **execute** it with your favourite JVM.

[pala]: <https://www.paladins.com/>
[hirez]: <http://www.hirezstudios.com/>
[spacedc]: <https://discord.gg/mFfDMAEFWE>
[gh-releases]: <https://github.com/Likqez/Squidly-v2/releases>
