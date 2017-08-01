/**Anton Jotoft, anjo8022
Gabriel Destici, gade3589
Lucas Hudert, luhu8240*/


public class Result implements Comparable<Result> {
	 
  private double value;
  private Participant participant;
  private Event event;

  public Result(Participant participant, double value, Event event){
      this.value = value;
      this.participant = participant;
      this.event = event;
  }
 
  public double getValue() {
      return value;
  }

  public Participant getParticipant() {
      return participant;
  }
 
  public Event getEvent() {
      return event;
  }

  public String toString() {
      return "Results for " + participant.getName()+" "+participant.getLastName() + " in " + event + ": " + value;
  }

  @Override
  public int compareTo(Result result) {
      return Double.compare(result.getValue(), this.value);
  }
 
  public int compareName(Result other) {
      return event.getEventName().compareTo(other.getEvent().getEventName());
  }
}