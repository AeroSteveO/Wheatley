## Wheatley

Wheatley is a bot that has been developed by Steve-O by fumbling around theTardis’ code, which was made by theDoctor, burg, Vanilla and jason89s. Wheatley is a derpy bot that is a conglomeration of functions from other bots, and some new functions, who whores himself out in many side channels on dhirc, doing random things from playing games, to kicking people, to just creating random messages. This is an exhaustive list of his functions, in no particularly useful order.

### Definition Functions

**[Public]**
* [Word/Phrase]?
  * Outputs the definition for the given word or phrase
* !RandDef
  * Responds with a random definition from the database
* !Whodef [definition]
  * Responds with who created that definition and when
* !List Defs
  * Responds with a PM of all the definitions contained in the database
* tell [user] about [definition]
  * Sends a PM to the input user with the definition requested
  
**[Bot Owner Only Commands]**
* !MkDef [word/phrase] @ [definition]
  * Adds the given word/definition combo to the definition file
  * Example: \<Steve-O> !mkdef Wheatley @ that one idiot bot
* !RmDef [Word/Phrase]
  * Deletes the given word/phrase from the definition file
  * Example: \<Steve-O> !rmdef Wheatley
* !OverDef [Word/Phrase] @ [Definition]
  * Updates the given word with the new definition
  * Example: \<Steve-O> !overdef Wheatley @ hyper intelligent ai
* !Load
  * Loads previous style definitions text file into the new JSON system
        
### Markov Chain Functions
**[Public]**
* !Mute
* Wheatley shutup
* Wheatley, shutup
  * Mutes Wheatley until someone un-mutes him
* !Speak
* Wheatley, speak up
  * Allows Wheatley to speak until someone mutes him
* !Line
* Wheatley, go on
* Wheatley, continue
  * Makes Wheatley speak a line based on the previous message
* Wheatley, what do you think of [given item]
* Wheatley, what do you think about [given item]
  * Makes Wheatley use the last word in the given item as the seed word to generate a line
  * Example: <Steve-O> Wheatley, what do you think of cisco
  * <Wheatley> we only need one of his managed cisco systems, we make the internet lol
* !words
  * Responds with the number of words, contexts, contexts/word that Wheatley knows, and the number of lines that these were learned from
* !Ignore List
  * Lists out the ignored nicks
     
**[Bot Owner Only]**
* !Save
  * Saves all current lines that have been added to the Markov database
* !Ignore [nick]
  * Tells the markov package to ignore all lines from that nickname, to prevent it from learning useless lines

**[Channel Owners and Bot Owner Only Commands]**
* !Markov Chance [number]
  * Sets the probability of Wheatley saying a line (1/x, 1/1 being the max)

### Useful Chat Functions
**[Public]**
* !Randchan [board]
  * Responds with a link to a random image on 4chan from the given board, if no board is given, the link will be to a random image from a random board
* !Randchan list
* !Randchan dict
* !Board list
  * Responds with a PM with all the boards available to randchan and the subsequent titles of those boards
* !Board [4chan board]
  * Gives the topic of the input board name
  * Example: \<~Steve-O> !board b
  * \<&Wheatley> b: Random
* !IP [URL/IP]
  * Responds with information about that IP, location, service provider, etc
* !Ping [URL/IP] [Port]
  * pings the IP address and tells you if it responds or not, port is assumed 80 if no port is given
  * Example: !ping google.com
  * Example: !ping mc.yoursite.net 25565
* !BF
* !BF [nick]
  * BlackFox’s the line by reversing the word order of the previous line said, or the newest line spoken by the input nick
* !hashtagify
  * Turns the previous line into a hashtag
  * #TurnsThePreviousLineIntoAHashtag
  
**[Bot Owner Only Commands]**
* !Ping Check [XML Group]
  * pings through the list of servers and informs you which are online and which are offline
  * Example: !ping check lan

### Math Functions
**[Public]**
* !base [int 0-36] [int]
  * Changes the base of a number from decimal to the supplied integer
  * \<Steve-O> !base 16 64589713
  * \<Wheatley> Decimal Value: 64589713 Base 16: 3D98F91
* !RPN [expression]
  * Evaluates Reverse-Polish-Notation expressions
  * \<Steve-O> !rpn 2 4 * 2 / 9 -
  * \<Wheatley> RPN Solution: -5
  
### Casino Bot Functions
**(Blocked from #dtella)**

**[Public]**
* !Omgword
  * Outputs a random word that has had the letters mixed up, for the user to solve within 30sec
* !Hangman
  * Plays a game of hangman, supports guessing letter by letter, accepts either uppercase or lowercase letters, and also supports full word guessing, which can give you a bonus onto your winnings
* !Bomb
* !Bomb Classic
  * Puts a bomb in the players hand to defuse by cutting one of the colored wires, classic mode has set colors whereas the standard mode has random colors
* !Reverse
  * Plays a game where you have to reverse the given word
* !Lotto
  * Gives the current winnings of the lotter (before tax)
* !Lotto List
  * Gives a list of the currently guessed lotto numbers
* !Lotto [int (0 to 100)]
  * Purchases a lotto ticket of that number, if correct, the lotto winnings will be yours, if incorrect, you lose money
* !Lotto [min]-[max]
  * Buys the lotto tickets one by one from the minimum to the maximum, if the lottery is won, no more lotto tickets will be purchased
* !Altreverse
* esreveR!
  * Plays a game where you have to reverse the given word
* !Mastermind [length] [chars] [lives]
  * Plays a game of mastermind, guessing the code, can be played without any modifier, or with the modifiers added in the order shown, defaults to 5, 2, and 10
    * Length: Number of total characters in the code [1-10]
    * Chars: Number of unique characters in the code [1-10]
    * Lives: Number of guesses you can make before losing
* !GuessTheNumber [Upper Bound]
  * Plays a game where you try to guess the number between 1 - [upperbound], if no upper bound is input, it is set to 100.
* !HighLow
  * Starts a game of highlow, where you're given a card from a deck, and have to guess whether the next card will be higher or lower than the one you have
* !Slots [bet value]
  * Plays a game of slots using the input value as your bet, if no value is input, the bet is considered to be $10
* !BlackJack [minimum bet] [options]
  * Starts a game of blackjack with Wheatley as the dealer, and the minimum bet set to the input value. At any point in time during the game, other users can use !dealmein to be added to the game in the next hand.
  * If no value is input, the minimum bet is set to $10
  * Option: Multi - Before dealing the first hand, asks others from the Chan to join the game. Note: Others can join in regular blackjack games without the multi tag.
* !DealMeIn
  * Tells Wheatley to deal you into the next hand if a card game is currently active
* !DealMeOut
  * Tells Wheatley that you don't want to be dealt into the next hand if a card game is currently active
  * Note: this command does not end your hand
* !Money
  * Responds with your current money for/from Wheatley's games
* !Money [user]
  * Responds with the only user's money for/from Wheatley's games
* !MakeItRain [amount]
  * Splits the amount into equal shares and gives it to everyone in the channel. If no amount is input, the amount is assumed to be 100
*!Give [user] [amount]
  * Gives the input user the input amount, if the input amount is negative, it sends an error to the user, if you don't have enough to give, it sends an error to the user
    * !FuckThis
    * I give up
      *  Both of these commands will end the current game
      
**[Bot Owner & Game Admin Only Commands]**
* !Cheat [user] [time] [extended reason]
  * Bans the user for the input amount of time, fines them $500, includes the extended reason on the kick
* !Cheat [user]
  * Bans the user for 10 minutes for cheating, also fines them for $500
* !Cheat [user] [extended reason]
  * Bans the user for 10 minutes, fines them $500, includes the extended reason on the kick

**[Bot Owner Only Commands]**
* !Merge [user a] [user b]
  * Merges the score of user a into user b, and resets user a's score to the base score used by the scoring array
* !Save
  * Saves everyone's score to JSON and removes duplicate entries if any were made
* !List games
  * Lists out the currently active games to the sender
* !Money [user] [amount]
  * Sets the input users money to the input money value

### UNO Bot Functions 
*(Blocked from #dtella)*

**[Public Game Commands]**
* !Uno
  * Starts an new UNO game.
* !Uno +a
  * Attack mode: When you draw there is a 20% chance that you will be UNO attacked and will have to draw anywhere from 0 - 7 cards!
* !Uno +e
  * Extreme mode: This inserts twice as many special cards into the deck! Special cards include:
  * R, S, D2, W, and WD4
* !Uno +e +a 
  * Enables both Extreme and Attack mode!
* !Join
  * Joins an existing UNO game.
* !Deal
  * Deals out the cards to start a UNO game, but only the person that started the game can deal
* !Wait
  * Stops your turn timer.
* !Play
  * Plays a card (!play <color> <face>) or (!p <color> <face>)
  * to play a RED FIVE !play r 5
* !Showcards 
  * Shows you your hand. (!hand)
* !Draw
  * Draws a card when you don't have a playable card, then you pass.
* !Pass
  * If you don't have a playable card after you draw then you pass.
* !Leave
  * If you want to leave the game early.
* !UnoCount
  * Show how many cards each player has.
* !What
  * If you were not paying attention this will tell you the top card and whos turn it is.
* !Players
  * Displays the player list.
* !Endgame
  * Ends the game, only the person who started the game may end it.

**[Public General Commands]**
* !Score
  * Prints out the score board.
* !AI
  * Turns the bot ai on or off.
* !Rank
  * Shows all users win:lose ratio
* !UnoHelp
  * This stuff

**[Bot Owner & Game Admin Only Commands]**
* !UnoAIHelp 
  * Responds with a command list for the AI (Only available if AI is on).
* !EndGame
  * Ends the current game no matter who started it.
* !ResetSB
  * Resets the Score Board.

**[Bot Owner & Game Admin Only AI Commands]**
* !NickAI
  * Tells the bot to change his nick.
* !JoinCAI
  * Tells the bot to join a channel.
* !JoinGame
  * Tells the bot to send the '!join' command.
* !Uno
  * Tells the bot send !join to join the uno game.
* !Quit
  * Tells the bot to disconnect from the entire server.

### Random Chat Functions 
*(randomly generated response, or just slightly less dumb functions)*

**[Public]**
* !Bane [term1] [term2] [term3] [term4]
  * If only no terms are input, outputs Bane’s original speech, if one term is input, outputs a slightly modified version, if 2 terms are input, outputs an even more modified version of Bane’s speech
  * Example:   \<Steve-O> !bane W X Y Z
  * \<Wheatley> Ah you think W is your ally? You merely adopted the X. I was born in it, molded by it. I didn't see the Y until I was already a man, by then it was nothing to me but Z!
  * \<Steve-O> !bane X
  * \<Wheatley> Ah you think X is your ally? You merely adopted the X. I was born in it, molded by it. I didn't see the light until I was already a man, by then it was nothing to me but blinding!
  * \<Steve-O> !bane X Y
  * \<Wheatley> Ah you think X is your ally? You merely adopted the X. I was born in it, molded by it. I didn't see the Y until I was already a man, by then it was nothing to me but blinding!
  * \<Steve-O> !bane X Y Z
  * \<Wheatley> Ah you think X is your ally? You merely adopted the Y. I was born in it, molded by it. I didn't see the Z until I was already a man, by then it was nothing to me but blinding!
* !Ignite [it]
  * Responds with randomly generated sentence about lighting [it] on fire, if nothing is input, then it will simply light “it” on fire
* !Laser [it]
  * Responds with randomly generated sentence about doing something to [it] pertaining to lasers, if nothing is input, then it will simply add lasers to “it”
* !Saying [, - .]
  * If nothing is input, it’ll choose one of the three following methods to generate a random saying, if one of the following is input, then it’ll generate that type of message
    * [,] - Generates a random 2 part comma separated saying
    * [-] - Generates a random 2 part hyphenated saying
    * [.] - Returns a random saying from the saying database
* !Why
  * Wheatley, why [whatever question you have]?
  * Responds with a randomly generated sentence as to why
* !Shakespeare [nick]
  * Responds with randomly generated Shakespearean insult to the nick input, if no nick is input, then it’ll insult “Thou”
* !Slander [nick]
* !Insult [nick]
  * Responds with a randomly generated insult to the nick input, if no nick is input, then it’ll insult “You”
* !Fact
  * Responds with a random fact by the fact sphere
* !Cave
* Cave Johnson
* Cave Johnson Here
  * Responds with a random quote by Cave Johnson, or Cave Johnson Prime, or Dark Cave Johnson

### Link Generating Functions
**[Public]**
* !Bash [number]
  * Responds with a link to the bash.org quote with that number
* !XKCDB [number]
  * Responds with a link to the xkcdb.com quote with that number
* !Shorten [URL]
  * Returns a shortened version of the URL using Bit.ly
* !Shorten [tag] [URL]
  * Returns a shortened version of the URL using the specified link shortener
  * Supported Tags/Shorteners:
  * b    Bit.ly
  * i    is.gd

### Kick Functions
**[Public]**
* !ListKicks
  * Responds with a PM containing all the currently enabled and disabled kicks

**[Semi-Public]**
* ![Kick-Command] [user]
  * Kicks the input user using the input kick command

**[Bot Owner Only]**
* !AddKick [Command] [Message]
  * Adds the input kick with the input message to the kick list. The command can only be a single word, whereas the message can be as long as irc will allow a single message.
* !AddKick [-c, -m, -f, -b, -u, -w] [settings]
  * Adds the kick using the input command, message, etc, allowing more granular control over who can use the kick command and where the kick command can be used.
  * [-c] - The command for the kick
  * [-m] - The message for the kick itself
  * [-f] - The kick message when a user tries to use the kick when they don’t have access granted to it
  * [-b | -w] - A space delimited list of channels where usage of the command is controlled, -w signifies that the command can only be used in listed channels (whitelist) whereas -b signifies that the kick cannot be used in the listed channels (blacklist)
  * [-u] - A space delimited list of users who are granted access to the command
* !DelKick [command]
  * Deletes a kick matching the input command string
* !EnableKick [command]
  * Checks the disabled kick list and enables the kick matching the input command string if found
* !DisableKick [command]
  * Removes a kick matching the input command string from the enabled kick list and adds it to the disable kick list

### Dumb Chat Functions 
*(generally one output, no input commands, or just dumb commands)*

**[Channel Owners and Bot Owner Only Commands]**
* !Hack [nickname]
  * Kicks the given nickname, if the sender doesn’t have access to this command, it will kick them instead
* !Smash [nickname]
  * Kicks the given nickname, if the sender doesn’t have access to this command, it will kick them instead
     
**[Public]**
* !Penis
  * Responds with penis ascii art
* !Boobs
* !BOTD
* !Melons
  * Responds with boob ascii art
* !Butt
  * PM's the user with an ASCII butt
* !Vagina
  * Responds with an ASCII vagina
* !Flag
* !America
* !Merika
* !Merica
  * PM's the user with a full color ASCII american flag
* Give [user] some freedom
  * PM’s the input user with a full color ASCII american flag
* !Space
* SPACE
  * Responds with…. SPACEEEEEE
* !Wheatley
  * Responds with link to this document
* THIS STATEMENT IS FALSE
  * Responds with the corresponding quote by Wheatley
* Wheatley, you’re a moron
  * Responds with the corresponding quote by Wheatley
* Oh. Hi.
  * Responds with GLaDOS’s quote about being a potato

S### RSBSNS Mirror Functions 
*(only function if srsbsns is not in the channel)*

**[Public]**
* !LastURL
  * Outputs the last URL seen by the bot
* !SecondLastURL
  * Outputs the second-last URL seen by the bot
* !Summon [user]
  * Sends a PM to that user saying that they have been summoned by you

### Hermes Mirror Functions
*(only function if hermes is not in the channel)*

**[Public]**
* !Tell [user] [message]
  * sends the input user the given message via pm

### BlarghleBot Mirrored Functions
*(only function if BlarghleBot is not in the channel)*

**[Channel Owners and Bot Owner Only Commands]**
* !Troll [nick]
  * Kicks the nickname from the channel, or kicks the sender if they don’t have access to the command

**[Public]**
* Blarghlebot, transform and rollout
* Wheatley, transform and rollout
  * Kicks the sender
* !DtellaUsers
* !Users
  * Gives the number of dtella users, #dtella users, and total visible users
* I put on my robe and wizard hat
  * Kicks the sender
* !Kickme
  * Kicks the sender
* !Suicide
  * Kicks the sender
* !Banme
  * Sets a 1 minute time ban on the sender, then kicks them
* !Russianroulette
  * Plays a game of russian roulette, where losing gets you kicked
* sw/[find this string or regex]/[replace with this]
* s/[find this string or regex]/[replace with this]
  * Attempts the given string or regex in the previous ~100 lines of chat and replaces it with the second string in the command
  * Example: <Steve-O> will this work 1
  * \<Steve-O> s/1/nope
  * \<Wheatley> <Steve-O> will this work nope
* Blarghlebot, [any kind of question]?
* Wheatley, [any kind of question]?
  * Responds with a magic 8-ball message
* !Passthepoop
  * When 2 users send this command, responds with a message about pooping back and forth between them
* !Roll [x]d[y]
  * Rolls X number of Y sided dice
* !Xzibit [item1] [item2]
  * Forms a ‘yo dog, i heard you like...’ statement using item1 and item2
* !Hmmmmm
  * Responds with a gentlemanly message
* !Trololol
  * Responds with a link to Eduard Khil’s famous song
* Ba dum
* Badum
  * psh
* !Burn
  * Responds with link to specific quote
* !Clitoris
  * Links to the dtella quote about the clitoris
* !Udon
  * Responds with link to specific quote
* !Rimshot
  * Responds with link to instant rimshot website
* !Vuvuzela
  * Responds with the vuvuzela noise
* !Headon
  * HEADON APPLY DIRECTLY TO FOREHEAD
* !g [whatever you want to search for]
  * Responds with LMGTFY link, to insult people who don’t know how to google things
* !Blarghlebot
  * Responds with a randomly generated sentence
* ![number]
  * Responds with a link to the dtella quote with that number
* !Badwords    
  * Lists out the words that will get users below hop kicked
* !DropTheBass
  * UNCE UNCE UNCE

### Bot Statistics Functions
**[Public]**
* !Uptime
  * Responds with the bot’s current uptime
* !Sysinfo
  * Responds with ram usage information, as well as the number of active threads
* !Threads
  * Responds with statistics on currently active threads, and how many threads the bot has spawned since it was started
* !Ram
  * Responds with detailed RAM and heap usage statistics
* !OS
  * Responds with information on Wheatley’s current operating system.

### Latent Functions 
*(Stuff that runs automagically)*
* Kicks users below HOP for saying bad words (see !Badwords)
* Keeps track of the previous 2 URLs posted in channels that Wheatley’s in
* Adds all lines not spoken by bots or bot commands to a db of Markov chain lines
* Auto-joins channels when invited via chanserv
* Keeps logs of all messages and /me actions
* Keeps a log of all definitions changed/deleted from the database
* Watches music pre channels and announces new albums to #rapterverse

### IdleRPG Functions
**[Bot Owner Only Commands]**
* !irpg
  * Logs the bot into idle RPG
* !regirpg
  * Registers the bot with an idle RPG bot

**[Automated Functionality]**
* Join a configured idle RPG channel
* Login after joining the channel to a configured bot

### Administrative Functions
**[Bot Owner Only Commands]**
* Wheatley, please shutdown
* Wheatley, shutdown
* !Shutdown
  * Turns off Wheatley
* Wheatley, join [#channel]
* Wheatley, please join [#channel]
  * Commands Wheatley to join the given channel
* Wheatley, leave [#channel]
* Wheatley, please leave [#channel]
  * Commands Wheatley to leave the given channel, if no channel is given, it’ll leave the one the command is sent from
* !Flush
  * Clears out variables from various functions to clear any possible errors
* Wheatley, fix yourself
  * Ghosts main nick, changes to main nick, identifies to nickserv, joins channels saved to settings XML
* !Update [filename] [addition]
  * Adds the word to the given text file, currently for the bad word list
  * Example: !update badwordlist defenitly
* !Channels
  * Responds with a list of channels that the bot is currently connected to
* !Save
  * Saves the current log variables to a .plog file, saves current Markov lines, and saves game scores
* !Say [sentence]
  * Has Wheatley repeat the input sentence in the channel the command was sent from
* !Say [#channel] [sentence]
  * Has Wheatley say the input sentence in the input channel
* !Act [action]
  * Has Wheatley repeat the input action in the channel the command was sent from
* !Act [#channel] [action]
  * Has Wheatley repeat the input action in the input channel
* !Set [key] [value] [channel]
  * Sets the given key to the given value, if a channel is input, it sets the given key for the given channel to the given value
* !Create [key] [value] [channel]
  * Creates the key/value pair under the given channel, if no channel is input, then it creates the key/value pair in the general settings
* !Get [key] [channel]
  * Returns the value for the key, if a channel is input, returns the value for the key within that channel group
* !Contains [tree]
  * Returns true or false if the input tree map is contained in the settings file
* !Relay
  * Enables or disables the multi server relay, in which Wheatley relays messages between #rapterverse channels on multiple irc servers

**[Channel Owners and Bot Owner Only Commands]**
* Wheatley, leave
* Wheatley, please leave
  * Commands Wheatley to leave the channel the command is sent from
* !Throttle [type] [log || time] [#channel] [int input]
  * In order to adjust throttling, a modifier and integer must be input, the modifier can be log or time, to correlate to how many calls may be made in a set time
* !Throttle [type] [#channel]
  * Returns the current throttle settings for that channel, if no channel is input, returns the throttle settings for the channel the command was run within
 
        
        
