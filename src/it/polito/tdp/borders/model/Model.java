package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Multigraph;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
	private BordersDAO dao ;
	private CountryIdMap countryIdMap ;
	private List<Country> countries ;
	private List<Border> borders ;
	private Graph<Country, DefaultEdge> graph ;
	private Simulator s ;
	
	public Model() {
		countryIdMap = new CountryIdMap();
	}

	public List<Border> calcolaConfini(int year) {
		dao = new BordersDAO() ;
		
		if(year >= 1816 && year <=2006) {
			countries = dao.getCountriesOfYear(year, countryIdMap) ;
			borders = dao.getAllBorders(year, countryIdMap) ;
			System.out.println(borders.toString());
			return borders ;
		} else
			throw new IllegalArgumentException();
		
	}

	public void createGraph() {
		graph = new SimpleGraph<>(DefaultEdge.class) ;
		
		Graphs.addAllVertices(this.graph, countries);
		
		for(Border b : borders) {
			if(graph.vertexSet().contains(b.getC1()) && graph.vertexSet().contains(b.getC2()))
				graph.addEdge(b.getC1(), b.getC2());
		}
		
	}

	public List<Country> getNumStatiConfinanti() {
		
		List<Country> list = new ArrayList<>() ;
		for(Country c : graph.vertexSet()) {
			if(graph.degreeOf(c) > 0) {
				c.setBorders(graph.degreeOf(c));
				list.add(c) ;
			}
		}
		
		Collections.sort(list, new Country.OrdinaPerBorders());
		
		return list ;
	}
	
	public List<Country> getGraphVertexSet(){
		List<Country> list = new ArrayList<>() ;
		for(Country c : graph.vertexSet())
			list.add(c) ;
		Collections.sort(list, new Country.OrdinaPerNome());
		return list ;
	}

	public void simula(Country country) {
		s = new Simulator();
		s.init(this.graph, country);
		s.run();
	}

	public int getTSimulazione() {
		return s.getT();
	}

	public List<Country> getCountriesStanziali() {
		Map<Country, Integer> map = s.getStanziali();
		List<Country> ltemp = new ArrayList<>();
		for(Country c : map.keySet()) {
			c.setNumbers(map.get(c));
			ltemp.add(c);
		}
		Collections.sort(ltemp, new Country.NumbersDESC());
		return ltemp;
	}
}
