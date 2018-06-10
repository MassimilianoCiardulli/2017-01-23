package it.polito.tdp.borders.model;

public class Event implements Comparable<Event>{

	private int t ;
	private int num ; //numero di persone che si spostano
	private Country destination ; //nazione di destinazione
	
	
	public Event(int t, int num, Country destination) {
		super();
		this.t = t;
		this.num = num;
		this.destination = destination;
	}


	@Override
	public int compareTo(Event other) {
		
		return this.t - other.t;
	}


	public int getT() {
		return t;
	}


	public int getNum() {
		return num;
	}


	public Country getDestination() {
		return destination;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Event [t=");
		builder.append(t);
		builder.append(", num=");
		builder.append(num);
		builder.append(", destination=");
		builder.append(destination);
		builder.append("]");
		return builder.toString();
	}

}
