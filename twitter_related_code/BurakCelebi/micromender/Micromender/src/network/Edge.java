package network;

public class Edge {

	private String fromUser, toUser;
	private int weight;

	public Edge(String fromUser, String toUser, int weight) {
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.weight = weight;
	}

	@Override
	public String toString() {
		return String.format("%20s->%20s (%3d)", fromUser, toUser, weight);
	}
	
	public String getFromUser() {
		return fromUser;
	}
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}
	public String getToUser() {
		return toUser;
	}
	public void setToUser(String toUser) {
		this.toUser = toUser;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	
}
