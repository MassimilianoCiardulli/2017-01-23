package it.polito.tdp.borders.db;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.CountryIdMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BordersDAO {
	
	public List<Country> loadAllCountries( CountryIdMap countryIdMap) {
		
		String sql = 
				"SELECT ccode,StateAbb,StateNme " +
				"FROM country " +
				"ORDER BY StateAbb " ;

		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet rs = st.executeQuery() ;
			
			List<Country> list = new LinkedList<Country>() ;
			
			while( rs.next() ) {
				
				Country c = new Country(
						rs.getInt("ccode"),
						rs.getString("StateAbb"), 
						rs.getString("StateNme")) ;
				
				list.add(countryIdMap.get(c)) ;
			}
			
			conn.close() ;
			
			return list ;
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null ;
	}
	
	/*public static void main(String[] args) {
		List<Country> list ;
		BordersDAO dao = new BordersDAO() ;
		list = dao.loadAllCountries() ;
		for(Country c: list) {
			System.out.println(c);
		}
	}*/

	public List<Border> getAllBorders(int year, CountryIdMap countryIdMap) {
		final String sql = "select state1no, state2no, year " + 
				"from contiguity " + 
				"where year <= ? "
				+ "and conttype = '1'" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, year);
			ResultSet rs = st.executeQuery() ;
			
			List<Border> result = new ArrayList<Border>() ;
			
			while( rs.next() ) {
				Country c1 = countryIdMap.get(rs.getInt("state1no"));			
				Country c2 = countryIdMap.get(rs.getInt("state2no"));	
			
				result.add(new Border(c1, c2, rs.getInt("year")));
			}
			
			conn.close() ;
			
			return result ;
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public List<Country> getCountriesOfYear(int year, CountryIdMap countryIdMap) {
		final String sql = "SELECT * " + 
				"FROM country as c " + 
				"WHERE c.CCode in( " + 
				"	SELECT state1no " + 
				"	FROM contiguity as c2 " + 
				"	WHERE c2.year <= ? AND conttype=1" + 
				") ";
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, year);
			ResultSet rs = st.executeQuery() ;
			
			List<Country> result = new ArrayList<>() ;
			
			while( rs.next() ) {
				Country c = new Country(
						rs.getInt("ccode"),
						rs.getString("StateAbb"), 
						rs.getString("StateNme")) ;
				result.add(countryIdMap.get(c));
			}
			
			conn.close() ;
			
			return result ;
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
}
