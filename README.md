# Plan For The Software


* Clients connect to the server and start a competition session.
* Server is controlled by another client that is connecting to another page of the server (with authentication).
  * From now on, this controlling client is referred to as the competition facilitator
* Competition facilitator sets up tournament (sending info to the server)
  * Determining how many matches and what type and brackets and stuff
* Clients join the session with the server at the start of every match
* For each match, competition facilitator loads the test
* Before match begins, competition facilitator checks everything is good
* Send the questions to the clients depending on how the test is configured by competition facilitator
* Clients take the test, competition facilitator monitors
* Once both have finished/time limit, score is computed automagically and a winner is determined
* Rinse and repeat until all matches in the tournament are done

## Workflow

### Pre-competition setup

* Server is started up and confirmed to work over LAN.
* Competition Facilitator connects to server and opens the tournament setup page.
* In this page, they configure their tournament event.
  * Determines the type of tournament here.
    * Bracket based (requiring competitor number equal to 2^n).
    * Placement based (any number of competitors, given placements).
      * One test that rules them all.
      * Multiple tests conducted.
      * Either way, tests MUST be done at the same time to ensure integrity of the competition (no async testing).
  * Create all competitors and assign their names and ids. Generate a sign on key for each competitor.
* From this point, tournament is ready and the competition facilitator can edit the competition or start matches.

### Conducting the competition

* Competition Facilitator starts one of the matches, opening the pre-match menu for that match
* Competition facilitator loads a test for that match, determining any other settings such as scoring
  * System for synchronous hosting of matches if another competition facilitator connects to the server and starts or opens another match
* In pre-match, competitors connect to the server and verify their name using the sign on key provided to the competition facilitator
* Competition facilitator sees what competitors have been associated with a client (team has signed in) and when ready can start the contest

Match Start: 

* Each competitor client is sent the test and shown a list of questions.
* For each question, the user can submit a response to the server
* Server verifies that the answer is correct (answer is never stored client side except in the case of showing one answer in a multiple choice question)
* Server returns the result to the client and stores it for later scoring.
  * Other clients are informed that the opposing team has answered the question (or otherwise that they tried and failed)
* Once the end condition has been met, clients are notified by server and the match is ended

Post Match: 

* Each competitor's results are processed by the server to determine score. 
* Competition facilitator is first shown results to verify, they have to confirm the results for them to be sent to the clients.
* Server records the winner and updates brackets if necessary.



# Feature Requirements

### Brackets
* Single elimination bracket
* Free for all
  * One match to win them all
  * Multiple matches with scores totaled at the end