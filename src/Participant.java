/**
 * Anton Jotoft, anjo8022 
 * Gabriel Destici, gade3589 
 * Lucas Hudert, luhu8240
 */

import java.util.ArrayList;

public class Participant {

	private String name;
	private String lastName;
	private Team team;
	private int number;
	private ArrayList<Result> result;

	public Participant(String name, String lastName, Team team, int number) {
		this.name = name;
		this.lastName = lastName;
		this.team = team;
		this.number = number;
		this.result = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public String getLastName() {
		return lastName;
	}

	public Team getTeam() {
		return team;
	}

	public int getNumber() {
		return number;
	}
	
	public ArrayList<Result> getResult(){
		return result;
	}
	
	public boolean checkAttempts(Event event){
		int resultsAmmount = 0;
		for (Result r : result) {
			if (r.getParticipant() == this && r.getEvent() == event) {
				++resultsAmmount;
			}
		}
		if (resultsAmmount >= event.getAttemptsAllowed()) {
			System.out.println("Too many attempts!");
			return true;
		}
		return false;
	}
	
	public void addResult(Result r){
		result.add(r);
	}
	
	public void printResults(){
		if (result.isEmpty()) {
			System.out.println(this + " does not have any results yet!");
			return;
		}

		ArrayList<Event> temp = new ArrayList();
		for (Result r : result) {
			if (!temp.contains(r.getEvent()))
				temp.add(r.getEvent());
		}
		for (Event event : temp) {
			System.out.print("Results for " + getName() + " " + getLastName() + " in "
					+ event.getEventName() + ": ");
			for (Result tempResult : result) {
				if (tempResult.getEvent() == event) {
					System.out.print(tempResult.getValue() + ", ");
				}
			}
			System.out.println();
		}
		}

	public String toString() {
		return name + " " + lastName + " from " + team.getTeamName() + " with number " + (number);
	}
}
