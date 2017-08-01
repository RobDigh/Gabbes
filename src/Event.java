
/**Anton Jotoft, anjo8022
Gabriel Destici, gade3589
Lucas Hudert, luhu8240*/

import java.util.ArrayList;
import java.util.Collections;

public class Event {

	private String eventName;
	private int attempts;
	private ArrayList<Result> result;

	public Event(String eventName, int attemptsAllowed) {
		this.eventName = eventName;
		attempts = attemptsAllowed;
		this.result = new ArrayList<>();
	}

	public String getEventName() {
		return eventName;
	}

	public void addResult(Result r) {
		result.add(r);
	}

	public ArrayList<Result> bestResults() {
		ArrayList<Participant> tempPar = new ArrayList<>();
		ArrayList<Result> tempResult = new ArrayList<>();
		for (Result r : result) {
			if (!tempPar.contains(r.getParticipant())) {
				tempPar.add(r.getParticipant());
				tempResult.add(r);
			}
		}
		return tempResult;
	}

	public void eventResult() {
		Collections.sort(result);
		Result previousResult = new Result(null, -1.0, this);
		int sharedResultTimes = 0;
		System.out.println("Results for " + eventName);
		ArrayList<Result> tempResult = bestResults();
		for (Result display : tempResult) {
			if (display.getValue() == previousResult.getValue()) {
				System.out.println(tempResult.indexOf(display) - sharedResultTimes + " "
						+ String.format("%.2f", display.getValue()) + " " + display.getParticipant().getName() + " "
						+ display.getParticipant().getLastName());
				sharedResultTimes++;
			} else {
				System.out.println(tempResult.indexOf(display) + 1 + " " + String.format("%.2f", display.getValue())
						+ " " + display.getParticipant().getName() + " " + display.getParticipant().getLastName());
				sharedResultTimes = 0;
				previousResult = display;
			}
		}
	}

	public ArrayList<Result> sortResult() {
		ArrayList<Result> bestResult = bestResults();
		Result temp;
        for (int i = 0; i < bestResult.size(); i++) {
            for (int x = 0; x < bestResult.size() - i - 1; x++) {
                if (bestResult.get(x).getValue() < bestResult.get(x + 1).getValue()) {
                    temp = bestResult.get(x);
                    bestResult.set(x, bestResult.get(x + 1));
                    bestResult.set(x + 1, temp);
                }
            }
        }
		return bestResult;
	}

	public ArrayList<Result> sortResults() {
		ArrayList<Result> bestResult = new ArrayList<>();
//		for (Result y : result) {
//			if (y.getEvent() == this) {
//				boolean contains = false;
//				if (!bestResult.isEmpty()) {
//					for (Result r : bestResult) {
//						if (y.getParticipant() == r.getParticipant() && y.getEvent().equals(r.getEvent())) {
//							contains = true;
//						}
//						if ((contains && y.getParticipant() == r.getParticipant() && r.getEvent() == y.getEvent()
//								&& y.getValue() < r.getValue())) {
//							bestResult.remove(r);
//							bestResult.add(y);
//							break;
//						}
//					}
//				}
//				if (!contains) {
//					bestResult.add(y);
//				}
//			}
//		}
//		Collections.sort(bestResult);
//		for (Result r : bestResult)
//			System.out.println(r);
		return bestResult;
	}

	public void removeResult(Participant person) {
		ArrayList<Result> temp = new ArrayList<>();

		for (Result r : result) {
			if (r.getParticipant().equals(person))
				temp.add(r);
		}
		result.removeAll(temp);
		temp.clear();

	}

	public int getAttemptsAllowed() {
		return attempts;
	}

	public String toString() {
		return eventName;
	}
}