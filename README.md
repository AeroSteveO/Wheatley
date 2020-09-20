# README #

### What is this repository for? ###

* This is a PIRCBOTX IRC bot that I made to learn java, now its a project of different experimentation and play with IRC bots
* [Bot commands](Commands.md) are available with descriptions of what they do

### Docker Environment
All of the environment variables the bot loads from docker are put into the configuration files at first start. If the config files exist, then these will not be used, and the config file settings will be used instead.
* BOT_CONFIG_FOLDER
  * The path to use for all of the files needed by the bot
* BOT_NICK
  * The nickname to use on IRC, this is loaded into the configuration file and should be updated there after the first startup
* BOT_PASSWORD
  * The nickname to use on IRC, this is loaded into the configuration file and should be updated there after the first startup
* BOT_OWNER_NICK
  * The nickname to use on IRC, this is loaded into the configuration file and should be updated there after the first startup
* BOT_IRC_PORT
  * The IRC server port, this is loaded into the configuration file and should be updated there after the first startup
* BOT_LOGIN
  * The nickname to use on IRC, this is loaded into the configuration file and should be updated there after the first startup
* BOT_IRC_ADDRESS
  * The IRC server address, this is loaded into the configuration file and should be updated there after the first startup
* BOT_CHANNEL_LIST
  * A comma separated list of channels to join, this is loaded into the configuration file and should be updated there after the first startup
* BOT_IRPG_HOST_BOT_NAME
  * the IRPG hosting bot name, used when logging into IRPG, this is loaded into the configuration file and should be updated there after the first startup
* BOT_IRPG_CHANNEL
  * the channel to use for IRPG, this is loaded into the configuration file and should be updated there after the first startup
* BOT_IRPG_USERNAME
  * the username that the bot should use when creating/logging into IRPG, this is loaded into the configuration file and should be updated there after the first startup
* BOT_IRPG_PASSWORD
  * the password that the bot should use when creating/logging into IRPG, this is loaded into the configuration file and should be updated there after the first startup
* BOT_IRPG_USER_CLASS
  * the bots IRPG class, used when creating an account for IRPG, this is loaded into the configuration file and should be updated there after the first startup


### Building Wheatley ###

* Building can be done using ant or docker
  * docker build . -t wheatley
  * ant clean-build
* .jar dependencies are included in the repo to provide a baseline configuration

### Contribution guidelines ###

* Follow the general style of the latest commits, style has changed a few times since this was started and not everything is setup the same way
* Pull requests will be reviewed before accepting
