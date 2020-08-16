# README #

### What is this repository for? ###

* This is a PIRCBOTX IRC bot that I made to learn java, now its a project of different experimentation and play with IRC bots
* PIRCBOTX 2.1

### Docker Environment
All of the environment variables the bot loads from docker are put into the configuration files at first start. If the config files exist, then these will not be used, and the config file settings will be used instead.
* BOT_CONFIG_FOLDER
* BOT_NICK
* BOT_PASSWORD
* BOT_OWNER_NICK
* BOT_IRC_PORT
* BOT_LOGIN
* BOT_IRC_ADDRESS
* BOT_CHANNEL_LIST
* BOT_IRPG_HOST_BOT_NAME
* BOT_IRPG_CHANNEL
* BOT_IRPG_USERNAME
* BOT_IRPG_PASSWORD
* BOT_IRPG_USER_CLASS


### How do I get set up? ###

* Building can be done using ant or docker
  * docker build . -t wheatley
  * ant clean-build
* .jar dependencies are included in the repo to provide a baseline configuration

### Contribution guidelines ###

* Follow the general style of the latest commits, style has changed a few times since this was started and not everything is setup the same way
* Pull requests will be reviewed before accepting
