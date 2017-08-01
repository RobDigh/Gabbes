
/**Anton Jotoft, anjo8022
Gabriel Destici, gade3589
Lucas Hudert, luhu8240*/

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

public class Contest {
	private Scanner input = new Scanner(System.in);
	private ArrayList<Event> events = new ArrayList<>();
	private ArrayList<Participant> participants = new ArrayList<Participant>();
	private ArrayList<Team> teams = new ArrayList<>();
	private ArrayList<Integer> removedList = new ArrayList<>();

	private void run() {
		while (true) {
			interpretCommand(menuChoice());
		}
	}

	private String prompt(String prompt) {
		System.out.println(prompt);
		return input.nextLine();
	}

	private String menuChoice() {
		writeMenu(); // SKRIVER UT ALLA ALTERNATIV
		return prompt("Command> ").toLowerCase();
	}

	private void writeMenu() {
		System.out.println("The following commands are available:");
		System.out.println(" add event \t  add participant \t remove participant"
				+ "\n add result \t  participant \t\t teams \t\t  " + "\n reinitialize \t  exit");
		// System.out.println("where commands followed by percent signs
		// enclosing text"
		// + "\n(i.e. %TEXT%) indicate variable user input");
	}

	private void interpretCommand(String command) {
		switch (command) {
		case "add event":
			addEvent();
			break;
		case "add participant":
			addParticipant();
			break;
		case "remove participant":
			removeParticipant();
			break;
		case "add result":
			addResult();
			break;
		case "participant":
			participant();
			break;
		case "teams":
			teams();
			break;
		case "reinitialize":
			reinitialize();
			break;
		case "exit":
			System.exit(0);
		default:
			if (command.startsWith("message ")) {
				message(command.substring(8));
				break;
			}
			if (command.trim().isEmpty()) {
				System.out.println("Unknown command " + '"' + command.trim() + '"');
				break;
			}
			boolean valid = false;
			for (Event e : events) {
				if (fixCase(command).equalsIgnoreCase(e.getEventName())) {
					eventResult(fixCase(command));
					valid = true;
					break;
				}
			}
			if (!valid) {
				System.out.println("Unknown command " + '"' + command + '"');
			}
		}
	}

	private void message(String message) {
		System.out.println("\n");
		System.out.println("############################################################");
		System.out.println("#                                                          #");
		while (message.length() > 56) {
			System.out.println("# " + message.toUpperCase().subSequence(0, 56) + " #");
			message = message.substring(56);
		}
		String spaces = "";
		for (int l = 0; l < 56 - message.length(); l++)
			spaces = spaces.concat(" ");
		System.out.println("# " + message.toUpperCase() + spaces + " #");
		System.out.println("#                                                          #");
		System.out.println("############################################################");
		System.out.println("\n");
	}

	private boolean teamsRunnable() {
		boolean dataComplete = true;

		if (participants.isEmpty()) {
			System.out.println("There are no participants yet!");
			dataComplete = false;
		}

		if (teams.isEmpty()) {
			System.out.println("There are no teams yet!");
			dataComplete = false;
		}

		if (events.isEmpty()) {
			System.out.println("There are no events yet!");
			dataComplete = false;
		}

		return dataComplete;
	}

	private void teams() {
		if (!teamsRunnable())
			return;
		for (Team a : teams)
			a.removeMedals();
		for (Event event : events) {
			ArrayList<Result> sorted = event.sortResult();
			Result previousResult = new Result(null, -1.0, event);
			int sharedResultTimes = 0;
			if (!sorted.isEmpty()) {
				for (Result result : sorted) {
					// ifall resultatet är samma som förra
					if (result.getValue() == previousResult.getValue()) {
						sharedResultTimes++;
						result.getParticipant().getTeam().setMedals(sorted.indexOf(result) - sharedResultTimes);
					}
					// om resultatet inte är samma som förra
					else {
						if (sorted.indexOf(result) >= 3)
							break;
						result.getParticipant().getTeam().setMedals(sorted.indexOf(result));
						sharedResultTimes = 0;
						previousResult = result;
					}
				}
			}
		}
		Collections.sort(teams);
		Collections.reverse(teams);
		System.out.println("1st    2nd    3rd    Team name");
		System.out.println("************************************");
		for (Team printing : teams)
			System.out.println(printing);
	}

	private void participant() {
		Participant display = checkParticipant();
		if (display != null)
			display.printResults();
	}

	private void reinitialize() {
		teams.clear();
		events.clear();
		participants.clear();
		message("ALL DATA HAS BEEN REMOVED");
	}

	private void eventResult(String eventName) {
		Event event = null;
		for (Event tempEvent : events) {
			if (eventName.equalsIgnoreCase(tempEvent.getEventName())) {
				event = tempEvent;
				break;
			}
		}
		event.eventResult();
	}

	private void addResult() {
		if (participants.isEmpty()) {
			System.out.println("There are no participants!");
			return;
		}
		if (events.isEmpty()) {
			System.out.println("There are no events!");
			return;
		}
		Participant participant = checkParticipant();
		if (participant == null)
			return;
		System.out.println("Eventname: ");
		String eventName = fixCase(input.nextLine());
		Event event = null;
		for (Event compare : events) {
			if (eventName.equalsIgnoreCase(compare.getEventName())) {
				event = compare;
				break;
			}
		}
		if (event == null) {
			System.out.println("Event could not be found");
			return;
		}
		
		if(!participant.checkAttempts(event)){
			double result = -1.0;
			while (result < 0.0) {
				System.out.println("Result for " + participant.getName() + " " + participant.getLastName() + " from "
						+ participant.getTeam().getTeamName() + ": ");
				result = input.nextDouble();
				input.nextLine();
				if (result < 0)
					System.out.println("Must be greater than or equal to zero!");
			}
			Result addresult = new Result(participant, result, event);
			event.addResult(addresult);
			participant.addResult(addresult);
		}
	}

	private Participant checkParticipant() {
		int checkNum = intPrompt("Number: ");
		Participant find = null;
		for (Participant tempPart : participants) {
			if (tempPart.getNumber() == checkNum)
				find = tempPart;
		}
		if (find == null) {
			System.out.println("No participant with number " + checkNum + " exists");
		}
		return find;
	}

	private void removeParticipant() {
		Participant delete = checkParticipant();
		if (delete == null)
			return;
		int teamMembers = 0;
		removeResults(delete);
		for (Participant tempPart : participants) {
			if (tempPart.getTeam().equals(delete.getTeam())) {
				teamMembers++;
			}
		}
		if (teamMembers <= 1) {
			teams.remove(delete.getTeam());
		}
		System.out.println(delete + " removed");
		delete.getResult().clear();
		for (Result r : delete.getResult()) {
			delete.getResult().remove(r);
		}
		for(Event e : events){
			e.removeResult(delete);
		}
		participants.remove(delete);
		removedList.add(delete.getNumber());
	}

	private void removeResults(Participant person) {
		for (Event e: events)
			e.removeResult(person);
	}

	private String checkName(NameType nameType, int resultNum) {
		String name = "";
		boolean retry = true;
		if (resultNum != -1)
			retry = false;
		while (fixCase(name).isEmpty()) {
			System.out.println(fixCase(nameType.toString()).concat(" name: "));
			name = fixCase(input.nextLine());
			if (!retry)
				return null;
			if (fixCase(name).isEmpty()) {
				System.out.println("Names can't be empty!");
			}
		}
		switch (nameType) {
		case EVENT: {
			for (Event event : events) {
				if (event.getEventName().equals(name)) {
					System.out.println(name + " already exists!");
					return "-1";
				}
			}
			return name;
		}
		case FIRST:
		case LAST:
			return name;
		case TEAM: {
			for (Team team : teams)
				if (team.getTeamName().equals(name))
					return name;
			teams.add(new Team(name));
			return name;
		}
		default:
			break;
		}
		return null;
	}

	private void addParticipant() {
		String firstName = checkName(NameType.FIRST, -1);
		String lastName = checkName(NameType.LAST, -1);
		String teamName = checkName(NameType.TEAM, -1);
		Team team = null;
		int number = 100;
		for (Team tempTeam : teams) {
			if (teamName.equalsIgnoreCase(tempTeam.getTeamName())) {
				team = tempTeam;
				break;
			}
		}
		if (!participants.isEmpty()) {
			int highest = 100;
			for (Participant tempPart : participants) {
				if (tempPart.getNumber() >= highest)
					highest = tempPart.getNumber();
			}
			number = (highest + 1);
			if(removedList.contains(number))
				number++;
		}

		Participant d = new Participant(firstName, lastName, team, number);
		participants.add(d);
		System.out.println(d + " added");
	}
	
	private String fixCase(String string) {
		if (string == null)
			return "";
		if (string.trim().equals(""))
			return "";
		return string = string.trim().substring(0, 1).toUpperCase() + string.trim().substring(1).toLowerCase();
	}

	private int intPrompt(String prompt) {
		System.out.println(prompt);
		int intRead = input.nextInt();
		input.nextLine();
		return intRead;
	}

	private int checkAttemptsAllowed() {
		int attemptsAllowed = 0;
		while ((attemptsAllowed = intPrompt("Attempts allowed: ")) < 1)
			System.out.println("Attempts can't be 0!");
		return attemptsAllowed;
	}

	private void addEvent() {
		String eventName = "";
		while (eventName.isEmpty()) {
			eventName = checkName(NameType.EVENT, -1);
		}
		if (eventName.equals("-1"))
			return;
		Event e = new Event(eventName, checkAttemptsAllowed() /*isBiggerBetter()*/);
		events.add(e);
		System.out.println(e.getEventName() + " added");
	}
	
	public static void main(String[] args) {
		Contest contest = new Contest();
		contest.run();
	}

}