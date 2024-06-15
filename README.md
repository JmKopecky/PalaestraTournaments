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
* Rince and repeat until all matches in the tournament are done

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
* 