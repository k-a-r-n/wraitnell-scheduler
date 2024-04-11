This is a scheduler service for Baerod's Wraitnell campaign. This was a learning project for me (for both Spring and
Java) and it probably shows. A lot of the comments in here are basically notes as I worked through various problems,
but I've also tried to comment intelligently to make the intent clear.

The application has several parts:

1) The web service and database. That's this.
2) A Foundry VTT plugin. (PENDING) This does a couple things:
  a) Connects to the calendar plugin SimpleCalendar and feeds the service data about the current campaign date,
     as well as how the calendar is configured so we can do downtime calculations without needing to hardcode the
     calendar config.
  b) Provides a UI for Baerod to start the session, which kicks off a number of processes. See SessionService.
3) A web front end (PENDING)

All app configuration is done in application.properties. Currently you have to edit the file directly to configure anything
but adding a user interface to maintain those values is on my list.
