/**
 * Anton Jotoft, anjo8022 
 * Gabriel Destici, gade3589 
 * Lucas Hudert, luhu8240
 */

public class Team implements Comparable<Team> {
	private String teamName;
	private int[] medals;

	public Team(String teamName) {
		this.teamName = teamName;
		this.medals = new int[3];
	}

	public String getTeamName() {
		return teamName;
	}

	public void removeMedals() {
		medals = new int[] { 0, 0, 0 };
	}

	public void setMedals(int index) {
		medals[index]++;
	}

	public String toString() {
		return medals[0] + "	" + medals[1] + "	" + medals[2] + "	" + teamName;
	}

	@Override
	public int compareTo(Team o) {
		int cmp = medals[0] - o.medals[0];
		if(cmp !=0){
			return cmp;
		}
		cmp = medals[1] - o.medals[1];
		if(cmp !=0){
			return cmp;
		}
		cmp = medals[2] - o.medals[2];
		if(cmp != 0)
			return cmp;
		return o.getTeamName().compareTo(teamName);
	}

}