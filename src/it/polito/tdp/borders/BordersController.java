/**
 * Skeleton for 'Borders.fxml' Controller Class
 */

package it.polito.tdp.borders;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class BordersController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtAnno"
    private TextField txtAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxNazione"
    private ComboBox<String> boxNazione; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    private Model model ;

    @FXML
    void doCalcolaConfini(ActionEvent event) {
    	try {
    		int year = Integer.parseInt(this.txtAnno.getText());
    		if(year < 0) {
    			this.txtResult.setText("L'anno deve essere positivo, compreso tra 1816 e 2006.\n");
    			return ;
    		}	
    		List<Border> borders = model.calcolaConfini(year) ;
    		if(borders.isEmpty()) {
    			this.txtResult.setText("Per l'anno selezionato non sono disponibili confini via terra.\n");
    			return ;
    		}
    		model.createGraph();
    		
    		for(Country c : model.getGraphVertexSet())
    			this.boxNazione.getItems().add(c.getStateName()) ;
    		
    		List<Country> list = model.getNumStatiConfinanti() ;
    		
    		for(Country c : list)
    			this.txtResult.appendText(c.getStateName() + " = "+c.getBorders() + "\n");
    			
    	} catch (NumberFormatException e) {
    		this.txtResult.setText("Inserisci un anno valido (numero intero positivo).\n");
    	} catch(IllegalArgumentException e) {
    		this.txtResult.setText("L'anno deve essere compreso tra 1816 e 2006...\n");
    	}
    }

    @FXML
    void doSimula(ActionEvent event) {
    	String country = this.boxNazione.getValue() ;
    	
    	if(country == null) {
    		this.txtResult.appendText("Scegli una nazione.\n");
    		return ;
    	}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Borders.fxml'.";
        assert boxNazione != null : "fx:id=\"boxNazione\" was not injected: check your FXML file 'Borders.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Borders.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model ;
	}
}
