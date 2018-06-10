package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

public class Simulator {
	
	//1) quali sono le tipologie di eventi/coda degli eventi?
	private PriorityQueue<Event> queue ;
	
	//2) qual è il modello del mondo?
	private Map<Country, Integer> stanziali ;
	private Graph<Country, DefaultEdge> graph ;
	
	//3) quali sono i parametri di simulazione?
	private int N_MIGRANTI = 1000;
	
	//4) quali sono le variabili di output?
	private int T ; //passi di simulazione
	
	public void init(Graph<Country, DefaultEdge> graph, Country partenza) {
		T = 1;
		
		queue = new PriorityQueue<Event>();
		this.queue.add(new Event(T, N_MIGRANTI, partenza));
		
		stanziali = new HashMap<Country, Integer>();
		for(Country c : graph.vertexSet()) 
			this.stanziali.put(c, 0);
		
		this.graph = graph ;
	}
	
	public void run() {
		Event e ;
		
		while((e=queue.poll())!=null) {
			
			this.T= e.getT();
			
			int arrivi = e.getNum() ;
			Country stato = e.getDestination();
			
			List<Country> confinanti = Graphs.neighborListOf(this.graph, stato);
			
			int migranti = (arrivi/2)/confinanti.size();
			
			if(migranti!=0) {
				for(Country arrivo : confinanti) {
					queue.add(new Event(e.getT()+1, migranti, arrivo));
				}
			}
			
			int rimasti = arrivi - migranti * confinanti.size();
			
			this.stanziali.put(stato, this.stanziali.get(stato) + rimasti);
		}
		
	}

	public Map<Country, Integer> getStanziali() {
		return stanziali;
	}

	public int getT() {
		return T;
	}
	
}
